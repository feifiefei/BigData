package cn.fei.scala

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/12 0012 16:19
 */
object MethodDemo01 {
  //定义返回两数最大值的方法
  def max(x: Int, y: Int): Int = {
    if (x > y) x else y
  }
  def max2(x: Int, y: Int)= if (x > y) x else y


  def main(args: Array[String]): Unit = {
    val a = 5
    val b = 6
    val i = max(a, b)
    print(i)
  }
}
