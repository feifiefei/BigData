package cn.fei.scala.D001

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/11 0011 16:21
 */
object ScalaHello {
  def main(args: Array[String]): Unit = {
    //    val name = "zhangsan"
    //    val age = "23"
    //    val sex = "男"
    //    val info = s"name = ${name},age=${age},sex=${sex}"
    //        val sql =
    //    """
    //      |hello
    //      |world
    //      |ni
    //      |afsd
    //      |""".stripMargin
    //          print(sql)
    //    for (i <- 1 to 3) {
    //      for(k <- 1 to 5){
    //        print("*")
    //      }
    //      println()
    //    }
    //    for (i <- 1 to 3; k <- 1 to 5) {
    //      print("*"); if (k == 5) {
    //        println()
    //      }
    //    }
    //    val name: String = "laowanba"
    //    print(s"$name")
    val sex = "male"
    val result = if (sex == "male") 1 else 0
    print(result)
  }
}
