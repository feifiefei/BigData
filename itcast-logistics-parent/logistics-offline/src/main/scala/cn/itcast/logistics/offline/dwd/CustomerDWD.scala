package cn.itcast.logistics.offline.dwd

import cn.itcast.logistics.common.{CodeTypeMapping, Configuration, OfflineTableDefine, TableMapping}
import cn.itcast.logistics.offline.AbstractOfflineApplication
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.IntegerType

/**
 * 客户主题指标统计：
 *      从Kudu表加载客户数据和相关维度数据，进行拉链JOIN为宽表，存储至Kudu中DWD层。
 */
object CustomerDWD extends AbstractOfflineApplication{
	/**
	 * 对数据集DataFrame按照业务需求编码，由子类复杂实现
	 *
	 * @param dataframe 数据集，表示加载事实表的数据
	 * @return 处理以后数据集
	 */
	override def process(dataframe: DataFrame): DataFrame = {
		// 导入隐式转换
		import dataframe.sparkSession.implicits._
		
		// 此时dataframe为从Kudu中ODS层加载数据：tbl_customer
		// step1. 加载维度表数据
		val customerSenderInfoDF: DataFrame = loadKuduSource(
			TableMapping.CONSUMER_SENDER_INFO, isLoadFullData = Configuration.IS_FIRST_RUNNABLE
		)
		val expressPackageDF: DataFrame = loadKuduSource(TableMapping.EXPRESS_PACKAGE, isLoadFullData = true)
		val codesDF: DataFrame = loadKuduSource(TableMapping.CODES, isLoadFullData = true)
		val customerTypeDF : DataFrame= codesDF.where($"type" === CodeTypeMapping.CUSTOM_TYPE)
		
		// step2. 将事实表与维度表关联
		val joinType: String = "left_outer"
		// 获取每个用户的首尾单发货信息及发货件数和总金额
		val customerSenderDetailInfoDF: DataFrame = customerSenderInfoDF
			.join(expressPackageDF, expressPackageDF("id") === customerSenderInfoDF("pkgId"), joinType)
			.groupBy(customerSenderInfoDF("ciid"))
			.agg(
				min(customerSenderInfoDF("id")).alias("first_id"), //
				max(customerSenderInfoDF("id")).alias("last_id"),  //
				min(expressPackageDF("cdt")).alias("first_cdt"),  //
				max(expressPackageDF("cdt")).alias("last_cdt"),  //
				count(customerSenderInfoDF("id")).alias("totalCount"),  //
				sum(expressPackageDF("actualAmount")).alias("totalAmount")  //
			)
		val customerDF: DataFrame = dataframe
		val customerDetailDF: DataFrame = customerDF
			.join(customerSenderDetailInfoDF, customerDF("id") === customerSenderInfoDF("ciid"), joinType)
			.join(customerTypeDF, customerDF("type") === customerTypeDF("code").cast(IntegerType), joinType)
			.sort(customerDF("cdt").asc)
			.select(
				customerDF("id"),
				customerDF("name"),
				customerDF("tel"),
				customerDF("mobile"),
				customerDF("type").cast(IntegerType),
				customerTypeDF("codeDesc").as("type_name"),
				customerDF("isownreg").as("is_own_reg"),
				customerDF("regdt").as("regdt"),
				customerDF("regchannelid").as("reg_channel_id"),
				customerDF("state"),
				customerDF("cdt"),
				customerDF("udt"),
				customerDF("lastlogindt").as("last_login_dt"),
				customerDF("remark"),
				customerSenderDetailInfoDF("first_id").as("first_sender_id"), //首次寄件id
				customerSenderDetailInfoDF("last_id").as("last_sender_id"), //尾次寄件id
				customerSenderDetailInfoDF("first_cdt").as("first_sender_cdt"), //首次寄件时间
				customerSenderDetailInfoDF("last_cdt").as("last_sender_cdt"), //尾次寄件时间
				customerSenderDetailInfoDF("totalCount"), //寄件总次数
				customerSenderDetailInfoDF("totalAmount") //总金额
			)
		customerDetailDF.show(10, truncate = false)
		
		// step3. 返回宽表进行保存
		customerDetailDF
	}
	
	def main(args: Array[String]): Unit = {
		// 直接调用基类中模板方法，传递相应的参数
		execute(
			this.getClass, TableMapping.CUSTOMER, //
			OfflineTableDefine.CUSTOMER_DETAIL, isLoadFullData = true
		)
	}
}
