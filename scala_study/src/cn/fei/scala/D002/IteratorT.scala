package cn.fei.scala.D002

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/13 0013 11:25
 */
object IteratorT {
  def main(args: Array[String]): Unit = {
    var list = List(1, 2, 3, 4, 5)
    //从list中获取迭代器
    val iter = list.iterator
    //使用while循环来迭代元素
    while (iter.hasNext) {
      println(iter.next())
    }
  }

}
