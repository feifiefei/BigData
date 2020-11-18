package cn.fei.scala.D002.oop.demo02

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/13 0013 17:35
 */
class Person1(name: String, age: Int) {
  var gender: String = _

  def say = println("我要学好scala")

  override def toString: String = s"name=$name,age=$age"
}

object MainT01 extends App {
  private val fei = new Person("Fei", 23)
  println(fei)

}
