package cn.itcast.logistics.offline.dws

import cn.itcast.logistics.common.{Configuration, OfflineTableDefine, SparkUtils}
import cn.itcast.logistics.offline.OfflineApp
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType, LongType, StringType, StructType}
import org.apache.spark.storage.StorageLevel

import scala.collection.mutable.ListBuffer

object ExpressBillDWS extends OfflineApp{
	/**
	 * 数据处理，包含加载宽表数据、依据指标进行分析和保存指标数据到DWS层
	 */
	override def execute(spark: SparkSession): Unit = {
		import spark.implicits._
		
		// step1. 加载宽表数据
		val expressBillDetailAllDF: DataFrame = getKuduSource(
			spark, OfflineTableDefine.EXPRESS_BILL_DETAIL, isLoadFullData = Configuration.IS_FIRST_RUNNABLE
		)
		// 由于多个指标分析，宽表数据被使用多次，需要缓存
		expressBillDetailAllDF.persist(StorageLevel.MEMORY_AND_DISK)
		
		if(expressBillDetailAllDF.isEmpty){
			logWarning(s"Kudu数据库DWD层[${OfflineTableDefine.EXPRESS_BILL_DETAIL}]表，没有加载到数据，请确认有数据在执行......")
			System.exit(-1)
		}
		
		// TODO: 假设每次加载宽表的数据都是全量的，离线分析按照每天聚合计算，将数据划分为不同天，针对每天的数据进行聚合计算
		// 定义集合列表，可变的列表，将每天计算指标数据Row存储
		val rowList: ListBuffer[Row] = ListBuffer[Row]()
		expressBillDetailAllDF.select($"day").distinct().collect().foreach{dayRow =>
			// val xx: Row = dayRow
			val day: String = dayRow.getAs[String](0) // 1210, 1211, 1212
			// 按照day进行过滤获取每天的数据，进行指标的计算
			val expressBillDetailDF: Dataset[Row] = expressBillDetailAllDF.filter($"day" === day)
			
			// step3. 按照业务指标计算
			// 指标一：计算总快递单数
			//expressBillDetailDF.count() // 返回值为Long类型
			val expressBillTotalDF: DataFrame = expressBillDetailDF.agg(count($"id").as("total"))
			
			// 指标二：各类客户快递单数，最大、最小、平均
			val expressBillTypeTotalDF: DataFrame = expressBillDetailDF.groupBy($"type").count()
			val expressBillTypeTotalAggDF: DataFrame = expressBillTypeTotalDF.agg(
				max($"count").as("maxTypeTotal"), //
				min($"count").as("minTypeTotal"), //
				round(avg($"count").as("avgTypeTotal"), 0) //
			)
			
			// 各网点快递单数指标三：，最大、最小、平均
			val expressBillDotTotalDF: DataFrame = expressBillDetailDF.groupBy($"dot_id").count()
			val expressBillDotTotalAggDF: DataFrame = expressBillDotTotalDF.agg(
				max($"count").as("maxDotTotal"), //
				min($"count").as("minDotTotal"), //
				round(avg($"count").as("avgDotTotal"), 0) //
			)
			
			// 指标四：各渠道快递单数，最大、最小、平均
			val expressBillChannelTotalDF: DataFrame = expressBillDetailDF.groupBy($"order_channel_id").count()
			val expressBillChannelTotalAggDF: DataFrame = expressBillChannelTotalDF.agg(
				max($"count").as("maxChannelTotal"), //
				min($"count").as("minChannelTotal"), //
				round(avg($"count").as("avgChannelTotal"), 0) //
			)
			
			// 指标五：各终端快递单数，最大、最小、平均
			val expressBillTerminalTotalDF: DataFrame = expressBillDetailDF.groupBy($"order_terminal_type").count()
			val expressBillTerminalTotalAggDF: DataFrame = expressBillTerminalTotalDF.agg(
				max($"count").as("maxTerminalTotal"), //
				min($"count").as("minTerminalTotal"), //
				round(avg($"count").as("avgTerminalTotal"), 0) //
			)
			
			// TODO： 需要将计算所有指标结果提取出来，并且组合到Row对象中
			/*
				Row对象创建方式2种：
					-1. Row(v1, v2, v3, ....)
					-2. Row.fromSeq(Seq(v1, v2, v3, ...))
					
				scala> val s1 = Seq(1)
				s1: Seq[Int] = List(1)
				
				scala> val s2 = Seq(2, 3)
				s2: Seq[Int] = List(2, 3)
				
				scala> s1 :+ s2
				res0: Seq[Any] = List(1, List(2, 3))
				
				scala> s1 ++ s2
				res1: Seq[Int] = List(1, 2, 3)
			 */
			val aggRow: Row = Row.fromSeq(
				dayRow.toSeq ++
					expressBillTotalDF.first().toSeq ++
					expressBillTypeTotalAggDF.first().toSeq ++
					expressBillDotTotalAggDF.first().toSeq ++
					expressBillChannelTotalAggDF.first().toSeq ++
					expressBillTerminalTotalAggDF.first().toSeq
			)
			// 将每天聚合计算结果加入列表中
			rowList += aggRow
		}
		
		/**
		 * 问题：如何将列表中数据转换为DataFrame？？？？
		 *      采用并行化方向将列表（Seq类型对象）转换为RDD
		 *      RDD转换为DataFrame： 反射RDD[CaseClass] 和 自定义Schema-> RDD[Row]和Schema
		 */
		// 第一步、将列表转换为RDD
		val rowsRDD: RDD[Row] = spark.sparkContext.parallelize(rowList.toList) // 将可变集合对象转换为不可变的
		// 第二步、自定义Schema信息
		val aggSchema: StructType = new StructType()
    		.add("id", StringType, nullable = false) // 针对每天数据进行聚合得到一个结果，设置day为结果表中id
    		.add("total", LongType, nullable = true)
    		.add("maxTypeTotal", LongType, nullable = true)
    		.add("minTypeTotal", LongType, nullable = true)
    		.add("avgTypeTotal", DoubleType, nullable = true)
			.add("maxDotTotal", LongType, nullable = true)
			.add("minDotTotal", LongType, nullable = true)
			.add("avgDotTotal", DoubleType, nullable = true)
			.add("maxChannelTotal", LongType, nullable = true)
			.add("minChannelTotal", LongType, nullable = true)
			.add("avgChannelTotal", DoubleType, nullable = true)
			.add("maxTerminalTotal", LongType, nullable = true)
			.add("minTerminalTotal", LongType, nullable = true)
			.add("avgTerminalTotal", DoubleType, nullable = true)
		
		// 第三步、调用SparkSession中createDataFrame方法，组合RowsRDD和Schema为DataFrame
		val aggDF: DataFrame = spark.createDataFrame(rowsRDD, aggSchema)
		aggDF.show(10, truncate = false)
		
		// step5. 将聚合指标数据保存Kudu的DWS层表中
		save(aggDF, OfflineTableDefine.EXPRESS_BILL_SUMMARY)
		
		// step6. 释放资源
		expressBillDetailAllDF.unpersist()
	}
	
	def main(args: Array[String]): Unit = {
		// a. 构建SparkConf对象，设置应用相关配置信息
		val sparkConf: SparkConf = SparkUtils.autoSettingEnv(
			SparkUtils.sparkConf(this.getClass)
		)
		val spark: SparkSession = SparkUtils.getSparkSession(sparkConf)
		spark.sparkContext.setLogLevel(Configuration.LOG_OFF)
		// b. 调用execute方法，加载源数据、处理转换数据和保存数据
		execute(spark)
		// c. 应用结束，关闭资源
		spark.stop()
	}
}
