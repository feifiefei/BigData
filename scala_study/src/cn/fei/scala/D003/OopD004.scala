package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 11:08
 */
class Person003

class Student005 extends Person003

object OopD004 {
  def main(args: Array[String]): Unit = {
    var stu: Person003 = new Student005
    println(stu.isInstanceOf[Person003])
    val s = stu.asInstanceOf[Student005]
    println(s)
    println(stu.getClass == classOf[Person003])
    println(stu.getClass == classOf[Student005])
  }

}
