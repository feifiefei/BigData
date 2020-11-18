package cn.fei.scala.Doo4.Func

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/16 0016 10:08
 */
object FuncD001 {
  //基本定义方法
  def addValue(x: Int, y: Int) = {
    x + y
  }

  def plusValue(x: Int)(y: Int) = {
    x + y
  }

  def operate(x: Int, y: Int, func: (Int, Int) => Int): Int = {
    func(x, y)
  }

  def operate2(x: Int, y: Int)(func: (Int, Int) => Int) = {
    func(x, y)
  }

  def main(args: Array[String]): Unit = {
    val list = (1 to 10).toList
    println(addValue(1, 99))
    println(plusValue(1)(99))
    operate2(1, 99) ((x, y) => x + y)
  }

}
