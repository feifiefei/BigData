package cn.fei.parquet

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @description:SparkSQL读取Parquet列式存储数据
 * @author: 飞
 * @date: 2020/11/22 0022 20:59
 */
object SparkSQLParquet {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .master("local[3]")
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      //设置sparksql在shuffle阶段的分区数据，实际项目根据数据量调整
      .config("spark.sql.shuffle.partitions", 4)
      .getOrCreate()
    import spark.implicits._
    //加载数据  SparkSQL默认读取数据格式为perquet
    val inputDF: DataFrame = spark.read.format("parquet").parquet("datas/resources/users.parquet")
    inputDF.printSchema()
    inputDF.show(10, truncate = -1)
    spark.stop()
  }
}
