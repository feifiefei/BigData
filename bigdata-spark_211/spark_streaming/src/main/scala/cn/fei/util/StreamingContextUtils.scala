package cn.fei.util

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * 工具类提供：构建流式应用上下文StreamingContext实例对象和从Kafka Topic消费数据
 */
object StreamingContextUtils {

	/**
	 * 获取StreamingContext实例，传递批处理时间间隔
	 * @param batchInterval 批处理时间间隔，单位为秒
	 */
	def getStreamingContext(clazz: Class[_], batchInterval: Int): StreamingContext = {
		// i. 创建SparkConf对象，设置应用配置信息
		val sparkConf = new SparkConf()
			.setAppName(clazz.getSimpleName.stripSuffix("$"))
			.setMaster("local[3]")
			// 设置Kryo序列化
			.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
			.registerKryoClasses(Array(classOf[ConsumerRecord[String, String]]))
			// 设置保存文件数据时，算法版本：2
    		.set("spark.hadoop.mapreduce.fileoutputcommitter.algorithm.version", "2")
		// ii.创建流式上下文对象, 传递SparkConf对象和时间间隔
		val context = new StreamingContext(sparkConf, Seconds(batchInterval))
		// iii. 返回
		context
	}

	/**
	 * 从指定的Kafka Topic中消费数据，默认从最新偏移量（largest）开始消费
	 * @param ssc StreamingContext实例对象
	 * @param topicName 消费Kafka中Topic名称
	 */
	def consumerKafka(ssc: StreamingContext, topicName: String): DStream[ConsumerRecord[String, String]] = {
		// i.位置策略
		val locationStrategy: LocationStrategy = LocationStrategies.PreferConsistent
		// ii.读取哪些Topic数据
		val topics = Array(topicName)
		// iii.消费Kafka 数据配置参数
		val kafkaParams = Map[String, Object](
			"bootstrap.servers" -> "node1.itcast.cn:9092",
			"key.deserializer" -> classOf[StringDeserializer],
			"value.deserializer" -> classOf[StringDeserializer],
			"group.id" -> "gui_0001",
			"auto.offset.reset" -> "latest",
			"enable.auto.commit" -> (false: java.lang.Boolean)
		)
		// iv.消费数据策略
		val consumerStrategy: ConsumerStrategy[String, String] = ConsumerStrategies.Subscribe(
			topics, kafkaParams
		)

		// v.采用新消费者API获取数据，类似于Direct方式
		val kafkaDStream: DStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(
			ssc, locationStrategy, consumerStrategy
		)
		// vi.返回DStream
		kafkaDStream
	}

}
