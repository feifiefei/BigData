package cn.fei.scala

import scala.collection.immutable
import scala.util.control.Breaks._

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/12 0012 15:12
 */
object BreakDemo01 {
  def main(args: Array[String]): Unit = {
    //当 item 大于5时，调出循环
    breakable {
      for (item <- 1 to 100) {
        if (item > 50) break()
        print(item + " ")
      }
    }
  }

}
