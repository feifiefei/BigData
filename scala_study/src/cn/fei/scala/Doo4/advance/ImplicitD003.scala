package cn.fei.scala.Doo4.advance

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/16 0016 11:15
 */


object ImplicitD003 {


  def main(args: Array[String]): Unit = {
    val ints = List(44, -55, 88, 99, 77, 44, 33)
    println(ints.sorted)
    println(ints.sorted[Int](Ordering.by(item=>(-item))))
  }
}
