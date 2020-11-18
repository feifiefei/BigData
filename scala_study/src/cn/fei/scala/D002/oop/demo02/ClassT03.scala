package cn.fei.scala.D002.oop.demo02

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/13 0013 17:00
 */
/**
 * 在scala语言中，构造方法分为两种：
 * 1.主构造方法
 * 紧跟类名称之后，使用一队圆括号
 * 2.附属构造方法
 *
 */
class Person(val name: String, var age: Int) {
  var gender: String = _

  //定义附属构造器
  def this(name: String, age: Int, gender: String) {
    //调用主构造方法
    this(name, age)
    this.gender = gender
  }

  override def toString: String = s"name=$name,age=$age"
}

object ClassT03 {
  def main(args: Array[String]): Unit = {
    val person = new Person("new", 14)
  }

}
