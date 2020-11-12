package cn.fei.scala

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/12 0012 15:12
 */
object ForDemo1 {
  def main(args: Array[String]): Unit = {
    for (j <- 1 to 3; i <- 1 to 5) {
      print("*")
      if (i == 5) println()
    }
  }

}
