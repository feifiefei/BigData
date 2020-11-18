package cn.fei.scala.D003

/**
 * @description:匹配类型
 * @author: 飞
 * @date: 2020/11/15 0015 15:01
 */
object Match002 {
  def typeFunc(value: Any) = {
    value match {
      case intValue: Int => println(s"Int Type:$intValue")
      case sss: String => println(s"Int Type:${sss}")
      case double: Double => println(s"Int Type:$double")
      case _ => println("I")
    }

  }

  def main(args: Array[String]): Unit = {
    typeFunc(1)
    typeFunc("a")
    typeFunc(1.14)

  }
}
