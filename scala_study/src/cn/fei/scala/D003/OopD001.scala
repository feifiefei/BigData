package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 10:21
 */
class Person(val name: String, val age: Int = 0) {
  def buyGoods() = {
    println(Person.money)
  }

}

object Person {
  private var money: Double = 8934.87

  def apply(name: String, age: Int): Person = new Person(name, age)
}

object OopD001 {
  def main(args: Array[String]): Unit = {
    val person =Person("zhangsan", age = 32)
    person.buyGoods()
  }

}
