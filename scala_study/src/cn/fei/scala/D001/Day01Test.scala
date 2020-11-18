package cn.fei.scala.D001

import scala.collection.mutable.{ArrayBuffer, ListBuffer, Map}

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/12 0012 18:13
 */
object Day01Test {
  def main(args: Array[String]): Unit = {
    //1
    print("hello world!")
    //3
    for (i <- 1 to 10 if i % 2 == 0) println(i)
    //4
    println(s"《《《《《${product(3, 4)}")
    //5定义一个函数，入参是2个整数，函数体中计算返回两数之差的绝对值
    val absoluteValue = (x: Int, y: Int) => (x - y).abs
    println(absoluteValue(3, 4))
    //6
    val arrayBuffer = ArrayBuffer(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println(arrayBuffer.sum / arrayBuffer.length)
    //7
    val name = "Fei"
    val age = 23
    val sex = "nan"
    val bodyWeight = 66
    val high = 1.76
    val t1 = (name, age, sex)
    val t2 = name -> bodyWeight
    val t3 = (name, high)
    val BMI: Double = t2._2 / (t3._2 * t3._2)
    println(s"BMI:$BMI")
    //8
    val map = Map("Fei" -> 23, "Tom" -> 20)
    map += "jack"->30
    println(map)
    println(s"Fei:age=${map("Fei")}")
    val l1 = ListBuffer(2, 4, 6)
    val l2 = List(1, 3, 5)
    l1 ++= l2
    //    for (i <- l1) print(s"+++++$i++++++++")
    l1.foreach((x: Int) => println(x))
    //用map方法返回每个元素的平方组成新列表
    println(l1.map(x => x * x).toString())
    //用filter过滤出l1中的奇数
    println(l1.filter(_ % 2 == 1).toString())
    //对l1按降序排序
    println(l1.sorted.reverse.toString())
    //用reduce求l1所有元素的乘积
    println(l1.reduce(_ * _))
  }

  def product(x: Int, y: Int) = x * y
}
