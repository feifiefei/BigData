package com.fei.wct

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/20 0020 19:42
 */
object Funtic {
  def main(args: Array[String]): Unit = {
    //获取执行环境
    val sc = {
      val conf = new SparkConf()
        .setMaster("local[2]")
        .setAppName("func1")
      SparkContext.getOrCreate(conf)
    }
    //map
    val rdd1 = sc.parallelize(List(1, 2, 2, 7, 3, 8, 2, 9, 1, 10))
    //    rdd1.map(_ * 2).foreach(print(_))
    //filter
    //    rdd1.filter(_ >= 10).foreach(print(_))
    //flatmap
    val rdd2 = sc.parallelize(Array("a b c", "d e f", "h i j"))
    //    rdd2.flatMap(_.split("\\s+")).foreach(println(_))
    //交集、并集、差集、笛卡尔积
    val rdd3 = sc.parallelize(List(1, 2, 3, 4))
    val rdd4 = sc.parallelize(List(4, 6, 7, 8))
    //union不会去重
    //    rdd3.union(rdd4).foreach(println(_))
    //去重distinct
    //    rdd3.union(rdd4).distinct()
    //求交集intersection
    //    rdd3.intersection(rdd4).foreach(println(_))
    //求差集
    //    rdd3.subtract(rdd4).foreach(println(_))
    //笛卡尔积
    val name = sc.parallelize(List("Tom", "Maria")) //学生
    val score = sc.parallelize(List("java", "scala", "python"))
    //    name.cartesian(score).foreach(println(_))
    //去重 distinct
    //    rdd1.distinct().foreach(println(_))
    //first、take、top函数
    //    rdd3.take(2).foreach(println(_)) //获取前n位
    //    println(rdd3.first())//获取第一个
    //    rdd3.top(2).foreach(println(_)) //获取最大的前n个
    //keys、values函数，针对RDD中数据类型为KeyValue对时，获取所有key和value的值
    val add = sc.parallelize(List("dog", "pig", "lion", "cat", "eagle"))
    val add2 = add.map(x => (x.length, x))
    //    add2.keys.foreach(print(_))
    //    add2.values.foreach(print(_))
    //collectAsMap函数
    // 当RDD中数据类型为Key/Value对时，转换为Map集合
    val acc = sc.parallelize(List(("a", 1), ("b", 2)))
    //    acc.collectAsMap().foreach(println(_))
    //mapPartitionSWithIndex函数
    //取分区中对应的数据时，还可以将分区的编号取出来
    val aee = sc.parallelize((1 to 10).toList, 3)
    val aee1 = (index: Int, iter: Iterator[Int]) => {
      iter.map(x => s"parID:${index},val:${x}")
    }
    aee.mapPartitionsWithIndex(aee1).foreach(println(_))


  }

}
