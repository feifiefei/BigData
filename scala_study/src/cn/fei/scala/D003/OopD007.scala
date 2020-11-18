package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 11:53
 */
trait Logger {
  //具体字段
  val logClazz: String = this.getClass.getSimpleName
  //抽象字段
  val logName: String

  def logging()

  def printLog(): Unit = {
    println("特质中的方法")
  }
}

class ConsoleLogger extends Logger {
  override val logName: String = "Consle"

  override def logging(): Unit = println("日志打印控制台")

}

class FLogger extends Logger {
  override def logging(): Unit = println("日志写入文件")

  override def printLog(): Unit = println("重写文件日志")

  override val logName: String = classOf[FLogger].getSimpleName
}

trait Monitor {
  def printMetrics()
}

class SparkApp extends ConsoleLogger with Monitor {
  override def printMetrics(): Unit = ???
}

object OopD007 {
  def main(args: Array[String]): Unit = {
    val logger = new ConsoleLogger
    val logger1 = new FLogger
    logger.logging()
    logger.printLog()
    logger1.logging()
    logger1.printLog()
    println(logger.logName, logger.logClazz)
  }

}
