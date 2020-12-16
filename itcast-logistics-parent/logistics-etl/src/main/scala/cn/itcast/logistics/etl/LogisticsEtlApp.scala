package cn.itcast.logistics.etl

import cn.itcast.logistics.common.Configuration
import org.apache.commons.lang3.SystemUtils
import org.apache.spark.SparkConf
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{Dataset, SparkSession}

/**
 * 编写StructuredStreaming程序，实时从Kafka消息数据（物流相关数据和CRM相关数据），打印控制台Console
	 * 1. 初始化设置Spark Application配置
	 * 2. 判断Spark Application运行模式进行设置
	 * 3. 构建SparkSession实例对象
	 * 4. 初始化消费物流Topic数据参数
	 * 5. 消费物流Topic数据，打印控制台
	 * 6. 初始化消费CRM Topic数据参数
	 * 7. 消费CRM Topic数据，打印控制台
	 * 8. 启动流式应用，等待终止
 */
object LogisticsEtlApp {
	
	def main(args: Array[String]): Unit = {
		// 1. 初始化设置Spark Application配置
		val sparkConf: SparkConf = new SparkConf()
    		.setAppName(this.getClass.getSimpleName.stripSuffix("$"))
			// 设置时区
			.set("spark.sql.session.timeZone", "Asia/Shanghai")
			// 设置单个分区可容纳的最大字节数，默认是128M， 等同于block块的大小
			.set("spark.sql.files.maxPartitionBytes", "134217728")
			// 设置合并小文件的阈值，避免每个小文件占用一个分区的情况
			.set("spark.sql.files.openCostInBytes", "134217728")
			// 设置join或者shuffle的时候使用的分区数，默认情况下分区数是200
			.set("spark.sql.shuffle.partitions", "4")
			// 设置join操作时可以广播到worker节点的最大字节大小，可以避免shuffe操作
			.set("spark.sql.autoBroadcastJoinThreshold", "67108864")
		
		// 2. 判断Spark Application运行模式进行设置
		if (SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_MAC) {
			//本地环境LOCAL_HADOOP_HOME
			System.setProperty("hadoop.home.dir", Configuration.LOCAL_HADOOP_HOME)
			//设置运行环境和checkpoint路径
			sparkConf
				.set("spark.master", "local[*]")
				.set("spark.sql.streaming.checkpointLocation", Configuration.SPARK_APP_WIN_CHECKPOINT_DIR)
		}else {
			//生产环境
			sparkConf
				.set("spark.master", "yarn")
				.set("spark.sql.streaming.checkpointLocation", Configuration.SPARK_APP_DFS_CHECKPOINT_DIR)
		}
		
		// 3. 构建SparkSession实例对象
		val spark: SparkSession = SparkSession.builder()
    		.config(sparkConf)
    		.getOrCreate()
		import spark.implicits._   // 导入隐式转换函数
		spark.sparkContext.setLogLevel(Configuration.LOG_OFF)  // 设置日志级别，将其关闭
		
		// 4. 初始化消费物流Topic数据参数
		val logisticsKafkaParams: Map[String, String] = Map[String, String](
			"kafka.bootstrap.servers" -> Configuration.KAFKA_ADDRESS,
			"subscribe" -> Configuration.KAFKA_LOGISTICS_TOPIC,
			"group.id" -> "logistics",
			//表示数据丢失以后（topic被删除，或者offset不存在可用的范围的时候）
			"failOnDataLoss" -> "false"
		)
		
		// 5. 消费物流Topic数据，打印控制台
		val logisticsDS: Dataset[String] = spark.readStream
			.format("kafka")
    		.options(logisticsKafkaParams)
			.load()
			.selectExpr("CAST(value AS STRING)")
			.as[String]
		logisticsDS.writeStream
			.outputMode(OutputMode.Update())
			.queryName("logistics-query")
			.format("console")
			.option("numRows", "20").option("truncate", "false")
			.start()
		
		// 6. 初始化消费CRM Topic数据参数
		val crmKafkaParams: Map[String, String] = Map[String, String](
			"kafka.bootstrap.servers" -> Configuration.KAFKA_ADDRESS,
			"subscribe" -> Configuration.KAFKA_CRM_TOPIC,
			"group.id" -> "crm",
			//表示数据丢失以后（topic被删除，或者offset不存在可用的范围的时候）
			"failOnDataLoss" -> "false"
		)
		
		// 7. 消费CRM Topic数据，打印控制台
		val crmDS: Dataset[String] = spark.readStream
			.format("kafka")
			.options(crmKafkaParams)
			.load()
			.selectExpr("CAST(value AS STRING)")
			.as[String]
		crmDS.writeStream
			.outputMode(OutputMode.Update())
			.queryName("crm-query")
			.format("console")
			.option("numRows", "20").option("truncate", "false")
			.start()
		
		// 8. 启动流式应用，等待终止
		spark.streams.active.foreach(query => println(s"准备启动查询Query： ${query.name}"))
		spark.streams.awaitAnyTermination()
	}
	
}
