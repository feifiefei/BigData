package cn.fei

import cn.fei.util.StreamingContextUtils
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.{State, StateSpec, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, MapWithStateDStream}

/**
 * @description:* 实时消费Kafka Topic数据，累加统计各个搜索词的搜索次数，实现百度搜索风云榜
 * @author: 飞
 * @date: 2020/11/26 0026 10:38
 */
object StreamingMapWithState {
  def main(args: Array[String]): Unit = {
    // 1. 获取StreamingContext实例对象
    val ssc: StreamingContext = StreamingContextUtils.getStreamingContext(this.getClass, 5)
    // TODO: 设置检查点目录
    ssc.checkpoint(s"datas/streaming/state-${System.nanoTime()}")
    //从kafka消费数据，使用kafka new COnsumer API
    val kafkaDStream: DStream[ConsumerRecord[String, String]] = StreamingContextUtils.consumerKafka(ssc, "search-log-topic")
    // 3. 对每批次的数据进行搜索词次数统计
    val reduceDStream: DStream[(String, Int)] = kafkaDStream.transform { rdd =>
      val reduceRDD = rdd
        // 过滤不合格的数据
        .filter { record =>
          val message: String = record.value()
          null != message && message.trim.split(",").length == 4
        }
        // 提取搜索词，转换数据为二元组，表示每个搜索词出现一次
        .map { record =>
          val keyword: String = record.value().trim.split(",").last
          keyword -> 1
        }
        // 按照单词分组，聚合统计
        .reduceByKey((tmp, item) => tmp + item) // TODO: 先聚合，再更新，优化
      reduceRDD // 返回
    }
    val Spec: StateSpec[String, Int, Int, (String, Int)] = StateSpec.function(
      (keyword: String, countOption: Option[Int], state: State[Int]) => {
        //获取当前批次中搜索词搜索次数
        val currentState: Int = countOption.getOrElse(0)
        //从以前状态中获取搜索词搜索次数
        val previousState = state.getOption().getOrElse(0)
        //搜索词总的搜索次数
        val latestState = currentState + previousState
        state.update(latestState)

        (keyword, latestState)

      }
    )
    //调用mapWIthState函数进行实时累加状态 统计
    val stateDStream: MapWithStateDStream[String, Int, Int, (String, Int)] = reduceDStream.mapWithState(Spec)
    //输出结果
    stateDStream.print()
    // 6.启动流式应用，一直运行，直到程序手动关闭或异常终止
    ssc.start()
    ssc.awaitTermination()
    ssc.stop(stopSparkContext = true, stopGracefully = true)
  }
}
