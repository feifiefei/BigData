package cn.fei.scala.D003

import cn.fei.scala.D003.T001.list

import scala.collection._

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/14 0014 21:01
 */
object T001 extends App {
  val list = List("spark flink spark", "hive spark flink spark")

  def wordCount(arr: List[String]): Unit = {
    println(arr.flatMap(_.split("\\s+"))
      .groupBy(x => x)
      .map {
        case (key, value) => (key, value.map {
          case x => 1
        }.reduce((x, y) => x + y))
      }
    )
  }

  wordCount(list)
}


