package cn.itcast.logistics.offline.dws

import cn.itcast.logistics.common.{Configuration, OfflineTableDefine}
import cn.itcast.logistics.offline.AbstractOfflineApplication
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType, LongType, StringType, StructType}
import org.apache.spark.sql.{DataFrame, Row}

import scala.collection.mutable.ListBuffer

/**
 * 运单主题指标开发：
 *      从Kudu表加载宽表数据，按照业务指标进行统计分析：基于不同维度分组聚合，类似快递单指表指标。
 */
object WaybillDWS extends AbstractOfflineApplication{
	/**
	 * 对数据集DataFrame按照业务需求编码，由子类复杂实现
	 *
	 * @param dataframe 数据集，表示加载宽表的数据
	 * @return 处理以后数据集
	 */
	override def process(dataframe: DataFrame): DataFrame = {
		// 导入隐式转换
		import dataframe.sparkSession.implicits._
		
		// 由于运单宽表数据被使用多次，进行缓存
		// dataframe.persist(StorageLevel.MEMORY_AND_DISK)
		
		/*
		 * 考虑：获取宽表数据是否是全量数据，如果是全量数据，需要按照day划分，进行每天数据统计分析
		 */
		val rowList: ListBuffer[Row] = ListBuffer[Row]()
		dataframe.select($"day").distinct().collect().foreach{dayRow =>
			val day: String = dayRow.getAs[String](0)
			// 获取每天运单数据
			val wayBillDetailDF: DataFrame = dataframe.filter($"day".equalTo(day))
			
			// 指标一：总运单数
			val wayBillTotalDF: DataFrame = wayBillDetailDF.agg(count($"id").as("total"))
			
			// 指标二：各个区域运单数，最大、最小和平均
			val wayBillAreaTotalDF: DataFrame = wayBillDetailDF.groupBy($"area_id").count()
			val wayBillAreaTotalAggDF: DataFrame = wayBillAreaTotalDF.agg(
				max($"count").as("maxAreaTotal"),  //
				min($"count").as("minAreaTotal"), //
				round(avg($"count"), 0).as("avgAreaTotal") //
			)
			
			// 指标三：各分公司运单数，最大、最小和平均
			val wayBillCompanyTotalDF: DataFrame = wayBillDetailDF.groupBy($"sw_company_name").count()
			val wayBillCompanyTotalAggDF: DataFrame = wayBillCompanyTotalDF.agg(
				max($"count").as("maxCompanyTotal"),  //
				min($"count").as("minCompanyTotal"), //
				round(avg($"count"), 0).as("avgCompanyTotal") //
			)
			
			// 指标四：各网点运单数，最大、最小和平均
			val wayBillDotTotalDF: DataFrame = wayBillDetailDF.groupBy($"dot_id").count()
			val wayBillDotTotalAggDF: DataFrame = wayBillDotTotalDF.agg(
				max($"count").as("maxDotTotal"),  //
				min($"count").as("minDotTotal"), //
				round(avg($"count"), 0).as("avgDotTotal") //
			)
			
			// 指标五：各线路运单数，最大、最小和平均
			val wayBillRouteTotalDF: DataFrame = wayBillDetailDF.groupBy($"route_id").count()
			val wayBillRouteTotalAggDF: DataFrame = wayBillRouteTotalDF.agg(
				max($"count").as("maxRouteTotal"),  //
				min($"count").as("minRouteTotal"), //
				round(avg($"count"), 0).as("avgRouteTotal") //
			)
			
			// 指标六：各运输工具运单数，最大、最小和平均
			/*
				WITH tmp AS(
				  SELECT tt_id, count(1) AS total FROM way_bill_dwd GROUP BY tt_id
				)
				SELECT max(t.total) AS max_total, min(t.total) AS min_total, AVG(t.total) AS avg_total FROM tmp t
			 */
			val wayBillToolTotalDF: DataFrame = wayBillDetailDF.groupBy($"tt_id").count()
			val wayBillToolTotalAggDF: DataFrame = wayBillToolTotalDF.agg(
				max($"count").as("maxToolTotal"),  //
				min($"count").as("minToolTotal"), //
				round(avg($"count"), 0).as("avgToolTotal") //
			)
			
			// 指标七：各类客户运单数，最大、最小和平均
			val wayBillCtypeTotalDF: DataFrame = wayBillDetailDF.groupBy($"ctype").count()
			val wayBillCtypeTotalAggDF: DataFrame = wayBillCtypeTotalDF.agg(
				max($"count").as("maxCtypeTotal"),  //
				min($"count").as("minCtypeTotal"), //
				round(avg($"count"), 0).as("avgCtypeTotal") //
			)
			
			// TODO： 需要将计算所有指标结果提取出来，并且组合到Row对象中
			val aggRow: Row = Row.fromSeq(
				dayRow.toSeq ++
					wayBillTotalDF.first().toSeq ++
					wayBillAreaTotalAggDF.first().toSeq ++
					wayBillCompanyTotalAggDF.first().toSeq ++
					wayBillDotTotalAggDF.first().toSeq ++
					wayBillRouteTotalAggDF.first().toSeq ++
					wayBillToolTotalAggDF.first().toSeq ++
					wayBillCtypeTotalAggDF.first().toSeq
			)
			// 将每天聚合计算结果加入列表中
			rowList += aggRow
		}
		
		// 第一步、将列表转换为RDD
		val rowsRDD: RDD[Row] = spark.sparkContext.parallelize(rowList.toList) // 将可变集合对象转换为不可变的
		// 第二步、自定义Schema信息
		val aggSchema: StructType = new StructType()
			.add("id", StringType, nullable = false) // 针对每天数据进行聚合得到一个结果，设置day为结果表中id
			.add("total", LongType, nullable = true)
			.add("maxAreaTotal", LongType, nullable = true)
			.add("minAreaTotal", LongType, nullable = true)
			.add("avgAreaTotal", DoubleType, nullable = true)
			.add("maxCompanyTotal", LongType, nullable = true)
			.add("minCompanyTotal", LongType, nullable = true)
			.add("avgCompanyTotal", DoubleType, nullable = true)
			.add("maxDotTotal", LongType, nullable = true)
			.add("minDotTotal", LongType, nullable = true)
			.add("avgDotTotal", DoubleType, nullable = true)
			.add("maxRouteTotal", LongType, nullable = true)
			.add("minRouteTotal", LongType, nullable = true)
			.add("avgRouteTotal", DoubleType, nullable = true)
			.add("maxToolTotal", LongType, nullable = true)
			.add("minToolTotal", LongType, nullable = true)
			.add("avgToolTotal", DoubleType, nullable = true)
			.add("maxCtypeTotal", LongType, nullable = true)
			.add("minCtypeTotal", LongType, nullable = true)
			.add("avgCtypeTotal", DoubleType, nullable = true)
		
		// 第三步、调用SparkSession中createDataFrame方法，组合RowsRDD和Schema为DataFrame
		val aggDF: DataFrame = spark.createDataFrame(rowsRDD, aggSchema)
		aggDF.show(10, truncate = false)
	
		// 计算结束释放资源
		// dataframe.unpersist()
		
		// 返回聚合数据
		aggDF
	}
	
	// 运单主题指标程序运行的入口
	def main(args: Array[String]): Unit = {
		execute(
			this.getClass, //
			OfflineTableDefine.WAY_BILL_DETAIL, //
			OfflineTableDefine.WAY_BILL_SUMMARY, //
			Configuration.IS_FIRST_RUNNABLE //
		)
	}
}
