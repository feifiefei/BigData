package cn.fei

import java.util.Date

import org.apache.commons.lang3.time.FastDateFormat
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @description:基于IDEA集成开发环境，编程实现从TCP Socket实时读取流式数据，对每批次中数据进行词频统计。
 * @author: 飞
 * @date: 2020/11/25 0025 19:51
 */
object StreamingOutputRDD {
  def main(args: Array[String]): Unit = {
    //构建StreamingContext流式上下文实例对象
    val ssc: StreamingContext = {
      val sparkConf = new SparkConf()
        .setMaster("local[3]")
        .setAppName(this.getClass.getSimpleName.stripSuffix("$"))
        //todo:设置输出文件系统的算法版本为2
        .set("spark.hadoop.mapreduce.fileoutputcommitter.algorithm.version", "2")
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
    // TODO: 4. 将结果数据输出 -> 将每批次的数据处理以后输出
    /*
      对DStream中每批次结果RDD数据进行输出操作
      def foreachRDD(foreachFunc: (RDD[T], Time) => Unit): Unit
      其中Time就是每批次BatchTime，Long类型数据, 转换格式：2020/05/10 16:53:25
    */
    resultDstream.foreachRDD { (rdd, time) =>
      //使用lang3包下FastDateFormat日期格式类，属于线程安全
      val batchTime: String = FastDateFormat.getInstance("yyyyMMddHHmmss")
        .format(new Date(time.milliseconds))
      println("-----------------------------------")
      println(s"Time:${batchTime}")
      println("-----------------------------------")
      //todo:先判断RDD是否有数据，有数据在输出
      if (!rdd.isEmpty()) {
        val resultRDD = rdd.coalesce(1)
        resultRDD
          .foreachPartition(iter => iter.foreach(print))
        resultRDD.saveAsTextFile(s"/datas/streaming/wc-output-${batchTime}")
      }
    }
    //设置启动
    ssc.start()
    ssc.awaitTermination()
    ssc.stop(stopSparkContext = true, stopGracefully = true)
  }
}
