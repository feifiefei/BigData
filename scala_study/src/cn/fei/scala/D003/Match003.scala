package cn.fei.scala.D003

import scala.io.StdIn

/**
 * @description:匹配守卫条件
 * @author: 飞
 * @date: 2020/11/15 0015 15:07
 */
object Match003 {
  def main(args: Array[String]): Unit = {
    while (true){
      val redVal:Int =StdIn.readInt()
      redVal match{
        case number if number >=0&& number <=3=>println("[0-3]")
        case _=>println("cuocuosdua")
      }
    }
  }

}
