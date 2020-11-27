package com.fei.etl

import com.fei.config.ApplicationConfig
import com.fei.utils.{IpUtils, SparkUtils}
import org.apache.spark.SparkFiles
import org.apache.spark.internal.Logging
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.lionsoul.ip2region.{DbConfig, DbSearcher}

/**
 * 广告数据进行ETL处理，具体步骤如下：
 * 第一步、加载json数据
 * 第二步、解析IP地址为省份和城市
 * 第三步、数据保存至Hive表
 */

object PmtEtlRunner extends Logging {
  /**
   * 格式转换ip转换为省份、城市
   *
   * @param dataFrame
   * @return
   */
  def processData(dataFrame: DataFrame): DataFrame = {
    // 从DataFrame中获取SparkSession实例对象
    val spark: SparkSession = dataFrame.sparkSession
    //将数据字典文件进行分布式缓存
    spark.sparkContext.addFile(ApplicationConfig.IPS_DATA_REGION_PATH)
    //a.提取出每条数据中的IP地址，调用工具类进行解析，转换为省份和城市
    //如果调用的是DataFrame的类似RDD的转换函数，建议先转化为RDD再使用转换函数
    //todo:原因在于DataFrame属于类型不安全，弱类型
    val value: RDD[Row] = dataFrame.rdd.mapPartitions { iter =>
      // 创建DbSearcher对象，针对每个分区创建一个，并不是每条数据创建一个
      val dbSearcher = new DbSearcher(new DbConfig(), SparkFiles.get("ip2region.db"))

      iter.map { row =>
        //从Row中获取Ip字段值
        val ipValue = row.getAs[String]("ip")
        val region: Region = IpUtils.convertIpToRegion(ipValue, dbSearcher)
        //将省份和城市追加到原来的Row中
        val newSeq: Seq[Any] = row.toSeq :+ (region.province) :+ (region.city)
        Row.fromSeq(newSeq)
      }
    }
    //b. 定义Schema信息，在原来的Schema基础上加上province和city
    val cityType = dataFrame.schema
      .add("province", StringType, nullable = true)
      .add("city", StringType, nullable = true)

    val dataFM = dataFrame.sparkSession.createDataFrame(value, cityType)
    dataFM.withColumn("date_str", date_sub(current_date(), 1).cast(StringType))
  }

  /**
   * 保存为parquet文件
   *
   * @param dataFrame
   */
  def saveAsParquet(dataFrame: DataFrame) = {
    dataFrame
      .coalesce(1) //降低分区数
      .write
      .mode(SaveMode.Overwrite)
      .partitionBy("date_str")
      .parquet("dataset/pmt-etl")
  }

  /**
   * 保存数据至Hive分区表中，按照日期字段分区
   *
   * @param dataFrame
   */
  def saveAsHiveTable(dataFrame: DataFrame) = {
    dataFrame
      .coalesce(1)
      .write
      .mode(SaveMode.Append)
      .format("hive") //一定要指定为hive数据源，否则会报错
      .partitionBy("date_str")
      .saveAsTable("itcast_ads.pmt_ads_info")
    logWarning("数据正在存储入hive表")
  }

  /**
   * 程序入口
   *
   * @param args
   */
  def main(args: Array[String]): Unit = {
    //设置以管理员身份运行
    System.setProperty("user.name", "root")
    System.setProperty("HADOOP_USER_NAME", "root")
    // 1. 创建SparkSession实例对象
    val spark: SparkSession = SparkUtils.createSparkSession(this.getClass)
    // 2. 加载json数据
    val pmtDF: DataFrame = spark.read.json(ApplicationConfig.DATAS_PATH)
    //pmtDF.printSchema()
    //pmtDF.show(10,truncate = false)
    // 3. 解析IP地址为省份和城市
    val etlDF: DataFrame = processData(pmtDF)
    etlDF.printSchema()
    etlDF.show(10, truncate = false)
    //保存数据值parquet列式存储文件中
    //saveAsParquet(etlDF)
    // 4. 保存ETL数据至Hive分区表
    saveAsHiveTable(etlDF)
    // 5. 应用结束，关闭资源
    spark.stop()
  }
}
