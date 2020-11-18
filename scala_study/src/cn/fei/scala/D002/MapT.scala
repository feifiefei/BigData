package cn.fei.scala.D002

import scala.collection._

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/13 0013 11:09
 */
object MapT {
  def main(args: Array[String]): Unit = {
    //不可变
    val map1 = Map(1 -> "kangkang", 2 -> "jack")
    val map2 = Map((1, "kanngshifu"), (2, "javk"))
    //可变
    val mutablemap = mutable.Map[Int, String](1 -> "kangkang")
    //添加数据
    mutablemap.+=(3 -> "marra")
    //获取值,如果没有返回null
    map1.getOrElse(4, "null")
    //获取所有Key
    map1.keys
    //获取所有的value
    map1.values
    //遍历
    for (item <- map1) print(item)
    for ((x,y) <- map1) print(x,y)
  }

}
