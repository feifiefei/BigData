package cn.itcast.logistics.offline.dwd

import cn.itcast.logistics.common.{CodeTypeMapping, Configuration, OfflineTableDefine, TableMapping}
import cn.itcast.logistics.offline.AbstractOfflineApplication
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.storage.StorageLevel

/**
 * 车辆主题：
 *      仓库车辆拉宽开发，将仓库车辆事实表与相关的维度表进行JOIN关联拉宽，存储Kudu的DWD层详细表。
 */
object WarehouseTransportToolDWD extends AbstractOfflineApplication{
	/**
	 * 对数据集DataFrame按照业务需求编码，由子类复杂实现
	 *
	 * @param dataframe 数据集，表示加载事实表的数据
	 * @return 处理以后数据集
	 */
	override def process(dataframe: DataFrame): DataFrame = {
		import dataframe.sparkSession.implicits._
		
		// 此处dataframe是从Kudu中加载tbl_warehouse_transport_tool表数据
		// step1. 加载维度表的数据
		//加载车辆表数据（事实表）
		val ttDF: DataFrame = loadKuduSource(TableMapping.TRANSPORT_TOOL, isLoadFullData = true)
			.persist(StorageLevel.DISK_ONLY_2)
		//加载公司表的数据
		val companyDF: DataFrame = loadKuduSource(TableMapping.COMPANY, isLoadFullData = true)
		//加载仓库公司关联表
		val companyWareHouseMapDF: DataFrame = loadKuduSource(TableMapping.COMPANY_WAREHOUSE_MAP, isLoadFullData = true)
		//加载仓库表数据
		val wsDF: DataFrame = loadKuduSource(TableMapping.WAREHOUSE, isLoadFullData = true)
		//加载物流码表数据
		val codesDF: DataFrame = loadKuduSource(TableMapping.CODES, isLoadFullData = true)
		//获取运输工具类型
		val transportTypeDF: DataFrame = codesDF
			.where($"type" === CodeTypeMapping.TRANSPORT_TYPE)
			.select($"code".as("ttType"), $"codeDesc".as("ttTypeName"))
		//获取运输工具状态
		val transportStatusDF: DataFrame = codesDF
			.where($"type" === CodeTypeMapping.TRANSPORT_STATUS)
			.select($"code".as("ttStatus"), $"codeDesc".as("ttStateName"))
		
		// step2. 将事实表与维度表数据进行关联JOIN
		val joinType: String = "left_outer"
		val ttWsDF: DataFrame = dataframe
		val ttWsDetailDF = ttWsDF
			.join(
				ttDF, ttWsDF.col("transportToolId") === ttDF.col("id"), joinType
			) //仓库车辆表关联车辆表
			.join(transportTypeDF, transportTypeDF("ttType") === ttDF("type"), joinType) //车辆表类型关联字典表类型
			.join(transportStatusDF, transportStatusDF("ttStatus") === ttDF("state"), joinType) //车辆表状态管理字典表状态
			.join(wsDF, wsDF.col("id") === ttWsDF.col("warehouseId"), joinType) //仓库车辆表关联仓库
			.join(
				companyWareHouseMapDF,
				ttWsDF.col("warehouseId") === companyWareHouseMapDF.col("warehouseId"),
				joinType
			) //仓库车辆管连仓库公司关联表
			.join(
				companyDF,
				companyDF.col("id") === companyWareHouseMapDF.col("companyId"),
				joinType
			)
			.withColumn("day", date_format(ttWsDF("cdt"), "yyyyMMdd"))//虚拟列,可以根据这个日期列作为分区字段，可以保证同一天的数据保存在同一个分区中
			.sort(ttDF.col("cdt").asc)
			.select(
				ttDF("id"), //车辆表id
				ttDF("brand"), //车辆表brand
				ttDF("model"), //车辆表model
				ttDF("type").cast(IntegerType), //车辆表type
				transportTypeDF("ttTypeName").as("type_name"), // 车辆表type对应字典表车辆类型的具体描述
				ttDF("givenLoad").as("given_load").cast(IntegerType), //车辆表given_load
				ttDF("loadCnUnit").as("load_cn_unit"), //车辆表load_cn_unit
				ttDF("loadEnUnit").as("load_en_unit"), //车辆表load_en_unit
				ttDF("buyDt").as("buy_dt"), //车辆表buy_dt
				ttDF("licensePlate").as("license_plate"), //车辆表license_plate
				ttDF("state").cast(IntegerType), //车辆表state
				transportStatusDF("ttStateName").as("state_name"), // 车辆表type对应字典表车辆类型的具体描述
				ttDF("cdt"), //车辆表cdt
				ttDF("udt"), //车辆表udt
				ttDF("remark"), //车辆表remark
				wsDF("id").as("ws_id"), //仓库表id
				wsDF("name"), //仓库表name
				wsDF("addr"), //仓库表addr
				wsDF("addrGis").as("addr_gis"), //仓库表addr_gis
				wsDF("employeeId").as("employee_id"), //仓库表employee_id
				wsDF("type").as("ws_type").cast(IntegerType), //仓库表type
				wsDF("area"), //仓库表area
				wsDF("isLease").as("is_lease").cast(IntegerType), //仓库表is_lease
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
		ttWsDetailDF.show(10, truncate = false)
		
		// step3. 返回拉链宽表数据
		ttWsDetailDF
	}
	
	// 整个SparkSQL应用程序入口，MAIN方法
	def main(args: Array[String]): Unit = {
		// 调用基类中模板方法，传递参数即可
		execute(
			this.getClass, TableMapping.WAREHOUSE_TRANSPORT_TOOL, //
			OfflineTableDefine.WAREHOUSE_TRANSPORT_TOOL_DETAIL, //
			isLoadFullData = Configuration.IS_FIRST_RUNNABLE //
		)
	}
}
