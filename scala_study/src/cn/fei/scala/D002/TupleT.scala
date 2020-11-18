package cn.fei.scala.D002

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/13 0013 10:05
 */
object TupleT {
  def main(args: Array[String]): Unit = {
    val tuple2: (Int, String) = (1001, "zhangsan")
    val t2: (Int, String) = (1001 -> "zhangsan")
    //二元组swap方法可将数据位置互换
    println(t2.swap)
  }

}
