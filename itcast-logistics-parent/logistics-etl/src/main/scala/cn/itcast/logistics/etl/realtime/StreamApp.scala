package cn.itcast.logistics.etl.realtime

import cn.itcast.logistics.common.Configuration
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * 所有ETL流式处理的基类BaseClass，实时增量ETL至：Kudu、Elasticsearch和ClickHouse都要实现此基类，定义三个方法
	 * - 1. 加载数据：loadKafkaSource
	 * - 2. 处理数据：execute
	 * - 3. 保存数据：save
 */
trait StreamApp {
	
	/**
	 * 读取数据的方法
	 * @param spark SparkSession
	 * @param topic 指定消费的主题
	 * @param selectExpr 默认值：CAST(value AS STRING)
	 */
	def getKafkaSource(spark: SparkSession, topic: String,
	                   selectExpr:String = "CAST(value AS STRING)"): DataFrame = {
		spark.readStream
			.format(Configuration.SPARK_KAFKA_FORMAT)
			.option("kafka.bootstrap.servers", Configuration.KAFKA_ADDRESS)
			.option("subscribe" , topic)
			//该参数可以省略，不需要指定（官网提到改参数不能设置： kafka的source会在每次query的时候自定创建唯一的group id）
			.option("group.id" , "logistics")
			//表示数据丢失以后（topic被删除，或者offset不存在可用的范围的时候）
			.option("failOnDataLoss" , "false")
			.load()
			.selectExpr(selectExpr)
	}
	
	/**
	 * 数据的处理：包含从数据源加载数据、对数据进行转换ETL操作及最终将数据保存至外部存储引擎（比如ES、Kudu和CK）
	 * @param sparkConf SparkConf实例对象，设置应用相关配置
	 */
	def execute(sparkConf: SparkConf): Unit
	
	/**
	 * 数据的保存
	 * @param dataframe 保存数据集DataFrame
	 * @param tableName 保存表的名称
	 * @param isAutoCreateTable 是否自动创建表，默认创建表
	 */
	def save(dataframe: DataFrame, tableName: String, isAutoCreateTable: Boolean = true)
	
}
