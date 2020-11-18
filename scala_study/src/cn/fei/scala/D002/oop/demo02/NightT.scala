package cn.fei.scala.D002.oop.demo02
/**
 * @description:
 * @author: 飞
 * @date: 2020/11/13 0013 19:26
 */
trait Animal {
  def say(): Unit

}

object NightT {
  def main(args: Array[String]): Unit = {
    val hen =
      new Hen("老六")
    val dog = Dog1("张三")
    hen.say()
    dog.say()
  }

}

class Hen(var name: String) extends Animal {
  override def say(): Unit = println(s"$name 是一只鸡")
}



class Dog1(val name: String) extends Animal {
  override def say(): Unit = println(s"${name}是一只猪")
}



object Dog1{
  def apply(name: String) = new Dog1(name)
}