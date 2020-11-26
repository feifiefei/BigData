package cn.fei.window

import cn.fei.util.StreamingContextUtils
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.DStream

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/26 0026 11:07
 */
/**
 * 实时消费Kafka Topic数据，每隔一段时间统计最近搜索日志中搜索词次数
 * 批处理时间间隔：BatchInterval = 2s
 * 窗口大小间隔：WindowInterval = 4s
 * 滑动大小间隔：SliderInterval = 2s
 */
object StreamingReduceWindow {
  def main(args: Array[String]): Unit = {

    // Streaming应用BatchInterval
    val BATCH_INTERVAL: Int = 2
    // Streaming应用窗口大小
    val WINDOW_INTERVAL: Int = BATCH_INTERVAL * 2
    val SLIDER_INTERVAL: Int = BATCH_INTERVAL * 1

    // 1. 获取StreamingContext实例对象
    val ssc: StreamingContext = StreamingContextUtils.getStreamingContext(this.getClass, BATCH_INTERVAL)

    // TODO: 设置检查点目录，存储窗口状态值
    ssc.checkpoint("datas/streaming/window/1001")

    // 2. 从Kafka消费数据，使用Kafka New Consumer API
    val kafkaDStream: DStream[ConsumerRecord[String, String]] = StreamingContextUtils
      .consumerKafka(ssc, "search-log-topic")

    // 3. 对每批次的数据进行搜索词次数统计
    val etlDStream: DStream[(String, Int)] = kafkaDStream.transform{ rdd =>
      // 数据格式 -> 9ff75446e7822928,106.90.139.4,20200919174620234,美国纽约州发生枪击案
      rdd
        // 获取Topic中每条数据Messsage
        .map(record => record.value())
        // 过滤数据
        .filter(msg => null != msg && msg.trim.split(",").length == 4)
        // 提取：搜索词，以二元组形式表示被搜索一次
        .map{msg =>
          val keyword = msg.trim.split(",")(3)
          (keyword, 1)
        }
    }

    // TODO：将窗口设置与聚合函数何在一起使用
    /*
      def reduceByKeyAndWindow(
          reduceFunc: (V, V) => V,
          windowDuration: Duration,
          slideDuration: Duration
        ): DStream[(K, V)]
     */
    val restDStream: DStream[(String, Int)] = etlDStream.reduceByKeyAndWindow(
      (tmp: Int, item: Int) => tmp + item, // 聚合函数
      Seconds(WINDOW_INTERVAL), //窗口大小
      Seconds(SLIDER_INTERVAL) // 滑动大小
    )

    /*
      针对窗口聚合函数，有很多重载方法，其中有个优化函数,性能比较好，推荐使用
        def reduceByKeyAndWindow(
            reduceFunc: (V, V) => V,
            invReduceFunc: (V, V) => V,
            windowDuration: Duration,
            slideDuration: Duration = self.slideDuration,
            numPartitions: Int = ssc.sc.defaultParallelism,
            filterFunc: ((K, V)) => Boolean = null
          ): DStream[(K, V)]
     */
    val resultDStream: DStream[(String, Int)] = etlDStream.reduceByKeyAndWindow(
      (tmp: Int, item: Int) => tmp + item, // 聚合函数
      (tmp: Int, item: Int) => tmp - item, // 聚合函数相反操作函数
      Seconds(WINDOW_INTERVAL), //窗口大小
      Seconds(SLIDER_INTERVAL), // 滑动大小
      filterFunc = (tuple: (String, Int)) => tuple._2 != 0
    )

    // 5. 将结果数据输出 -> 将每批次的数据处理以后输出
    resultDStream.print()

    // 6.启动流式应用，一直运行，直到程序手动关闭或异常终止
    ssc.start()
    ssc.awaitTermination()
    ssc.stop(stopSparkContext = true, stopGracefully = true)
  }
}
