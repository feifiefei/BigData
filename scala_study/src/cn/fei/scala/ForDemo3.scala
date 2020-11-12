package cn.fei.scala

import scala.collection.immutable

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/12 0012 15:12
 */
object ForDemo3 {
  def main(args: Array[String]): Unit = {
    var i: Int = 0
    while (i <= 10) {
      print(i + " ")
      i += 1
    }
    do {
      print(i + " ")
      i += 1
    } while (i <= 10
    )
  }

}
