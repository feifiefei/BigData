package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 12:12
 */


//case 编译时自动生成apply方法，实现所有变量的setter和getter方法，重写equals、hashCode、toString方法
case class Perss(name: String, var age: Int)


object OopD008 {
  def main(args: Array[String]): Unit = {
    val p = new Perss("hehe", 14)
    println(p)
  //拷贝一个独享
  val p2 = p.copy(age = 18)
    println(p2)
  }

}
