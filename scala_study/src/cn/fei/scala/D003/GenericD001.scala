package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 17:09
 */
class Tuple[T](val _1: T, val _2: T) {
  override def toString: String = s"${_1},${_2}"
}


object GenericD001 {

  //上界
  def getMiddle[T](array: Array[T]): T = {
    array(array.length / 2)
  }

  //下界
  def getMiddle1[T >: Null](array: Array[T]): T = {
    array(array.length / 2)
  }


  def main(args: Array[String]): Unit = {
    println(getMiddle(Array(1, 2, 3, 4, 5, 6)))
    println(getMiddle(Array("a", "b", "c")))
    val tuple = new Tuple[String]("A", "b")
    println(tuple)
    val value = new Tuple[Int](1, 2)
    println(value)
  }
}
