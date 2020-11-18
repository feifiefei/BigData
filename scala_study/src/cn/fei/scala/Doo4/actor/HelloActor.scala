package cn.fei.scala.Doo4.actor

import scala.actors.Actor

/**
 * @description:Actor、多线程
 * @author: 飞
 * @date: 2020/11/16 0016 14:57
 */
class wxActor extends Actor {
  override def act(): Unit = {
    //一直接受信息
    loop {
      react {
        case "hello" => println("阿巴阿巴阿巴……………………")
      }
    }
    /*//只接受一次信息 性能差

    receive{
      case "hello" => println("阿巴阿巴阿巴……………………")
    }*/
  }
}


object HelloActor {
  def main(args: Array[String]): Unit = {
    val actor = new wxActor()
    actor.start()
    actor ! "hello"
  }
}
