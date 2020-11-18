package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 16:57
 */

class Studens(val name: String, val age: Int)

object Studens {
  def apply(name: String, age: Int): Studens = new Studens(name, age)

  def unapply(arg: Studens): Option[(String, Int)] = {
    if (arg != null) {
      Some(arg.name -> arg.age)
    }
    else {
      None
    }
  }
}


object ApplyD001 {
  def main(args: Array[String]): Unit = {
    val studens = Studens("zhangsan", 15)

  }

}
