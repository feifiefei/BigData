package cn.itcast.logistics.etl.realtime

import cn.itcast.logistics.common.beans.parser.{CanalMessageBean, OggMessageBean}
import cn.itcast.logistics.common.{Configuration, SparkUtils}
import com.alibaba.fastjson.JSON
import org.apache.spark.SparkConf
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{DataFrame, Dataset, Encoders, SparkSession}

object KuduStreamApp extends StreamApp {
	
	
	/**
	 * 数据的处理：包含从数据源加载数据、对数据进行转换ETL操作及最终将数据保存至外部存储引擎（比如ES、Kudu和CK）
	 *
	 * @param sparkConf SparkConf实例对象，设置应用相关配置
	 */
	override def execute(sparkConf: SparkConf): Unit = {
		/*
			数据处理的步骤：
				step1. 创建SparkSession实例对象，传递SparkConf
				step2. 从Kafka数据源实时消费数据
				step3. 对JSON格式字符串数据进行转换处理
				step4. 获取消费每条数据字段信息
				step5. 将解析过滤获取的数据写入同步至Kudu表
			 */
		// step1. 创建SparkSession实例对象，传递SparkConf
		val spark: SparkSession = SparkUtils.getSparkSession(sparkConf)
		import spark.implicits._
		spark.sparkContext.setLogLevel(Configuration.LOG_OFF)
		
		// step2. 从Kafka数据源实时消费数据
		// 获取物流相关数据
		val logisticsDF: DataFrame = getKafkaSource(spark, Configuration.KAFKA_LOGISTICS_TOPIC)
		val logisticsBeanDS: Dataset[OggMessageBean] = logisticsDF
			.as[String] // 将DataFrame转换为Dataset，由于DataFrame中只有一个字段value，类型是String
			// 对Dataset中每个分区数据进行操作，每个分区数据封装在迭代器中
			.mapPartitions{iter =>
				//val xx: Iterator[String] = iter
				iter
					.filter(jsonStr => null != jsonStr && jsonStr.trim.length > 0 )// 过滤数据
					// 解析JSON格式数据, 使用FastJson类库
					.map{jsonStr => JSON.parseObject(jsonStr, classOf[OggMessageBean]) }
			}(Encoders.bean(classOf[OggMessageBean]))
		
		// 获取CRM相关数据
		val crmDF: DataFrame = getKafkaSource(spark, Configuration.KAFKA_CRM_TOPIC)
		implicit val canalEncoder = Encoders.bean(classOf[CanalMessageBean])
		val crmBeanDS: Dataset[CanalMessageBean] = crmDF
			.as[String]
			.mapPartitions{iter =>
				//val xx: Iterator[String] = iter
				iter
					.filter(jsonStr => null != jsonStr && jsonStr.trim.length > 0 )// 过滤数据
					// 解析JSON格式数据, 使用FastJson类库
					.map{jsonStr => JSON.parseObject(jsonStr, classOf[CanalMessageBean]) }
			}
		
		// 消费Topic数据，打印控制台
		logisticsBeanDS.writeStream
			.outputMode(OutputMode.Update())
			.queryName("logistics-query")
			.format("console")
			.option("numRows", "20").option("truncate", "false")
			.start()
		crmBeanDS.writeStream
			.outputMode(OutputMode.Update())
			.queryName("crm-query")
			.format("console")
			.option("numRows", "20").option("truncate", "false")
			.start()
		
		spark.streams.active.foreach(query => println(s"准备启动查询Query： ${query.name}"))
		spark.streams.awaitAnyTermination()
	}
	
	/**
	 * 数据的处理：包含从数据源加载数据、对数据进行转换ETL操作及最终将数据保存至外部存储引擎（比如ES、Kudu和CK）
	 *
	 * @param sparkConf SparkConf实例对象，设置应用相关配置
	 */
	def executeFromKafka(sparkConf: SparkConf): Unit = {
		/*
			数据处理的步骤：
				step1. 创建SparkSession实例对象，传递SparkConf
				step2. 从Kafka数据源实时消费数据
				step3. 对JSON格式字符串数据进行转换处理
				step4. 获取消费每条数据字段信息
				step5. 将解析过滤获取的数据写入同步至Kudu表
			 */
		// step1. 创建SparkSession实例对象，传递SparkConf
		val spark: SparkSession = SparkUtils.getSparkSession(sparkConf)
		spark.sparkContext.setLogLevel(Configuration.LOG_OFF)
		
		// step2. 从Kafka数据源实时消费数据
		// 获取物流相关数据
		val logisticsDF: DataFrame = getKafkaSource(spark, Configuration.KAFKA_LOGISTICS_TOPIC)
		// 获取CRM相关数据
		val crmDF: DataFrame = getKafkaSource(spark, Configuration.KAFKA_CRM_TOPIC)
		
		// 消费Topic数据，打印控制台
		logisticsDF.writeStream
			.outputMode(OutputMode.Update())
			.queryName("logistics-query")
			.format("console")
			.option("numRows", "20").option("truncate", "false")
			.start()
		crmDF.writeStream
			.outputMode(OutputMode.Update())
			.queryName("crm-query")
			.format("console")
			.option("numRows", "20").option("truncate", "false")
			.start()
		spark.streams.active.foreach(query => println(s"准备启动查询Query： ${query.name}"))
		spark.streams.awaitAnyTermination()
	}
	
	/**
	 * 数据的保存
	 *
	 * @param dataframe         保存数据集DataFrame
	 * @param tableName         保存表的名称
	 * @param isAutoCreateTable 是否自动创建表，默认创建表
	 */
	override def save(dataframe: DataFrame, tableName: String, isAutoCreateTable: Boolean): Unit = {
	
	}
	
	// StructuredStreaming应用程序入口：MAIN 方法
	def main(args: Array[String]): Unit = {
		// 第一步、构建SparkConf对象，设置应用基本配置
		/*
			val sparkConf = SparkUtils.sparkConf(this.getClass)
			val conf = SparkUtils.autoSettingEnv(sparkConf)
		*/
		val sparkConf: SparkConf = SparkUtils.autoSettingEnv(SparkUtils.sparkConf(this.getClass))
		
		// 第二步、调度execute方法，具体处理数据，从数据源、数据转换和数据保存输出
		execute(sparkConf)
	}
	
}
