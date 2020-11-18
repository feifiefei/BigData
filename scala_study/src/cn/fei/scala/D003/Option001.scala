package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 15:53
 */
object Option001 {
  def div(a: Int, b: Int): Option[Double] = {
    if (b != 0) {
      val result = a / b.toDouble
      Some(result)
    } else {
      None
    }
  }

  def main(args: Array[String]): Unit = {
    val result = div(15, 0)
    //    print(s"result=${result.get}")
    result match {
      case Some(x) => println(s"result=$x")
      case None => println("none")
    }
    val map = Map("a" -> 1, "b" -> 2)
    map.getOrElse("a", "none")
  }
}
