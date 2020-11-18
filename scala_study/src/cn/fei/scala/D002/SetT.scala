package cn.fei.scala.D002

import scala.collection.mutable.Set

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/13 0013 11:05
 */
object SetT {
  //Set集合没有重复元素无序
  val set1 = Set[Int]()
  val set2 = Set(1, 2, 3, 4, 5, 1, 2)
  //获取大小
  set2.size
  //遍历
  for (iter <- set2) print(iter)
  //删除元素
  set2 - 1

}
