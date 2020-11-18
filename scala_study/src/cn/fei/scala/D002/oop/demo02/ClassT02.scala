package cn.fei.scala.D002.oop.demo02

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/13 0013 16:28
 */
class Customer {
   var name: String = _
   var age: Int = _

  def printHello(msg: String) = println(msg)
  //定义私有变量
  def setName(name:String)=this.name=name


  override def toString: String = s"name:$name,age:$age"
}

object ClassT02 {
  def main(args: Array[String]): Unit = {
    val customer = new Customer
    customer.age = 14
    customer.name = "hiehei"
    customer.printHello("hello")
    println(customer)
  }

}
