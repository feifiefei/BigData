package cn.fei.report

import cn.fei.config.ApplicationConfig
import cn.fei.utils.{SparkUtils, StreamingUtils}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.{DataFrame, Dataset, Row, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.DataTypes

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/28 0028 21:29
 */
object RealTimeOrderReport {
  /** 实时统计：总销售额，使用sum函数 */
  def reportAmtTotal(streamDataFrame: DataFrame) = {
    // a. 导入隐式转换
    val session = streamDataFrame.sparkSession
    import session.implicits._
    //业务计算
    val resultStreamDF: DataFrame = streamDataFrame
      .agg(sum($"money").as("total_amt"))
      .withColumn("total", lit("global"))

    // c. 输出Redis及启动流式应用
    resultStreamDF.writeStream
      .outputMode(OutputMode.Update())
      .queryName("query-amt-total")
      // 设置检查点目录
      .option("checkpointLocation", ApplicationConfig.STREAMING_AMT_TOTAL_CKPT)
      // 结果输出到Redis
      .foreachBatch { (batchDF: DataFrame, _: Long) =>
        batchDF
          .coalesce(1) // 降低分区数目
          // TODO: 行转列
          .groupBy()
          .pivot($"total").sum("total_amt")
          // 添加一列, 统计类型
          .withColumn("type", lit("total"))
          .write
          .mode(SaveMode.Append)
          .format("org.apache.spark.sql.redis")
          .option("host", ApplicationConfig.REDIS_HOST)
          .option("port", ApplicationConfig.REDIS_PORT)
          .option("dbNum", ApplicationConfig.REDIS_DB)
          .option("table", "orders:money")
          .option("key.column", "type")
          .save()
      }
      .start() // 启动start流式应用
  }

  def reportAmtProvince(streamDataFrame: DataFrame) = {
    // a. 导入隐式转换
    val session = streamDataFrame.sparkSession
    import session.implicits._

    // b. 业务计算
    val resultStreamDF: Dataset[Row] = streamDataFrame
      /*
        province        total_amt             江苏省     安徽省      上海市
        江苏省             9999.99     ->     9999.99    8888.88    9999.66
        安徽省             8888.88
        上海市             9999.66
              |           |
          orders:money:province   ->    江苏省-> 9999.99    安徽省-> 8888.88   上海市->9999.66
       */
      .groupBy($"province")
      .agg(sum($"money").as("total_amt"))

    // c. 输出Redis及启动流式应用
    resultStreamDF.writeStream
      .outputMode(OutputMode.Update())
      .queryName("query-amt-province")
      // 设置检查点目录
      .option("checkpointLocation", ApplicationConfig.STREAMING_AMT_PROVINCE_CKPT)
      // 结果输出到Redis
      .foreachBatch { (batchDF: DataFrame, _: Long) =>
        batchDF
          .coalesce(1) // 降低分区数目
          // TODO: 行转列
          .groupBy()
          .pivot($"province").sum("total_amt")
          // 添加一列, 统计类型
          .withColumn("type", lit("province"))
          .write
          .mode(SaveMode.Append)
          .format("org.apache.spark.sql.redis")
          .option("host", ApplicationConfig.REDIS_HOST)
          .option("port", ApplicationConfig.REDIS_PORT)
          .option("dbNum", ApplicationConfig.REDIS_DB)
          .option("table", "orders:money")
          .option("key.column", "type")
          .save()
      }
      .start() // 启动start流式应用
  }

  def reportAmtCity(streamDataFrame: DataFrame) = { // a. 导入隐式转换
    val session = streamDataFrame.sparkSession
    import session.implicits._

    // 重点城市：9个城市
    val cities: Array[String] = Array(
      "北京市", "上海市", "深圳市", "广州市", "杭州市", "成都市", "南京市", "武汉市", "西安市"
    )
    val citiesBroadcast: Broadcast[Array[String]] = session.sparkContext.broadcast(cities)
    // 自定义UDF函数，判断是否重点城市
    val city_is_contains: UserDefinedFunction = udf(
      (cityName: String) => citiesBroadcast.value.contains(cityName)
    )

    // b. 业务计算
    val resultStreamDF: Dataset[Row] = streamDataFrame
      // 过滤重点城市订单数据
      .filter(city_is_contains($"city"))
      /*
        city           total_amt             北京市     上海市      深圳市
        北京市             9999.99     ->     9999.99    8888.88    9999.66
        上海市             8888.88
        深圳市             9999.66
              |           |
          orders:money:city   ->    北京市-> 9999.99    上海市-> 8888.88   深圳市->9999.66
       */
      .groupBy($"city")
      .agg(sum($"money").as("total_amt"))

    // c. 输出Redis及启动流式应用
    resultStreamDF.writeStream
      .outputMode(OutputMode.Update())
      .queryName("query-amt-city")
      // 设置检查点目录
      .option("checkpointLocation", ApplicationConfig.STREAMING_AMT_CITY_CKPT)
      // 结果输出到Redis
      .foreachBatch { (batchDF: DataFrame, _: Long) =>
        batchDF
          .coalesce(1) // 降低分区数目
          // TODO: 行转列
          .groupBy()
          .pivot($"city").sum("total_amt")
          // 添加一列, 统计类型
          .withColumn("type", lit("city"))
          .write
          .mode(SaveMode.Append)
          .format("org.apache.spark.sql.redis")
          .option("host", ApplicationConfig.REDIS_HOST)
          .option("port", ApplicationConfig.REDIS_PORT)
          .option("dbNum", ApplicationConfig.REDIS_DB)
          .option("table", "orders:money")
          .option("key.column", "type")
          .save()
      }
      .start() // 启动start流式应用}
  }

  def main(args: Array[String]): Unit = {
    // 1. 获取SparkSession实例对象
    val spark: SparkSession = SparkUtils.createSparkSession(this.getClass)
    import spark.implicits._
    // 2. 从KAFKA读取消费数据
    val kafkaStreamDF: DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS)
      .option("subscribe", ApplicationConfig.KAFKA_ETL_TOPIC)
      // 设置每批次消费数据最大值
      .option("maxOffsetsPerTrigger", ApplicationConfig.KAFKA_MAX_OFFSETS)
      .load()
    // 3. 提供数据字段
    val orderStreamDF: DataFrame = kafkaStreamDF
      // 获取value字段的值，转换为String类型
      .selectExpr("CAST(value AS STRING)")
      // 转换为Dataset类型
      .as[String]
      // 过滤数据：通话状态为success
      .filter(record => null != record && record.trim.split(",").length > 0)
      // 提取字段：orderMoney、province和city
      .select(
        // 由于对金额数据进行sum操作，需要转换订单金额为BigDecimal类型
        get_json_object($"value", "$.orderMoney")
          .cast(DataTypes.createDecimalType(10, 2)).as("money"), //
        get_json_object($"value", "$.province").as("province"), //
        get_json_object($"value", "$.city").as("city") //
      )
    // 4. 实时报表统计：总销售额、各省份销售额及重点城市销售额
    reportAmtTotal(orderStreamDF)
    reportAmtProvince(orderStreamDF)
    reportAmtCity(orderStreamDF)
    // 5. 定时扫描HDFS文件，优雅的关闭停止StreamingQuery
    spark.streams.active.foreach { query =>
      StreamingUtils.stopStructuredStreaming(query, ApplicationConfig.STOP_STATE_FILE)
    }
  }
}
