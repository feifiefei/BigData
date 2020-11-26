package cn.fei

import java.util.Date

import org.apache.commons.lang3.time.FastDateFormat
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @description:Streaming通过Kafka New Consumer消费者API获取数据
 * @author: 飞
 * @date: 2020/11/25 0025 21:15
 */
object StreamingSourceKafka {
  def main(args: Array[String]): Unit = {
    // 1. 构建StreamingContext流式上下文实例对象
    val ssc: StreamingContext = {
      // a. 创建SparkConf对象，设置应用配置信息
      val sparkConf = new SparkConf()
        .setAppName(this.getClass.getSimpleName.stripSuffix("$"))
        .setMaster("local[3]")
      // b.创建流式上下文对象, 传递SparkConf对象，TODO: 时间间隔 -> 用于划分流式数据为很多批次Batch
      val context = new StreamingContext(sparkConf, Seconds(5))
      // c. 返回
      context
    }
    // ii.读取哪些Topic数据
    val topics = Array("wc-topic")
    // iii.消费Kafka 数据配置参数
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "node1.itcast.cn:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "group_id_streaming_0001",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean))
    // iv.消费数据策略
    val consumerStrategy: ConsumerStrategy[String, String] = ConsumerStrategies.Subscribe(
      topics, kafkaParams
    )
    // v.采用新消费者API获取数据，类似于Direct方式
    val kafkaDStream: DStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(
      ssc,
      LocationStrategies.PreferConsistent,
      consumerStrategy
    )
    // 3. 对每批次的数据进行词频统计
    val resultDStream: DStream[(String, Int)] = kafkaDStream.transform(kafkaRDD => {
      kafkaRDD
        .map(record => record.value()) // 获取Message数据
        // 过滤不合格的数据
        .filter(line => null != line && line.trim.length > 0)
        // 按照分隔符划分单词
        .flatMap(line => line.trim.split("\\s+"))
        // 转换数据为二元组，表示每个单词出现一次
        .map(word => (word, 1))
        // 按照单词分组，聚合统计
        .reduceByKey((tmp, item) => tmp + item)
    })
    // 4. 将结果数据输出 -> 将每批次的数据处理以后输出
    resultDStream.foreachRDD { (rdd, time) =>
      val batchTime: String = FastDateFormat.getInstance("yyyy/MM/dd HH:mm:ss")
        .format(new Date(time.milliseconds))
      println("-------------------------------------------")
      println(s"Time: $batchTime")
      println("-------------------------------------------")
      // TODO: 先判断RDD是否有数据，有数据在输出哦
      if (!rdd.isEmpty()) {
        rdd.coalesce(1).foreachPartition { iter => iter.foreach(item => println(item)) }
      }
    }
    // 5. 对于流式应用来说，需要启动应用
    ssc.start()
    // 流式应用启动以后，正常情况一直运行（接收数据、处理数据和输出数据），除非人为终止程序或者程序异常停止
    ssc.awaitTermination()
    // 关闭流式应用(参数一：是否关闭SparkContext，参数二：是否优雅的关闭）
    ssc.stop(stopSparkContext = true, stopGracefully = true)
  }
}
