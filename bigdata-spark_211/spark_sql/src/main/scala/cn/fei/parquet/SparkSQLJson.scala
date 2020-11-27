package cn.fei.parquet

import org.apache.parquet.format.IntType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/22 0022 21:05
 */
object SparkSQLJson {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .master("local[3]")
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      //设置sparksql在shuffle阶段的分区数据，实际项目根据数据量调整
      .config("spark.sql.shuffle.partitions", 4)
      .getOrCreate()
    import spark.implicits._
    //加载数据
    val inputDS: Dataset[String] = spark.read.textFile("datas/resources/employees.json")
//    inputDS.printSchema()
//    inputDS.show(10, truncate = false)
    import org.apache.spark.sql.functions._
    val userDF: DataFrame = inputDS.select(
      get_json_object($"value", "$.name").as("name"),
      get_json_object($"value", "$.salary").cast(IntegerType)as("salary")
    )
    userDF.printSchema()
    userDF.show(10, truncate = false)
  }
}
