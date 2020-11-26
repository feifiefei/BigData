package cn.fei

import cn.fei.util.StreamingContextUtils
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream

/**
 * @description:* 实时消费Kafka Topic数据，累加统计各个搜索词的搜索次数，实现百度搜索风云榜
 * @author: 飞
 * @date: 2020/11/26 0026 10:38
 */
object StreamingUpdateState {
  def main(args: Array[String]): Unit = {
    // 1. 获取StreamingContext实例对象
    val ssc: StreamingContext = StreamingContextUtils.getStreamingContext(this.getClass, 5)
    // TODO: 设置检查点目录
    ssc.checkpoint(s"datas/streaming/state-${System.nanoTime()}")
    //从kafka消费数据，使用kafka new COnsumer API
    val kafkaDStream: DStream[ConsumerRecord[String, String]] = StreamingContextUtils.consumerKafka(ssc, "search-log-topic")
    // 3. 对每批次的数据进行搜索词次数统计
    val reduceDStream: DStream[(String, Int)] = kafkaDStream.transform{ rdd =>
      val reduceRDD = rdd
        // 过滤不合格的数据
        .filter{ record =>
          val message: String = record.value()
          null != message && message.trim.split(",").length == 4 }
      // 提取搜索词，转换数据为二元组，表示每个搜索词出现一次
        .map{record =>
          val keyword: String = record.value().trim.split(",").last
          keyword -> 1 }
        // 按照单词分组，聚合统计
        .reduceByKey((tmp, item) => tmp + item) // TODO: 先聚合，再更新，优化
      reduceRDD // 返回
    }
    /*
    def updateStateByKey[S: ClassTag](
    // 状态更新函数
    updateFunc: (Seq[V], Option[S]) => Option[S]
    ): DStream[(K, S)]
    第一个参数：Seq[V]
    表示的是相同Key的所有Value值
    第二个参数：Option[S]
    表示的是Key的以前状态，可能有值Some，可能没值None，使用Option封装
    S泛型，具体类型有业务具体，此处是词频：Int类型
    */
    val stateDStream: DStream[(String, Int)] = reduceDStream.updateStateByKey((values: Seq[Int], state: Option[Int]) => {
      // a. 获取以前状态信息
      val previousState = state.getOrElse(0)
      // b. 获取当前批次中Key对应状态
      val currentState = values.sum
      // c. 合并状态
      val latestState = previousState + currentState
      // d. 返回最新状态
      Some(latestState)
    }
    )
    // 5. 将结果数据输出 -> 将每批次的数据处理以后输出
    stateDStream.print()
    // 6.启动流式应用，一直运行，直到程序手动关闭或异常终止
    ssc.start()
    ssc.awaitTermination()
    ssc.stop(stopSparkContext = true, stopGracefully = true)
  }
}
