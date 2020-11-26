package com.fei.wct

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:scala开发spark词频统计
 * @author: 飞
 * @date: 2020/11/18 0018 20:38
 */
object WordcountT {
  def main(args: Array[String]): Unit = {
    //构建spark环境
    val conf = new SparkConf()
      .setMaster("local[2]")
      .setAppName("SparkWordCount")
    val sc = new SparkContext(conf)
    //读取数据
    val inputPDD: RDD[String] = sc.textFile("/datas/wordcount.data")
    //数据处理
    var outputPDD: RDD[(String, Int)] = inputPDD.flatMap(_.split("\\s+"))
      .map(word => (word, 1))
      .reduceByKey((x, y) => x + y)
    outputPDD.foreach(println(_))
    //写入文件
    inputPDD.saveAsTextFile(s"/datas/output-${System.currentTimeMillis()}")
  }
}
