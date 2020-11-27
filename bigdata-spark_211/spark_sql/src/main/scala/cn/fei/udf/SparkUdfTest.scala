package cn.fei.udf

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/23 0023 14:24
 */
object SparkUdfTest {
  def main(args: Array[String]): Unit = {
    //构建连接
    val spark: SparkSession = SparkSession.builder()
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      .master("local[3]")
      .getOrCreate()
    import spark.implicits._
    //获取数据
    val inputDF: DataFrame = spark.read.json("datas/resources/employees.json")
    //自定义udf函数功能将某列的数据转化为大写
    spark.udf.register(
      "to_lower",
      (name: String) => {
        name.toLowerCase()
      }
    )
    //注册DataFrame为临时视图
    inputDF.createOrReplaceTempView("view_tmp_emp")
    spark.sql(
      """
        |SELECT name, to_lower(name) AS lower_name  FROM view_tmp_emp
        |""".stripMargin)
      .show(10, truncate = false)
    println("======================================================================")
    //在DSL中使用
    val udfLower = udf(
      (name: String) => name.toLowerCase()
    )
    inputDF.select(
      $"name",
      udfLower($"name").as("lowname")
    ).show(10, truncate = false)
    spark.stop()
  }
}
