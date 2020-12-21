package cn.itcast.logistics.offline.dws

import cn.itcast.logistics.common.{Configuration, OfflineTableDefine}
import cn.itcast.logistics.offline.AbstractOfflineApplication
import cn.itcast.logistics.offline.dws.WaybillDWS.spark
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType, LongType, StringType, StructType}

import scala.collection.mutable.ListBuffer

/**
 * 仓库主题报表开发：
 *      从DWD层加载仓库宽表数据，按照具体指标计算，使用不同维度进行分组统计。
 */
object WarehouseDWS extends AbstractOfflineApplication{
	/**
	 * 对数据集DataFrame按照业务需求编码，由子类复杂实现
	 *
	 * @param dataframe 数据集，表示加载宽表的数据
	 * @return 处理以后数据集
	 */
	override def process(dataframe: DataFrame): DataFrame = {
		// 导入隐式转换
		import dataframe.sparkSession.implicits._
		
		// step1. 考虑全量数据，需要按照day进行划分，每天数据进行指标计算
		val rowList: ListBuffer[Row] = ListBuffer[Row]()
		dataframe.select($"day").distinct().collect().foreach{dayRow =>
			// 获取day值
			val day: String = dayRow.getAs[String](0)
			
			// 依据日期获取每天数据
			val warehouseDetailDF: DataFrame = dataframe.filter($"day" === day)
			
			// 指标一：各仓库发车次数，最大、最小和平均
			val whSwTotalDF: DataFrame  = warehouseDetailDF.groupBy($"sw_id").count()
			val whSwTotalAggDF: DataFrame = whSwTotalDF.agg(
				max($"count").as("maxWsTotal"), //
				min($"count").as("minWsTotal"), //
				round(avg($"count").as("avgWsTotal"), 0) //
			)
			
			// 指标二：各网点发车次数，最大、最小和平均
			val whDotTotalDF: DataFrame  = warehouseDetailDF.groupBy($"dot_id").count()
			val whDotTotalAggDF: DataFrame = whDotTotalDF.agg(
				max($"count").as("maxDotTotal"), //
				min($"count").as("minDotTotal"), //
				round(avg($"count").as("avgDotTotal"), 0) //
			)
			
			// 指标三：各线路发车次数，最大、最小和平均
			val whRouteTotalDF: DataFrame  = warehouseDetailDF.groupBy($"route_id").count()
			val whRouteTotalAggDF: DataFrame = whRouteTotalDF.agg(
				max($"count").as("maxRouteTotal"), //
				min($"count").as("minRouteTotal"), //
				round(avg($"count").as("avgRouteTotal"), 0) //
			)
			
			// 指标四：各类型客户发车次数，最大、最小和平均
			val whCtypeTotalDF: DataFrame  = warehouseDetailDF.groupBy($"ctype").count()
			val whCtypeTotalAggDF: DataFrame = whCtypeTotalDF.agg(
				max($"count").as("maxCtypeTotal"), //
				min($"count").as("minCtypeTotal"), //
				round(avg($"count").as("avgCtypeTotal"), 0) //
			)
			
			// 指标五：各类型包裹发车次数，最大、最小和平均
			val whPackageTotalDF: DataFrame  = warehouseDetailDF.groupBy($"package_id").count()
			val whPackageTotalAggDF: DataFrame = whPackageTotalDF.agg(
				max($"count").as("maxPackageTotal"), //
				min($"count").as("minPackageTotal"), //
				round(avg($"count").as("avgPackageTotal"), 0) //
			)
			
			// 指标六：各区域发车次数，最大、最小和平均
			val whAreaTotalDF: DataFrame  = warehouseDetailDF.groupBy($"area_id").count()
			val whAreaTotalAggDF: DataFrame = whAreaTotalDF.agg(
				max($"count").as("maxAreaTotal"), //
				min($"count").as("minAreaTotal"), //
				round(avg($"count").as("avgAreaTotal"), 0) //
			)
			
			// 指标七：各公司发车次数，最大、最小和平均
			val whCompanyTotalDF: DataFrame  = warehouseDetailDF.groupBy($"company_id").count()
			val whCompanyTotalAggDF: DataFrame = whCompanyTotalDF.agg(
				max($"count").as("maxCompanyTotal"), //
				min($"count").as("minCompanyTotal"), //
				round(avg($"count").as("avgCompanyTotal"), 0) //
			)
			
			// 组合指标，存储Row对象中
			val aggRow = Row.fromSeq(
				dayRow.toSeq ++
					whSwTotalAggDF.first().toSeq ++
					whDotTotalAggDF.first().toSeq ++
					whRouteTotalAggDF.first().toSeq ++
					whCtypeTotalAggDF.first().toSeq ++
					whPackageTotalAggDF.first().toSeq ++
					whAreaTotalAggDF.first().toSeq ++
					whCompanyTotalAggDF.first().toSeq
			)
			// 添加到列表
			rowList += aggRow
		}
		
		// 第一步、将列表转换为RDD
		val rowsRDD: RDD[Row] = spark.sparkContext.parallelize(rowList.toList) // 将可变集合对象转换为不可变的
		// 第二步、自定义Schema信息
		val aggSchema: StructType = new StructType()
			.add("id", StringType, nullable = false) // 针对每天数据进行聚合得到一个结果，设置day为结果表中id
			.add("maxSwTotal", LongType, nullable = true)
			.add("minSwTotal", LongType, nullable = true)
			.add("avgSwTotal", DoubleType, nullable = true)
			.add("maxDotTotal", LongType, nullable = true)
			.add("minDotTotal", LongType, nullable = true)
			.add("avgDotTotal", DoubleType, nullable = true)
			.add("maxRouteTotal", LongType, nullable = true)
			.add("minRouteTotal", LongType, nullable = true)
			.add("avgRouteTotal", DoubleType, nullable = true)
			.add("maxCtypeTotal", LongType, nullable = true)
			.add("minCtypeTotal", LongType, nullable = true)
			.add("avgCtypeTotal", DoubleType, nullable = true)
			.add("maxPackageTotal", LongType, nullable = true)
			.add("minPackageTotal", LongType, nullable = true)
			.add("avgPackageTotal", DoubleType, nullable = true)
			.add("maxAreaTotal", LongType, nullable = true)
			.add("minAreaTotal", LongType, nullable = true)
			.add("avgAreaTotal", DoubleType, nullable = true)
			.add("maxCompanyTotal", LongType, nullable = true)
			.add("minCompanyTotal", LongType, nullable = true)
			.add("avgCompanyTotal", DoubleType, nullable = true)
		
		// 第三步、调用SparkSession中createDataFrame方法，组合RowsRDD和Schema为DataFrame
		val aggDF: DataFrame = spark.createDataFrame(rowsRDD, aggSchema)
		aggDF.show(10, truncate = false)
		
		// 返回计算指标数据
		aggDF
	}
	
	def main(args: Array[String]): Unit = {
		execute(
			this.getClass, //
			OfflineTableDefine.WAREHOUSE_DETAIL, //
			OfflineTableDefine.WAREHOUSE_SUMMARY, //
			isLoadFullData = Configuration.IS_FIRST_RUNNABLE //
		)
	}
}
