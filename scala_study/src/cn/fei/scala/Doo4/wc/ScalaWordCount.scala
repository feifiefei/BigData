package cn.fei.scala.Doo4.wc

import scala.collection._

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/16 0016 10:24
 */
object ScalaWordCount {
  def main(args: Array[String]): Unit = {
    val list = List("spark flink spark", "hive spark flink spark")
    println(list
      .flatMap(_.trim.split("\\s+"))
      .map(x => (x, 1))
      .groupBy(x => x._1)
      .map { case
        (word, values) =>
        val count = values.map(_._2).sum
        (word, count)
      }
    )


  }
}
