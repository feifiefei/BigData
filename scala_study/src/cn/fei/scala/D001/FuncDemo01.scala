package cn.fei.scala.D001

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/12 0012 17:05
 */
class FuncDemo01 {
  def max(x: Int, y: Int) = {
    if (x > y) x else y
  }

  val maxFunc = (x: Int, y: Int) => if (x > y) x else y
  val maxFunc2: (Int, Int) => Int = max _
}
