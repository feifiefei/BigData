package cn.itcast.logistics.offline

import cn.itcast.logistics.common.{Configuration, Tools}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * 根据不同的主题开发，定义抽象方法
 *- 1. 数据读取：从Kudu数据库的ODS层读取数据，事实表和维度表
 *- 2. 数据处理：要么是拉链行程宽表，要么是依据业务指标分析得到结果表
 *- 3. 数据保存：将宽表或结果表存储Kudu数据库的DWD层或者DWS层
 */
trait OfflineApp extends Logging{
	
	/**
	 * 读取Kudu表的数据
	 *
	 * @param spark          SparkSession实例对象
	 * @param tableName      表的名
	 * @param isLoadFullData 是否加载全量数据，默认值为false
	 */
	def getKuduSource(spark: SparkSession, tableName: String, isLoadFullData: Boolean = false): DataFrame = {
		if (isLoadFullData) {
			// 加载全部的数据
			spark.read
				.format(Configuration.SPARK_KUDU_FORMAT)
				.option("kudu.master", Configuration.KUDU_RPC_ADDRESS)
				.option("kudu.table", tableName)
				.option("kudu.socketReadTimeoutMs", "60000")
				.load()
		} else {
			//加载增量数据
			spark.read
				.format(Configuration.SPARK_KUDU_FORMAT)
				.option("kudu.master", Configuration.KUDU_RPC_ADDRESS)
				.option("kudu.table", tableName)
				.option("kudu.socketReadTimeoutMs", "60000")
				.load()
				.where(
					// col("cdt")  -> $"dt"
					date_format(col("cdt"), "yyyy-MM-dd") === date_sub(current_date(), 1)
				)
		}
	}
	
	/**
	 * 数据处理
	 */
	def execute(spark: SparkSession): Unit
	
	/**
	 * 数据存储: DWD及DWS层的数据都是需要写入到kudu数据库中，写入逻辑相同
	 * @param dataframe 数据集，主题指标结果数据
	 * @param isAutoCreateTable 是否自动创建表，默认为true，当表不存在时创建表
	 */
	def save(dataframe: DataFrame, tableName: String, isAutoCreateTable: Boolean = true): Unit = {
		//允许自动创建表
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
}
