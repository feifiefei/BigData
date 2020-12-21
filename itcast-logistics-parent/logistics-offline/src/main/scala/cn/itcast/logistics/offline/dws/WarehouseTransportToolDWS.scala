package cn.itcast.logistics.offline.dws

import cn.itcast.logistics.common.{Configuration, OfflineTableDefine}
import cn.itcast.logistics.offline.AbstractOfflineApplication
import cn.itcast.logistics.offline.dws.DotTransportToolDWS.spark
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.apache.spark.sql.functions.{avg, max, min, round, sum}
import org.apache.spark.sql.types.{DoubleType, LongType, StringType, StructType}

import scala.collection.mutable.ListBuffer

/**
 * 仓库车辆相关指标聚合统计：
 *      从Kudu表加载仓库车辆详细宽表数据，按照不同维度进行分组，计算相关指标。
 */
object WarehouseTransportToolDWS extends AbstractOfflineApplication{
	/**
	 * 对数据集DataFrame按照业务需求编码，由子类复杂实现
	 *
	 * @param dataframe 数据集，表示加载宽表的数据
	 * @return 处理以后数据集
	 */
	override def process(dataframe: DataFrame): DataFrame = {
		// 导入隐式转换
		import dataframe.sparkSession.implicits._
		
		// dataframe 表示从Kudu表加载宽表数据：tbl_warehouse_transport_tool_detail
		val rowList: ListBuffer[Row] = ListBuffer[Row]()
		dataframe.select($"day").collect().foreach{dayRow =>
			// 获取具体日期day
			val day: String = dayRow.getAs[String](0)
			// 依据day过滤出对应宽表数据
			val ttWsDetailDF: Dataset[Row] = dataframe.filter($"day" === day)
			
			// 指标计算
			// 指标一：各网点发车次数及最大、最小和平均
			val ttWsTotalDF: DataFrame = ttWsDetailDF.groupBy($"ws_id").count()
			val ttWsTotalAggDF: DataFrame = ttWsTotalDF.agg(
				sum($"count").as("sumDotTotal"),  //
				max($"count").as("maxDotTotal"),  //
				min($"count").as("minDotTotal"), //
				round(avg($"count"), 0).as("avgDotTotal") //
			)
			// 指标二：各区域发车次数及最大、最小和平均
			val ttCityTotalDF: DataFrame = ttWsDetailDF.groupBy($"city_id").count()
			val ttCityTotalAggDF: DataFrame = ttCityTotalDF.agg(
				sum($"count").as("sumCityTotal"),  //
				max($"count").as("maxCityTotal"),  //
				min($"count").as("minCityTotal"), //
				round(avg($"count"), 0).as("avgCityTotal") //
			)
			// 指标三：各公司发车次数及最大、最小和平均
			val ttCompanyTotalDF: DataFrame = ttWsDetailDF.groupBy($"company_id").count()
			val ttCompanyTotalAggDF: DataFrame = ttCompanyTotalDF.agg(
				sum($"count").as("sumCompanyTotal"),  //
				max($"count").as("maxCompanyTotal"),  //
				min($"count").as("minCompanyTotal"), //
				round(avg($"count"), 0).as("avgCompanyTotal") //
			)
			
			// TODO： 需要将计算所有指标结果提取出来，并且组合到Row对象中
			val aggRow: Row = Row.fromSeq(
				dayRow.toSeq ++ //
					ttWsTotalAggDF.first().toSeq ++  //
					ttCityTotalAggDF.first().toSeq ++  //
					ttCompanyTotalAggDF.first().toSeq  //
			)
			// 将每天聚合计算结果加入列表中
			rowList += aggRow
		}
		
		// 第一步、将列表转换为RDD
		val rowsRDD: RDD[Row] = spark.sparkContext.parallelize(rowList.toList) // 将可变集合对象转换为不可变的
		// 第二步、自定义Schema信息
		val aggSchema: StructType = new StructType()
			.add("id", StringType, nullable = false) // 针对每天数据进行聚合得到一个结果，设置day为结果表中id
			.add("sumWsTotal", LongType, nullable = true)
			.add("maxWsTotal", LongType, nullable = true)
			.add("minWsTotal", LongType, nullable = true)
			.add("avgWsTotal", DoubleType, nullable = true)
			.add("sumCityTotal", LongType, nullable = true)
			.add("maxCityTotal", LongType, nullable = true)
			.add("minCityTotal", LongType, nullable = true)
			.add("avgCityTotal", DoubleType, nullable = true)
			.add("sumCompanyTotal", LongType, nullable = true)
			.add("maxCompanyTotal", LongType, nullable = true)
			.add("minCompanyTotal", LongType, nullable = true)
			.add("avgCompanyTotal", DoubleType, nullable = true)
		
		// 第三步、调用SparkSession中createDataFrame方法，组合RowsRDD和Schema为DataFrame
		val aggDF: DataFrame = spark.createDataFrame(rowsRDD, aggSchema)
		aggDF.show(10, truncate = false)
		
		// 返回聚合数据
		aggDF
	}
	
	def main(args: Array[String]): Unit = {
		// 调用execute方法，传递Kudu表名称及是否第一次运行
		execute(
			this.getClass, OfflineTableDefine.WAREHOUSE_TRANSPORT_TOOL_DETAIL, //
			OfflineTableDefine.WAREHOUSE_TRANSPORT_TOOL_SUMMARY, //
			isLoadFullData = Configuration.IS_FIRST_RUNNABLE //
		)
	}
}
