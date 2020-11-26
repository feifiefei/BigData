package cn.fei

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @description:SPark Streaming 流词频统计
 * @author: 飞
 * @date: 2020/11/25 0025 11:18
 */
object StreamingWordCount {
  def main(args: Array[String]): Unit = {
    val ssc: StreamingContext = {
      //创建spark conf对象，设置应用基本信息
      val sparkConf = new SparkConf()
        .setAppName(this.getClass.getSimpleName.stripSuffix("$"))
        .setMaster("local[3]")
      //创建实例对象，设置BatchInterval
      //时间间隔用于划分数据为很多批次Batch
      new StreamingContext(sparkConf, Seconds(5))
    }
    //读取流数据
    val inputDStream: DStream[String] = ssc.socketTextStream("node1.itcast.cn", 9999)
    //转换函数
    val resultDStream = inputDStream
      .flatMap(line => line.split("\\s+"))
      .map(word => word -> 1)
      .reduceByKey((x, y) => x + y)
    //定义数据终端，将每批次结果数据进行输出
    resultDStream.print()
    //启动流式应用等待终止
    ssc.start()
    ssc.awaitTermination()
    ssc.stop(stopSparkContext = true, stopGracefully = true)
  }
}
