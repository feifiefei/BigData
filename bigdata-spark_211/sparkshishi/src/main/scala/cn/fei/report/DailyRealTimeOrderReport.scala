package cn.fei.report

import cn.fei.config.ApplicationConfig
import cn.fei.utils.{JedisUtils, SparkUtils, StreamingUtils}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.{DataTypes, StringType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import redis.clients.jedis.Jedis

/**
 * 实时订单报表：从Kafka Topic实时消费订单数据，进行销售订单额统计，维度如下：
	 * - 第一、总销售额：sum
	 * - 第二、各省份销售额：province
	 * - 第三、重点城市销售额：city
 *      "北京市", "上海市", "深圳市", "广州市", "杭州市", "成都市", "南京市", "武汉市", "西安市"
 * TODO：每日实时统计，每天统计的数据为当日12:00 - 24：00产生的数据
 */
object DailyRealTimeOrderReport {
	
	/** 实时统计：总销售额，使用sum函数 */
	def reportAmtTotal(streamDataFrame: DataFrame): Unit = {
		// a. 导入隐式转换
		val session = streamDataFrame.sparkSession
		import session.implicits._
		
		// b. 业务计算
		val resultStreamDF: Dataset[Row] = streamDataFrame
			// TODO: 由于是聚合操作，需要设置水位线，清除过时状态数据
			.withWatermark("order_timestamp", "10 minutes")
			// TODO：由于每日实时统计，按照日期分组
			.groupBy($"order_date")
			.agg(sum($"money").as("total_amt"))
			// Redis中Hash结构Value值字段
			.select($"total_amt".cast(StringType).as("value"), $"order_date")
			// Redis中Hash结构Field值字段
			.withColumn("field", lit("global"))
			// Redis中Hash结构Key值字段
			.withColumn("prefix", lit("orders:money:total"))
			.withColumn("key", concat($"prefix", lit(":") ,$"order_date"))
		
		// c. 输出Redis及启动流式应用
		resultStreamDF.writeStream
			.outputMode(OutputMode.Update())
			.queryName("query-amt-total")
			// 设置检查点目录
			.option("checkpointLocation", ApplicationConfig.STREAMING_AMT_TOTAL_CKPT)
			// 结果输出到Redis
			.foreachBatch{ (batchDF: DataFrame, _: Long) => saveToRedis(batchDF)}
			.start() // 启动start流式应用
	}
	
	/** 实时统计：各省份销售额，按照province省份分组 */
	def reportAmtProvince(streamDataFrame: DataFrame): Unit = {
		// a. 导入隐式转换
		val session = streamDataFrame.sparkSession
		import session.implicits._
		
		// b. 业务计算
		val resultStreamDF: Dataset[Row] = streamDataFrame
			// TODO: 由于是聚合操作，需要设置水位线，清除过时状态数据
			.withWatermark("order_timestamp", "10 minutes")
			// TODO：由于每日实时统计，按照日期分组，再按照省份分组
			.groupBy($"order_date", $"province")
			.agg(sum($"money").as("total_amt"))
			// Redis中Hash结构Value值字段
			.select(
				$"total_amt".cast(StringType).as("value"),
				$"province".as("field"), //
				$"order_date"
			)
			// Redis中Hash结构Key值字段
			.withColumn("prefix", lit("orders:money:province"))
			.withColumn("key", concat($"prefix", lit(":") ,$"order_date"))
		
		// c. 输出Redis及启动流式应用
		resultStreamDF.writeStream
			.outputMode(OutputMode.Update())
			.queryName("query-amt-province")
			// 设置检查点目录
			.option("checkpointLocation", ApplicationConfig.STREAMING_AMT_PROVINCE_CKPT)
			// 结果输出到Redis
			.foreachBatch{ (batchDF: DataFrame, _: Long) => saveToRedis(batchDF)}
			.start() // 启动start流式应用
	}
	
	/** 实时统计：重点城市cities销售额，按照city城市分组 */
	def reportAmtCity(streamDataFrame: DataFrame): Unit = {
		// a. 导入隐式转换
		val session = streamDataFrame.sparkSession
		import session.implicits._
		
		// 重点城市：9个城市
		val cities: Array[String] = Array(
			"北京市", "上海市", "深圳市", "广州市", "杭州市", "成都市", "南京市", "武汉市", "西安市"
		)
		val citiesBroadcast: Broadcast[Array[String]] = session.sparkContext.broadcast(cities)
		// 自定义UDF函数，判断是否重点城市
		val city_is_contains: UserDefinedFunction = udf(
			(cityName: String) => citiesBroadcast.value.contains(cityName)
		)
		
		// b. 业务计算
		val resultStreamDF: Dataset[Row] = streamDataFrame
			// 过滤重点城市订单数据
			.filter(city_is_contains($"city"))
			// TODO: 由于是聚合操作，需要设置水位线，清除过时状态数据
			.withWatermark("order_timestamp", "10 minutes")
			// TODO：由于每日实时统计，按照日期分组，再按照城市分组
			.groupBy($"order_date", $"city")
			.agg(sum($"money").as("total_amt"))
			// Redis中Hash结构Value值字段
			.select(
				$"total_amt".cast(StringType).as("value"),
				$"city".as("field"), //
				$"order_date"
			)
			// Redis中Hash结构Key值字段
			.withColumn("prefix", lit("orders:money:city"))
			.withColumn("key", concat($"prefix", lit(":") ,$"order_date"))
		
		// c. 输出Redis及启动流式应用
		resultStreamDF.writeStream
			.outputMode(OutputMode.Update())
			.queryName("query-amt-city")
			// 设置检查点目录
			.option("checkpointLocation", ApplicationConfig.STREAMING_AMT_CITY_CKPT)
			// 结果输出到Redis
			.foreachBatch{ (batchDF: DataFrame, _: Long) => saveToRedis(batchDF)}
			.start() // 启动start流式应用
	}
	
	/**
	 * 将数据DataFrame保存至Redis数据库
	 * @param dataframe 分布数据集，包含三个字段：key, field, value
	 */
	def saveToRedis(dataframe: DataFrame): Unit = {
		dataframe
			.coalesce(1)
			.rdd
			.foreachPartition{iter =>
				// 1. 获取Jedis连接实例对象
				val jedis: Jedis = JedisUtils.getJedisPoolInstance(
					ApplicationConfig.REDIS_HOST, ApplicationConfig.REDIS_PORT
				).getResource
				// 选择数据库
				jedis.select(ApplicationConfig.REDIS_DB)
				// 2. 保存数据至Redis中，使用数据结构为：哈希Hash
				iter.foreach{row =>
					val redisKey = row.getAs[String]("key")
					val redisField = row.getAs[String]("field")
					val redisValue = row.getAs[String]("value")
					jedis.hset(redisKey, redisField, redisValue)
				}
				// 3. 关闭连接
				JedisUtils.release(jedis)
			}
	}
	
	def main(args: Array[String]): Unit = {
		
		// 1. 获取SparkSession实例对象
		val spark: SparkSession = SparkUtils.createSparkSession(this.getClass)
		import spark.implicits._
		
		// 2. 从KAFKA读取消费数据
		val kafkaStreamDF: DataFrame = spark
			.readStream
			.format("kafka")
			.option("kafka.bootstrap.servers", ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS)
			.option("subscribe", ApplicationConfig.KAFKA_ETL_TOPIC)
			// 设置每批次消费数据最大值
			.option("maxOffsetsPerTrigger", ApplicationConfig.KAFKA_MAX_OFFSETS)
			.load()
		
		// 3. 提供数据字段
		val orderStreamDF: DataFrame = kafkaStreamDF
			// 获取value字段的值，转换为String类型
			.selectExpr("CAST(value AS STRING)")
			// 转换为Dataset类型
			.as[String]
			// 过滤数据：通话状态为success
			.filter(record => null != record && record.trim.length > 0)
			// TODO: 提取字段信息
			.select(
				// 获取每个订单日期，用于分组
				to_date(get_json_object($"value", "$.orderTime")).as("order_date"),
				// 获取每个订单日期时间，类型是TimeStamp，设置水位线watermark
				to_timestamp(get_json_object($"value", "$.orderTime")).as("order_timestamp"),
				get_json_object(
					$"value", "$.orderMoney").cast(DataTypes.createDecimalType(10, 2)
				).as("money"), //
				get_json_object($"value", "$.province").as("province"), //
				get_json_object($"value", "$.city").as("city") //
			)
		
		// 4. 实时报表统计：总销售额、各省份销售额及重点城市销售额
		reportAmtTotal(orderStreamDF)
		reportAmtProvince(orderStreamDF)
		reportAmtCity(orderStreamDF)
		
		// 5. 定时扫描HDFS文件，优雅的关闭停止StreamingQuery
		spark.streams.active.foreach{query =>
			StreamingUtils.stopStructuredStreaming(query, ApplicationConfig.STOP_STATE_FILE)
		}
	
	}
	
}
