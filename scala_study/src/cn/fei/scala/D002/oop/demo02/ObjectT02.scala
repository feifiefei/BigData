package cn.fei.scala.D002.oop.demo02

import java.text.SimpleDateFormat
import java.util.Date

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/13 0013 17:23
 */
object DataUtils {
  //定义日期格式化对象
  private val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

  //定义一个日期格式化操作
  def format(timeStamp: Long): String = {
    format.format(new Date(timeStamp))
  }
}

object ObjectT02 {

  def main(args: Array[String]): Unit = {
    println(DataUtils.format(1605259884749L))
  }
}
