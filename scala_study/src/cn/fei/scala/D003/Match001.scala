package cn.fei.scala.D003

import scala.io.StdIn

/**
 * @description:匹配值
 * @author: 飞
 * @date: 2020/11/15 0015 14:54
 */
object Match001 {
  def main(args: Array[String]): Unit = {
    while (true){
      val input =StdIn.readLine()
      input match {
        case "hadoop" => println("4")
        case "zoopeeper" => println("3")
        case "spark" => println("2")
        case _ => println("1")
      }
    }
  }

}
