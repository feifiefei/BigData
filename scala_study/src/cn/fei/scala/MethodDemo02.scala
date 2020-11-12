package cn.fei.scala

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/12 0012 16:26
 */
object MethodDemo02 {
  def max(x: Int, y: Int): Int = {
    if (x > y) x else y
  }

  def max2(x: Int = 0, y: Int = 1): Int = {
    if (x > y) x else y
  }

  def sum(num: Int*) = num.sum
  def main(args: Array[String]): Unit = {
    print(sum(1,2,3,4,5,6,7))
  }
}
