package com.fei.wct

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:用scala开发spark词频统计并排名
 * @author: 飞
 * @date: 2020/11/18 0018 20:59
 */
object WordCountTWO {
  def main(args: Array[String]): Unit = {
    //构建spark连接
    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("wordcount2")
    val sc: SparkContext = new SparkContext(conf)
    //读取文件
    val inputRDD: RDD[String] = sc.textFile("/datas/wordcount.data")
    //数据处理
    val outputRDD: RDD[(String, Int)] = inputRDD.flatMap(_.split("\\s+"))
      .map(word => (word, 1))
      .reduceByKey((x, y) => x + y)
    outputRDD.foreach(x => println(x))
    //将无序的数据排序
    //def sortByKey(ascending: Boolean = true, numPartitions: Int = self.partitions.length)
    //      : RDD[(K, V)] = self.withScope
    //    outputRDD.sortByKey(ascending = false)
  }
}
