package cn.fei.scala.Doo4.actor

import scala.actors.Actor

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/16 0016 15:09
 */
case class HelloMessage(msg: String)

case class ReplyMessage(reply: String)


class TeacherActor extends Actor {
  //一直接收消息
  override def act(): Unit = {
    loop {
      react {
        case "hello" =>
          println("这都不会，为师没有你这个徒弟")
          //返回信息
          sender ! ""
      }
    }
  }
}

class StudentActor(teacherActor: TeacherActor) extends Actor {

  override def act(): Unit = {
    //发信息给老师
    teacherActor ! "hello"

    //接受消息
    react {
      case "hello" => println("")
    }
  }
}


object RpcActor {
  def main(args: Array[String]): Unit = {
    val teacherActor = new TeacherActor
    teacherActor.start()
    val zsstudentActor = new StudentActor(teacherActor)
    zsstudentActor.start()
  }
}
