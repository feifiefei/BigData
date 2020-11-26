package cn.fei.wcSession

import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions._

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/23 0023 14:47
 */
object WDCDSL {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .master("local[3]")
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      .config("spark.sql.shuffle,partitions", "4")
      .getOrCreate()
    import spark.implicits._
    //加载数据
    val inputDS: Dataset[String] = spark.read.textFile("datas/wordcount.data")
    //    valueDS.groupBy("value").count().show(10, truncate = false)
    //    inputDS.printSchema()
    //    inputDS.show(10, truncate = false)
    //DSL处理
    val valueDS: Dataset[String] = inputDS
      //过滤无用数据
      .filter(line => line != null && line.split("\\s+").length > 0)
      .flatMap(line => line.split("\\s+"))
    //sql处理
    //注册表视图
    valueDS.createOrReplaceTempView("word")
    spark.sql(
      """
        |select value,count(value) as ccc from word group by value
        |""".stripMargin)
      .show(10, truncate = false)
    Thread.sleep(1000000)
    spark.stop()

  }

}
