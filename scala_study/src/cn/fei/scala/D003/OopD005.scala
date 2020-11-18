package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 11:16
 */
abstract class Shape {
  def area(): Double
}

class Square(val edge: Double) extends Shape {
  override def area(): Double = edge * edge
}

class Rectangle(val edge: Double, val length: Double) extends Shape {
  override def area(): Double = edge * length
}

class Circle(val radius: Double) extends Shape {
  override def area(): Double = Math.PI * radius * radius
}

class OopD005 {
  def main(args: Array[String]): Unit = {

  }

}
