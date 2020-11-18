package cn.fei.scala.D003

/**
 * @description:匹配样例类
 * @author: 飞
 * @date: 2020/11/15 0015 15:11
 */
case class Customer(val name: String, var age: Int)

case class Order(id: Long, price: Double)


object Match004 {
  def caseFunc(obj: Any) = {
    obj match {
      case Customer(name, age) => println(s"$name,$age")
      case Order(id, price) => println(s"$id,$price")
      case (x, y, _) => println(s"$x,$y")
      case (x, y, _) => println(s"$x,$y")
      case ("spark", y) => println(s"spark,$y")
    }
  }

  def main(args: Array[String]): Unit = {
    val customer: Any = Customer("张三", 28)
    val order: Any = Order(1001L, 99.99)
    //使用模式匹配，匹配样例类
    caseFunc(customer)
    caseFunc(order)
    caseFunc("a","b","C")
    caseFunc("spark",1231)
  }
}
