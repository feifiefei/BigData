package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 16:28
 */
object ExceptionD001 {
  def main(args: Array[String]): Unit = {
    //异常的典型案例：两个数相除
    var result = 0
    try {
      result = 10 / "0".toInt
    } catch {
      case e1: Exception => e1.printStackTrace()
      case e3: ArithmeticException => e3.printStackTrace()
      //      case e2: RuntimeException => e2.printStackTrace()
    } finally {
      println("finally")
    }
  }

}
