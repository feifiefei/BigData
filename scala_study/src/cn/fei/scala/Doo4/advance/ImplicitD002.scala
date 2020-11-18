package cn.fei.scala.Doo4.advance

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/16 0016 11:57
 */

class SingPen {
  def write(): Unit = {
    println("签到……………………………………")
  }
}


object ImplicitD002 {
  implicit val singPen = new SingPen()

  def singnForMeetup( implicit pen: SingPen) = {
    pen.write()
  }


  def main(args: Array[String]): Unit = {
    singnForMeetup

    singnForMeetup(new SingPen())

  }

}
