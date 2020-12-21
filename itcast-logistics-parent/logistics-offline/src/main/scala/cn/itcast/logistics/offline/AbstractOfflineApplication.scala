package cn.itcast.logistics.offline

import cn.itcast.logistics.common.{Configuration, SparkUtils, Tools}
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions.{col, current_date, date_format, date_sub}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.storage.StorageLevel

/**
 * 离线报表分析基类：采用模板方法设计模式构建， 包含基本方法和模板方法
 */
abstract class AbstractOfflineApplication {
	
	// 声明变量，构建SparkSession实例对象，程序入口
	var spark: SparkSession = _
	
	/**
	 * 初始化SparkSession实例
	 */
	def init(clazz: Class[_]): Unit = {
		// a. 获取SparkConf对象
		val sparkConf: SparkConf = SparkUtils.autoSettingEnv(
			SparkUtils.sparkConf(clazz)
		)
		// b. 构建SparkSession实例
		spark = SparkUtils.getSparkSession(sparkConf)
		spark.sparkContext.setLogLevel(Configuration.LOG_OFF)
	}
	
	/**
	 * 从Kudu数据源加载数据，指定表的名称
	 * @param tableName 表的名称
	 * @return 分布式数据集DataFrame
	 */
	def loadKuduSource(tableName: String, isLoadFullData: Boolean = false): DataFrame = {
		if(isLoadFullData) {
			// 加载全部的数据
			spark.read
				.format(Configuration.SPARK_KUDU_FORMAT)
				.option("kudu.master", Configuration.KUDU_RPC_ADDRESS)
				.option("kudu.table", tableName)
				.option("kudu.socketReadTimeoutMs", "60000")
				.load()
		} else { //加载增量数据
			spark.read
				.format(Configuration.SPARK_KUDU_FORMAT)
				.option("kudu.master", Configuration.KUDU_RPC_ADDRESS)
				.option("kudu.table", tableName)
				.option("kudu.socketReadTimeoutMs", "60000")
				.load()
				.where(
					date_format(col("cdt"), "yyyy-MM-dd") === date_sub(current_date(), 1)
				)
		}
	}
	
	/**
	 * 对数据集DataFrame按照业务需求编码，由子类复杂实现
	 * @param dataframe 数据集，表示加载事实表的数据
	 * @return 处理以后数据集
	 */
	def process(dataframe: DataFrame): DataFrame
	
	/**
	 * 将数据集DataFrame保存至Kudu表中，指定表的名
	 * @param dataframe 要保存的数据集
	 *  @param tableName 保存表的名称
	 */
	def saveKuduSink(dataframe: DataFrame, tableName: String,
	                 isAutoCreateTable: Boolean = true): Unit = {
		// 允许自动创建表
		if (isAutoCreateTable) {
			Tools.autoCreateKuduTable(tableName, dataframe)
		}
		
		//将数据写入到kudu中
		dataframe.write
			.mode(SaveMode.Append)
			.format(Configuration.SPARK_KUDU_FORMAT)
			.option("kudu.master", Configuration.KUDU_RPC_ADDRESS)
			.option("kudu.table", tableName)
			.save()
	}
	
	/**
	 * 程序结束，关闭资源，SparkSession关闭
	 */
	def close(): Unit = {
		if(null != spark) spark.stop()
	}
	
	/**
	 * 模板方法，确定基本方法执行顺序
	 */
	def execute(clazz: Class[_], srcTable: String, dstTable: String,
	             isLoadFullData: Boolean = false, isAutoCreateTable: Boolean = true): Unit = {
		try{
			// step1. 初始化
			init(clazz)
			
			// step2. 从Kudu加载数据
			val kuduDF: DataFrame = loadKuduSource(srcTable, isLoadFullData)
			kuduDF.persist(StorageLevel.MEMORY_AND_DISK) // 缓存业务数据
			
			// step3. 数据转换处理操作
			val resultDF: DataFrame = process(kuduDF)
			
			// step4. 数据保存
			saveKuduSink(resultDF, dstTable, isAutoCreateTable)
			kuduDF.unpersist() // 释放资源
		}catch {
			case e: Exception => e.printStackTrace()
		}finally {
			// step5. 关闭资源
			close()
		}
	}
	
}
