package cn.fei.scala.D001

import scala.collection.parallel.immutable

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/12 0012 15:12
 */
object ForDemo2 {
  def main(args: Array[String]): Unit = {
    //只打印奇数
    for (item <- 1 to 10 if item % 2 == 1) {
      println(s"item = ${item}")
    }
    //将1到10进行平方打印
    //for的推导式
    val xx= for (item <- 1 to 10) yield {
      item * item
    }
    print(xx)
  }

}
