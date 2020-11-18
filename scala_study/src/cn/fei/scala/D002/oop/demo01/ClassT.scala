package cn.fei.scala.D002.oop.demo01
import scala.collection._
/**
 * @description:
 * @author: 飞
 * @date: 2020/11/13 0013 16:20
 */

class Person {
  var name: String = _
  var age: Int = _
}


object ClassT {
  def main(args: Array[String]): Unit = {
    val person = new Person
    person.name = "heima"
    person.age = 14

    println(person.age, person.name)
    var tom: Array[Int] = Array[Int](1,2,3,4)
    tom(3)=5
    println(tom.mkString(","))
    tom.foreach(x=>println(x))
  }
}
