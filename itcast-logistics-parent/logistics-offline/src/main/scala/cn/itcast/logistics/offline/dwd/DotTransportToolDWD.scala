package cn.itcast.logistics.offline.dwd

import cn.itcast.logistics.common.{CodeTypeMapping, Configuration, OfflineTableDefine, TableMapping}
import cn.itcast.logistics.offline.AbstractOfflineApplication
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.IntegerType

/**
 * 车辆主题：
 *      网点车辆拉宽开发，将网点车辆事实表与相关的维度表进行JOIN关联拉宽，存储Kudu的DWD层详细表。
 */
object DotTransportToolDWD extends AbstractOfflineApplication{
	/**
	 * 对数据集DataFrame按照业务需求编码，由子类复杂实现
	 *
	 * @param dataframe 数据集，表示加载事实表的数据
	 * @return 处理以后数据集
	 */
	override def process(dataframe: DataFrame): DataFrame = {
		import dataframe.sparkSession.implicits._
		
		// 此处dataframe是从Kudu中加载tbl_dot_transport_tool表数据
		// step1. 加载维度表数据，全量加载
		// 加载仓库车辆表数据
		val ttDotDF: DataFrame = loadKuduSource(TableMapping.DOT_TRANSPORT_TOOL, isLoadFullData = true)
		// 加载网点表的数据
		val dotDF: DataFrame = loadKuduSource(TableMapping.DOT, isLoadFullData = true)
		// 加载公司网点关联表的数据
		val companyDotDF: DataFrame = loadKuduSource(TableMapping.COMPANY_DOT_MAP, isLoadFullData = true)
		// 加载公司表的数据
		val companyDF: DataFrame = loadKuduSource(TableMapping.COMPANY, isLoadFullData = true)
		// 加载物流码表数据
		val codesDF: DataFrame = loadKuduSource(TableMapping.CODES, isLoadFullData = true)
		// 获取运输工具类型
		val transportTypeDF: DataFrame = codesDF
			.where($"type" === CodeTypeMapping.TRANSPORT_TYPE)
			.select($"code".as("ttType"), $"codeDesc".as("ttTypeName"))
		// 获取运输工具状态
		val transportStatusDF: DataFrame = codesDF
			.where($"type" === CodeTypeMapping.TRANSPORT_STATUS)
			.select($"code".as("ttStatus"), $"codeDesc".as("ttStateName"))
		
		// step2. 事实表与维度表，采用leftJoin方法关联，选择字段信息
		val joinType: String = "left_outer"
		val ttDF: DataFrame = dataframe
		val ttDotDetailDF = ttDotDF
			// 网点车辆表关联车辆表
			.join(
				ttDF,
				ttDotDF.col("transportToolId") === ttDF.col("id"),
				joinType
			)
			// 车辆表类型关联字典表类型
			.join(
				transportTypeDF, transportTypeDF("ttType") === ttDF("type"), joinType
			)
			// 车辆表状态管理字典表状态
			.join(
				transportStatusDF, transportStatusDF("ttStatus") === ttDF("state"), joinType
			)
			// 网点车辆表关联网点
			.join(
				dotDF, dotDF.col("id") === ttDotDF.col("dotId"), joinType)
			// 网点车辆管连网点公司关联表
			.join(
				companyDotDF, ttDotDF.col("dotId") === companyDotDF.col("dotId"), joinType
			)
			// 网点车辆表关联公司表
			.join(
				companyDF, companyDotDF.col("companyId") === companyDF.col("id"), joinType
			)
			// 虚拟列,可以根据这个日期列作为分区字段，可以保证同一天的数据保存在同一个分区中
			.withColumn("day", date_format(ttDotDF("cdt"), "yyyyMMdd"))
			.sort(ttDF.col("cdt").asc)
			.select(
				ttDF("id"), //车辆表id
				ttDF("brand"), //车辆表brand
				ttDF("model"), //车辆表model
				ttDF("type").cast(IntegerType), //车辆表type
				transportTypeDF("ttTypeName").as("type_name"), // 车辆表type对应字典表车辆类型的具体描述
				ttDF("givenLoad").cast(IntegerType).as("given_load"), //车辆表given_load
				ttDF("loadCnUnit").as("load_cn_unit"), //车辆表load_cn_unit
				ttDF("loadEnUnit").as("load_en_unit"), //车辆表load_en_unit
				ttDF("buyDt").as("buy_dt"), //车辆表buy_dt
				ttDF("licensePlate").as("license_plate"), //车辆表license_plate
				ttDF("state").cast(IntegerType), //车辆表state
				transportStatusDF("ttStateName").as("state_name"), // 车辆表state对应字典表类型的具体描述
				ttDF("cdt"), //车辆表cdt
				ttDF("udt"), //车辆表udt
				ttDF("remark"), //车辆表remark
				dotDF("id").as("dot_id"), //网点表dot_id
				dotDF("dotNumber").as("dot_number"), //网点表dot_number
				dotDF("dotName").as("dot_name"), //网点表dot_name
				dotDF("dotAddr").as("dot_addr"), //网点表dot_addr
				dotDF("dotGisAddr").as("dot_gis_addr"), //网点表dot_gis_addr
				dotDF("dotTel").as("dot_tel"), //网点表dot_tel
				dotDF("manageAreaId").as("manage_area_id"), //网点表manage_area_id
				dotDF("manageAreaGis").as("manage_area_gis"), //网点表manage_area_gis
				companyDF("id").alias("company_id"), //公司表id
				companyDF("companyName").as("company_name"), //公司表company_name
				companyDF("cityId").as("city_id"), //公司表city_id
				companyDF("companyNumber").as("company_number"), //公司表company_number
				companyDF("companyAddr").as("company_addr"), //公司表company_addr
				companyDF("companyAddrGis").as("company_addr_gis"), //公司表company_addr_gis
				companyDF("companyTel").as("company_tel"), //公司表company_tel
				companyDF("isSubCompany").as("is_sub_company"), //公司表is_sub_company
				$"day"
			)
		
		// step3. 返回关联数据即可
		ttDotDetailDF
	}
	
	// 整个SparkSQL应用程序入口，MAIN方法
	def main(args: Array[String]): Unit = {
		// 调用基类中模板方法，传递参数即可
		execute(
			this.getClass, TableMapping.TRANSPORT_TOOL, //
			OfflineTableDefine.DOT_TRANSPORT_TOOL_DETAIL, //
			isLoadFullData = Configuration.IS_FIRST_RUNNABLE //
		)
	}
}
