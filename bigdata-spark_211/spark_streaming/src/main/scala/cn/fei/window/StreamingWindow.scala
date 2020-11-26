package cn.fei.window
import cn.fei.util.StreamingContextUtils
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.DStream
/**
 * @description:
 * @author: 飞
 * @date: 2020/11/26 0026 11:08
 */
object StreamingWindow {

  def main(args: Array[String]): Unit = {

    // Streaming应用BatchInterval
    val BATCH_INTERVAL: Int = 2
    // Streaming应用窗口大小
    val WINDOW_INTERVAL: Int = BATCH_INTERVAL * 2
    val SLIDER_INTERVAL: Int = BATCH_INTERVAL * 1

    // 1. 获取StreamingContext实例对象
    val ssc: StreamingContext = StreamingContextUtils.getStreamingContext(this.getClass, BATCH_INTERVAL)

    // 2. 从Kafka消费数据，使用Kafka New Consumer API
    val kafkaDStream: DStream[ConsumerRecord[String, String]] = StreamingContextUtils
      .consumerKafka(ssc, "search-log-topic")

    // TODO: 设置窗口，窗口大小和滑动大小（每个多久执行一次）
    /*
      def window(windowDuration: Duration, slideDuration: Duration): DStream[T]
     */
    val windowDStream: DStream[ConsumerRecord[String, String]] = kafkaDStream.window(
      Seconds(WINDOW_INTERVAL), Seconds(SLIDER_INTERVAL)
    )

    // 3. 对每批次的数据进行搜索词次数统计
    val resultDStream: DStream[(String, Int)] = windowDStream.transform{ rdd =>
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
        // 按照搜索词分组，聚合：表示当前批次中数据各个搜索词被搜索的次数
        .reduceByKey((tmp, item) => tmp + item)
    }

    // 5. 将结果数据输出 -> 将每批次的数据处理以后输出
    resultDStream.print()

    // 6.启动流式应用，一直运行，直到程序手动关闭或异常终止
    ssc.start()
    ssc.awaitTermination()
    ssc.stop(stopSparkContext = true, stopGracefully = true)
  }
}
