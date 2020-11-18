package cn.fei.scala.D002

import scala.collection.mutable._

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/13 0013 10:22
 */
object LIstT {
  def main(args: Array[String]): Unit = {
    var list1 = List(4 :: 5 :: 6 :: Nil)
    val list2 = (1 to 3).toList
    //判断是否为空
    list2.isEmpty
    //反转
    list1.reverse
    //获取前两个
    list2.take(2)
    //获取后两个
    list2.takeRight(2)
    //删除前两个
    list2.drop(2)
    //删除后两个
    list2.dropRight(2)
    //扁平化 将集合内的集合展开
    val l1 = List(list1, list2)
    l1.flatten
    //拉链 拉开
    val ll1: List[Int] = List(1, 2, 3, 4)
    val ll2: List[String] = List("a", "b", "C", "4")
    val ll3 = ll1.zip(ll2)
    ll3.unzip


  }
}
