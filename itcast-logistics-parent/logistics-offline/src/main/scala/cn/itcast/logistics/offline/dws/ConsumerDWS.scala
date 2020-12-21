package cn.itcast.logistics.offline.dws

import cn.itcast.logistics.common.{OfflineTableDefine, TableMapping}
import cn.itcast.logistics.offline.AbstractOfflineApplication
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DataType, DataTypes, DoubleType, LongType, StringType, StructType}

/**
 * 客户主题指标（标签）开发：
 *      从Kudu中DWD层加载客户宽表数据，依据指标进行统计计算，最终存储至Kudu表中。
 */
object ConsumerDWS extends AbstractOfflineApplication{
	/**
	 * 对数据集DataFrame按照业务需求编码，由子类复杂实现
	 *
	 * @param dataframe 数据集，表示加载宽表的数据
	 * @return 处理以后数据集
	 */
	override def process(dataframe: DataFrame): DataFrame = {
		import dataframe.sparkSession.implicits._
		
		/*
			客户主题来说：主表【tbl_customer_detail】，针对客户指标最终统计出来结果1数据，每日统计
		 */
		// 指标1：主键id（数据产生时间）
		val dayValue: String = dataframe
			.select(
				date_format(date_sub(current_date(), 1), "yyyyMMdd").cast(StringType).as("day")
			)
    		.limit(1)
			.first().getAs[String]("day")
		// 指标2: 总客户数
		val totalCount: Long = dataframe.count()
		// 指标3：今日新增客户数(注册时间为今天) TODO: 计算昨天的数据
		val additionTotalCount: Long = dataframe
			.filter(
				date_format($"regdt", "yyyy-MM-dd") === date_sub(current_date(), 1)
			)
			.count()
		// 指标4：留存数(超过180天未下单表示已流失，否则表示留存)和留存
		/*
			用户最后一次下单时间 距今天数 <= 180 ，表示此用户为留存用户
		 */
		val preserveTotalCount: Long = dataframe
			// .where($"last_sender_cdt" >= date_sub(current_date(), 180))
			.filter(
				datediff(date_sub(current_date(), 1), $"last_sender_cdt") <= 180
			)
    		.count()
		val preserveTotalRate: Double = preserveTotalCount / totalCount.toDouble
		// 指标5：活跃用户数(近10天内有发件的客户表示活跃用户)
		val activeTotalCount: Long = dataframe
    		.where($"last_sender_cdt" >= date_sub(current_date(), 10) )
    		.count()
		// 指标6： 月度新用户
		val monthOfNewCount: Long = dataframe
			/*
    		.where(
			    date_format($"regdt", "yyyy-MM") === date_format(date_sub(current_date(), 1), "yyyy-MM")
		    )
		    */
			.filter( // 2020-12-10 18:54:00  在  2020-12-01   和 2020-12-31
				$"regdt".between(
					trunc($"regdt", "mm"), date_format(date_sub(current_date(), 1), "yyyy-MM-dd")
				)
			)
    		.count()
		// 指标7： 沉睡用户数(3个月~6个月之间的用户表示已沉睡)
		val sleepCount = dataframe
			.where(
				"last_sender_cdt >= date_sub(now(), 90) and last_sender_cdt <= date_sub(now(), 180)"
			)
			.count()
		// 指标8： 流失用户数(9个月未下单表示已流失)
		val loseCustomerCount: Long = dataframe
			.where("last_sender_cdt >= date_sub(now(), 270)")
			.count()
		// 指标9： 客单数
		val aggTotalDF: DataFrame = dataframe.agg(
			sum($"totalcount").as("sumCount"), // 所有客户订单数
			sum($"totalamount".cast(DataTypes.createDecimalType())).as("sumAmount") // 所有订单金额之和
		)
		val orderTotalCount: Long = aggTotalDF.first().getAs[Long]("sumCount")
		// 客单价 =  总金额 / 总单数（客单数）
		val orderAvgAmount: Double = aggTotalDF.first().get(1).toString.toDouble / orderTotalCount
		// 平均客单数
		val orderAvgCount: Double = orderTotalCount / totalCount.toDouble
		
		val aggRow: Row = Row(
			dayValue, //
			totalCount, //
			additionTotalCount, //
			preserveTotalRate, //
			activeTotalCount, //
			monthOfNewCount, //
			sleepCount, //
			loseCustomerCount, //
			orderTotalCount, //
			orderAvgAmount, //
			orderAvgCount
		)
		
		// 第一步、将列表转换为RDD
		val rowsRDD: RDD[Row] = spark.sparkContext.parallelize(Seq(aggRow)) // 将可变集合对象转换为不可变的
		// 第二步、自定义Schema信息
		val aggSchema: StructType = new StructType()
			.add("id", StringType, nullable = false) // 针对每天数据进行聚合得到一个结果，设置day为结果表中id
			.add("customerCount", LongType, nullable = true)
			.add("additionCustomerCount", LongType, nullable = true)
			.add("preserveRate", DoubleType, nullable = true)
			.add("activeCustomerCount", LongType, nullable = true)
			.add("monthOfNewCustomerCount", LongType, nullable = true)
			.add("sleepCustomerCount", LongType, nullable = true)
			.add("loseCustomerCount", LongType, nullable = true)
			.add("customerBillCount", LongType, nullable = true)
			.add("customerAvgAmount", DoubleType, nullable = true)
			.add("avgCustomerBillCount", DoubleType, nullable = true)
		
		// 第三步、调用SparkSession中createDataFrame方法，组合RowsRDD和Schema为DataFrame
		val aggDF: DataFrame = spark.createDataFrame(rowsRDD, aggSchema)
		aggDF.show(10, truncate = false)
		
		// 返回聚合数据
		aggDF
	}
	
	def main(args: Array[String]): Unit = {
		// 调用基类中模板方法，传递参数
		execute(
			this.getClass, OfflineTableDefine.CUSTOMER_DETAIL, //
			OfflineTableDefine.CUSTOMER_SUMMERY, isLoadFullData = true //
		)
	}
}
