package cn.fei.convert

import cn.fei.bean.MovieRating
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
 * @description:反射方式将RDD转化为DataFrame
 * @author: 飞
 * @date: 2020/11/22 0022 10:57
 */
object SparkRDDInferring {
  def main(args: Array[String]): Unit = {
    //构建连接
    val spark: SparkSession = SparkSession.builder()
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      .master("local[3]")
      .getOrCreate()
    import spark.implicits._
    //加载数据
    val rawRatingsRDD: RDD[String] = spark.sparkContext.textFile("datas/resources/u.data")
    //封装数据
    val rawRDD: RDD[MovieRating] = rawRatingsRDD.mapPartitions { iter =>
      iter.map { line =>
        val Array(userId, iterId, rating, timestamp) = line.trim.split("\\t")
        //返回MovieRating实例化对象
        MovieRating(userId, iterId, rating.toDouble, timestamp.toLong)
      }
    }
    val dataFrame = rawRDD.toDF()

    dataFrame show(10, truncate = false)
    dataFrame printSchema()
    spark.stop()
  }
}
