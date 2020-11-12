package cn.fei.scala

import scala.collection.mutable.ArrayBuffer

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/12 0012 17:23
 */
object ArrayDemo01 {
  def main(args: Array[String]): Unit = {
    //定义定长数组方式一：
    val array: Array[Int] = new Array[Int](5)
    array(0) = 1
    array(1) = 2
    array(2) = 3
    array(3) = 4
    array(4) = 5
    //定义定长数组方式二
    val array2: Array[String] = Array(
      "a", "b", "c"
    )
    //定义变长数组方式一
    val arrayBuffer = ArrayBuffer[Int]()
    arrayBuffer += 1
    arrayBuffer += 2
    arrayBuffer += 3
    arrayBuffer += 4
    arrayBuffer += 5
    //定义变长数组方式二
    val arrayBuffer1 = ArrayBuffer(
      "a",
      "b",
      "c"
    )
    println(s"sum= ${array.sum}")
      }
}
