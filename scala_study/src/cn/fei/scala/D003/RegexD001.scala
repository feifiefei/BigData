package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 16:19
 */
object RegexD001 {
  def main(args: Array[String]): Unit = {
    //定义正则表达式
    val regex = """.+@.+\..+""".r
    val email = "123456@qq.com"
    val mat = regex.findAllMatchIn(email)
    println(mat)
  }

}
