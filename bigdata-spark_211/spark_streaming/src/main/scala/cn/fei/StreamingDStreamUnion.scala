package cn.fei

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @description:从TCP Socket 中读取数据，对每批次（时间为5秒）数据进行词频统计，将统计结果输出到控制台。
 *                   TODO: 从多个Socket读取流式数据，进行union合并
 * @author: 飞
 * @date: 2020/11/25 0025 19:51
 */
object StreamingDStreamUnion {
  def main(args: Array[String]): Unit = {
    //构建StreamingContext流式上下文实例对象
    val ssc: StreamingContext = {
      val sparkConf = new SparkConf()
        .setMaster("local[3]")
        .setAppName(this.getClass.getSimpleName.stripSuffix("$"))
      sparkConf
      new StreamingContext(sparkConf, Seconds(5))
    }
    //加载socket流,同时设置缓存等级
    val inputDStream01: DStream[String] = ssc.socketTextStream("node1.itcast.cn", 9999,storageLevel = StorageLevel.MEMORY_AND_DISK)
    val inputDStream02: DStream[String] = ssc.socketTextStream("node1.itcast.cn", 9988)
    //合并两个DStream流
    val inputDStream: DStream[String] = inputDStream01.union(inputDStream02)
    //对每批次的数据进行词频统计
    val resultDstream: DStream[(String, Int)] = inputDStream
      .filter((line) => line != null && line.split("\\s+").length > 0)
      .flatMap(value => value.trim.split("\\s+"))
      .map(word => word -> 1)
      .reduceByKey((tmp, item) => tmp + item)
    //输出数据
    resultDstream.print(10)
    //设置启动
    ssc.start()
    ssc.awaitTermination()
    ssc.stop(stopSparkContext = true, stopGracefully = true)
  }
}
