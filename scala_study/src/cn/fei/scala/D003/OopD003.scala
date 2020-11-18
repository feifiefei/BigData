package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 10:42
 */

class Person001 {
  val name: String = "none"

  def getName(): String = this.name

  def sayHello() = println("Hello")
}

class Student1 extends Person001 {
  override val name: String = "heihie"

  override def getName(): String = {
    super.sayHello()
    s"subclass,${this.name}"
  }
}


object OopD003 {
  def main(args: Array[String]): Unit = {
    val student = new Student1
    student.getName()
  }

}
