package cn.fei.scala.D003

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/15 0015 15:25
 */
object Math005 {
  def main(args: Array[String]): Unit = {
    val array = (1 to 10).toArray
    val Array(_, x, y, z, _*) = array
    println(s"x=$x,y=$y,z=$z")
    val info = "1001,zhangsan,34"
    val Array(id, name, age) = info.split(",")
    print(id, name, age)
    var list = (1 to 10).toList
    var a1 :: b1 :: tail = list
    print(a1,b1)


  }


}
