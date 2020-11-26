package cn.fei

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @description:基于IDEA集成开发环境，编程实现从TCP Socket实时读取流式数据，对每批次中数据进行词频统计。
 * @author: 飞
 * @date: 2020/11/25 0025 19:51
 */
object StreamingTransformRDD {
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
    val inputDStream: DStream[String] = ssc.socketTextStream("node1.itcast.cn", 9999, storageLevel = StorageLevel.MEMORY_AND_DISK)
    //对每批次的数据进行词频统计
    val resultDstream: DStream[(String, Int)] = inputDStream.transform(rdd => {
      rdd
        .filter((line) => line != null && line.split("\\s+").length > 0)
        .flatMap(value => value.trim.split("\\s+"))
        .map(word => word -> 1)
        .reduceByKey((tmp, item) => tmp + item)
    })
    //输出数据
    resultDstream.print(10)
    //设置启动
    ssc.start()
    ssc.awaitTermination()
    ssc.stop(stopSparkContext = true, stopGracefully = true)
  }
}
