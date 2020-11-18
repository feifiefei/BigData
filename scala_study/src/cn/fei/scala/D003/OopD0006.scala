package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 11:33
 */

abstract class Perx(val name: String) {
  def sayHello()
}


object OopD0006 {
  def main(args: Array[String]): Unit = {
    val nadf = new Perx("nadf") {
      override def sayHello(): Unit = print(s"小八加油$name")
    }
    nadf.sayHello()
  }

}
