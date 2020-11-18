package cn.fei.scala.Doo4.advance

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/16 0016 11:15
 */
class Man(var name: String)

class SupMan(name: String) {
  def emitLaser() = {
    println(s"我${name}没有开挂！！迪迦……………………")
  }

}

object Utils {
  implicit def man2SuperMan(man: Man): SupMan = {
    new SupMan(man.name)
  }
}


object ImplicitD001 {


  def main(args: Array[String]): Unit = {
    val man = new Man("大古")
    import cn.fei.scala.Doo4.advance.Utils._
    man.emitLaser()
  }
}
