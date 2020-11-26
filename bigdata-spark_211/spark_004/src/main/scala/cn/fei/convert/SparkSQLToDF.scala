package cn.fei.convert

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/22 0022 19:42
 */
object SparkSQLToDF {
  def main(args: Array[String]): Unit = {
    //构建连接
    val spark: SparkSession = SparkSession.builder()
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      .master("local[3]")
      .getOrCreate()
    import spark.implicits._
    // TODO: 1、构建RDD，数据类型为三元组形式
    val usersRDD: RDD[(Int, String, Int)] = spark.sparkContext.parallelize(
      Seq(
        (10001, "zhangsan", 23),
        (10002, "lisi", 22),
        (10003, "wangwu", 23),
        (10004, "zhaoliu", 24)
      )
    )
    //将RDD转化为DataFrame
    val usDF: DataFrame = usersRDD.toDF("id", "name", "age")
    usDF.printSchema()
    usDF.show(10, truncate = false)
    println("==========================================================================")
    val swDF: DataFrame = Seq(
      (10001, "zhangsan", 23),
      (10002, "lisi", 22),
      (10003, "wangwu", 23),
      (10004, "zhaoliu", 24)
    ).toDF("id", "name", "age")
    swDF.printSchema()
    swDF.show(10, truncate = false)
    spark.stop()
  }

}
