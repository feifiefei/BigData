package cn.fei.process

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.storage.StorageLevel

/**
 * @description:分析电影评分前十强类型的
 * @author: 飞
 * @date: 2020/11/22 0022 11:44
 */
object SparkTop10M0vie {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
      .master("local[3]")
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      //设置sparksql在shuffle阶段的分区数据，实际项目根据数据量调整
      .config("spark.sql.shuffle.partitions", 4)
      .getOrCreate()
    import spark.implicits._
    //获取数据
    val ratingRDD: RDD[String] = spark.sparkContext.textFile("datas/ratings.dat", minPartitions = 2)
    //解析数据
    //构建元组对象
    val resultDF: DataFrame = ratingRDD
      //过滤数据
      .filter(iter => null != iter && iter.trim.split("::").length == 4)
      .mapPartitions { iter =>
        iter.map { line =>
          val Array(userId, movieId, rating, timestamp) = line.trim.split("::")
          //构建元组对象
          (userId, movieId, rating, timestamp)
        }
      }
      .toDF("user_id", "movie_id", "rating", "timestamp")
    //    resultDF.printSchema()
    //    resultDF.show(10, truncate = false)
    //基于sql分析
    resultDF.createOrReplaceTempView("tb_view")
    val dataFrame = spark.sql(
      """
        |SELECT
        |movie_id,
        |ROUND(AVG(rating),2) as avg,
        |COUNT(movie_id) AS count_rating
        |FROM
        | tb_view
        |GROUP BY
        | movie_id
        |HAVING
        | count_rating > 2000
        |ORDER BY
        | avg DESC
        |LIMIT 10
        |""".stripMargin)
//        dataFrame.show(10, truncate = false)
    println("==============================")
    //使用DSL方式来分析数据
    import org.apache.spark.sql.functions._
    val value = resultDF
      //分组
      .groupBy($"movie_id")
      .agg(
        round(avg($"rating"), 2).as("avg_rating"),
        count($"movie_id").as("Count_rating")
      )
      .filter($"count_rating" > 2000)
      .orderBy($"avg_rating".desc)
      .limit(10)
//        value.show(10, truncate = false)
    //数据保存
    //数据缓存(看下规则）
    value.persist(StorageLevel.MEMORY_AND_DISK)
    //保存数据至mysql
    value
      //降低分区
      .coalesce(1)
      .write
      .mode(SaveMode.Overwrite)
      .format("jdbc")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("user", "root")
      .option("password", "123456")
      .option("url", "jdbc:mysql://node1.itcast.cn:3306/?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true")
      .option("dbtable", "db_test.tb_top10_movies")
      .save()
    //保存至CSV文件
    value.coalesce(1)
      .write
      //保存策略
      .mode(SaveMode.Overwrite)
      .csv("sparkdata/Top10.csv")
    //转化成RDD
    //    resultDF.rdd->row类型
    value.unpersist()
    spark.stop()

  }
}
