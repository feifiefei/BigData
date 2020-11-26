package com.fei.wct

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/19 0019 19:06
 */
object WCT003 {
  def main(args: Array[String]): Unit = {
    //构建spark运行环境
    val sc: SparkContext = {
      val sparkConf: SparkConf = new SparkConf()
        .setMaster("local[2]")
        .setAppName(this.getClass.getSimpleName.stripSuffix("$"))
      SparkContext.getOrCreate(sparkConf)
    }
    //加载小批量数据
    val inputRDD: RDD[(String, String)] = sc.wholeTextFiles("datas/ratings100")
    //数据处理
    inputRDD.flatMap {
      case (x, y) => y.split("\\n")
    }.foreach(x => print(x.length))
    //    Thread.sleep(200000000)
    sc.stop()
  }
}
