package cn.fei.convert

import cn.fei.bean.MovieRating
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{DoubleType, LongType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
 * @description:自定义schema
 * @author: 飞
 * @date: 2020/11/22 0022 19:16
 */
object SparkRDDSchema {
  def main(args: Array[String]): Unit = {
    //构建执行环境
    val spark: SparkSession = SparkSession.builder()
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      .master("local[2]")
      .getOrCreate()
    import spark.implicits._
    //加载数据源
    //    val inputDS: Dataset[String] = spark.read.textFile("datas/resources/u.data")
    val inputRDD: RDD[String] = spark.sparkContext.textFile("datas/resources/u.data", minPartitions = 2)
    //数据处理
    val valueRDD: RDD[Row] = inputRDD.mapPartitions { iter =>
      iter.map { line =>
        val Array(userId, itemId, rating, timestamp) = line.trim.split("\\s+")
        Row(userId, itemId, rating.toDouble, timestamp.toLong)
      }
    }
    val value2RDD: RDD[MovieRating] = inputRDD.mapPartitions { iter =>
      iter.map { line =>
        val Array(userId, itemId, rating, timestamp) = line.trim.split("\\s+")
        MovieRating(userId, itemId, rating.toDouble, timestamp.toLong)
      }
    }
    //scema定义
    val schemaType: StructType = StructType(
      Array(
        StructField("user_id", StringType, nullable = false),
        StructField("itemId", StringType, nullable = false),
        StructField("rating", DoubleType, nullable = false),
        StructField("timestamp", LongType, nullable = false)
      )
    )
    //应用函数createDateFtame
    val vaDF: DataFrame = spark.createDataFrame(valueRDD, schemaType)
    val value: Dataset[MovieRating] = value2RDD.toDS()
    val value1: Dataset[MovieRating] = vaDF.as[MovieRating]
    vaDF.show(10, truncate = false)
    spark.stop()
  }
}
