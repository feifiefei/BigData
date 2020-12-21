package cn.itcast.logistics.offline.dwd

import cn.itcast.logistics.common.{CodeTypeMapping, Configuration, OfflineTableDefine, TableMapping}
import cn.itcast.logistics.offline.AbstractOfflineApplication
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.IntegerType

/**
 * 仓库主题宽表：
 *      从ODS层加载事实表（运输记录表）和相关维度表数据，进行JOIN关联拉链为宽表
 */
object WarehouseDWD extends AbstractOfflineApplication{
	/**
	 * 对数据集DataFrame按照业务需求编码，由子类复杂实现
	 *
	 * @param dataframe 数据集，表示加载事实表的数据
	 * @return 处理以后数据集
	 */
	override def process(dataframe: DataFrame): DataFrame = {
		import dataframe.sparkSession.implicits._
		
		// step1. 加载维度表数据
		// 加载公司仓库关联表的数据
		val companyWareHouseMapDF: DataFrame = loadKuduSource(TableMapping.COMPANY_WAREHOUSE_MAP, isLoadFullData = true)
		// 加载公司表的数据
		val companyDF: DataFrame = loadKuduSource(TableMapping.COMPANY, isLoadFullData = true)
		// 加载区域表的数据
		val areasDF: DataFrame = loadKuduSource(TableMapping.AREAS, isLoadFullData = true)
		// 加载运单表的数据
		val wayBillDF: DataFrame = loadKuduSource( TableMapping.WAY_BILL, isLoadFullData = true)
		// 加载快递单表的数据
		val expressBillDF: DataFrame = loadKuduSource(TableMapping.EXPRESS_BILL, isLoadFullData = true)
		// 加载客户寄件信息表数据
		val senderInfoDF: DataFrame = loadKuduSource(TableMapping.CONSUMER_SENDER_INFO, isLoadFullData = true)
		// 加载包裹表数据
		val expressPackageDF: DataFrame = loadKuduSource(TableMapping.EXPRESS_PACKAGE, isLoadFullData = true)
		// 加载客户表数据
		val customerDF: DataFrame = loadKuduSource(TableMapping.CUSTOMER, isLoadFullData = true)
		// 加载物流码表数据
		val codesDF: DataFrame = loadKuduSource(TableMapping.CODES, isLoadFullData = true)
		// 加载仓库表数据
		val warehouseDF: DataFrame = loadKuduSource(TableMapping.WAREHOUSE, isLoadFullData = true)
		// 加载入库数据
		val phWarehouseDF: DataFrame = loadKuduSource(TableMapping.PUSH_WAREHOUSE, isLoadFullData = true)
		// 加载入库数据
		val dotDF: DataFrame = loadKuduSource(TableMapping.DOT, isLoadFullData = true)
		// 客户类型表
		val customerTypeDF: DataFrame = codesDF
			.where(col("type") === CodeTypeMapping.CUSTOM_TYPE)
			.select(col("code").as("customerTypeCode"), col("codeDesc").as("customerTypeName"))
		
		// step2. 事实表与维度表进行关联，选取字段，需要添加day字段
		val joinType: String = "left_outer"
		val recordDF: DataFrame = dataframe
		val recordDetailDF: DataFrame = recordDF
			// 转运记录表与公司仓库关联表关联
			.join(
				companyWareHouseMapDF,
				recordDF.col("swId") === companyWareHouseMapDF.col("warehouseId"),
				joinType
			)
			// 公司仓库关联表与公司表关联
			.join(
				companyDF,
				companyWareHouseMapDF.col("companyId") === companyDF.col("id"),
				joinType
			)
			// 公司表与区域表关联
			.join(areasDF, companyDF.col("cityId") === areasDF.col("id"), joinType)
			// 运单表与转运记录表关联
			.join(
				wayBillDF,
				recordDF.col("pwWaybillNumber") === wayBillDF.col("waybillNumber"),
				joinType)
			//运单表与快递单表关联
			.join(
				expressBillDF,
				wayBillDF.col("expressBillNumber") === expressBillDF.col("expressNumber"),
				joinType
			)
			// 客户寄件信息表与快递单表关联
			.join(
				senderInfoDF,
				expressBillDF.col("cid") === senderInfoDF.col("ciid"),
				joinType
			)
			// 客户寄件信息表与包裹表关联
			.join(
				expressPackageDF,
				senderInfoDF.col("pkgId") === expressPackageDF.col("id"),
				joinType
			)
			// 客户寄件信息表与客户表关联
			.join(
				customerDF,
				senderInfoDF.col("ciid") === customerDF.col("id"),
				joinType
			)
			//客户表与客户类别表关联
			.join(
				customerTypeDF,
				customerDF.col("type") === customerTypeDF.col("customerTypeCode"),
				joinType
			)
			// 转运记录表与仓库表关联
			.join(warehouseDF, recordDF.col("swId")===warehouseDF.col("id"), joinType)
			// 入库表与仓库表关联
			.join(
				phWarehouseDF,
				phWarehouseDF.col("warehouseId")=== warehouseDF.col("id"),
				joinType
			)
			// 转运记录表与网点表关联
			.join(dotDF, dotDF("id")=== phWarehouseDF.col("pwDotId"), joinType)
			// 虚拟列,可以根据这个日期列作为分区字段，可以保证同一天的数据保存在同一个分区中
			.withColumn("day", date_format(recordDF("cdt"), "yyyyMMdd"))
			.sort(recordDF.col("cdt").asc)
			.select(
				recordDF("id"),         //转运记录id
				recordDF("pwId").as("pw_id"),   //入库表的id
				recordDF("pwWaybillId").as("pw_waybill_id"),    //入库运单id
				recordDF("pwWaybillNumber").as("pw_waybill_number"),  //入库运单编号
				recordDF("owId").as("ow_id"),       //出库id
				recordDF("owWaybillId").as("ow_waybill_id"),  //出库运单id
				recordDF("owWaybillNumber").as("ow_waybill_number"),  //出库运单编号
				recordDF("swId").as("sw_id"),     //起点仓库id
				warehouseDF.col("name").as("sw_name"),  //起点仓库名称
				recordDF("ewId").as("ew_id"),    //到达仓库id
				recordDF("transportToolId").as("transport_tool_id"),  //运输工具id
				recordDF("pwDriver1Id").as("pw_driver1_id"),    //入库车辆驾驶员
				recordDF("pwDriver2Id").as("pw_driver2_id"),    //入库车辆驾驶员2
				recordDF("pwDriver3Id").as("pw_driver3_id"),    //入库车辆驾驶员3
				recordDF("owDriver1Id").as("ow_driver1_id"),    //出库车辆驾驶员
				recordDF("owDriver2Id").as("ow_driver2_id"),    //出库车辆驾驶员2
				recordDF("owDriver3Id").as("ow_driver3_id"),    //出库车辆驾驶员3
				recordDF("routeId").as("route_id"),  //线路id
				recordDF("distance").cast(IntegerType),   //运输里程
				recordDF("duration").cast(IntegerType),    //运输耗时
				recordDF("state").cast(IntegerType),      //转运状态id
				recordDF("startVehicleDt").as("start_vehicle_dt"),    //发车时间
				recordDF("predictArrivalsDt").as("predict_arrivals_dt"), //预计到达时间
				recordDF("actualArrivalsDt").as("actual_arrivals_dt"),   //实际到达时间
				recordDF("cdt"), //创建时间
				recordDF("udt"), //修改时间
				recordDF("remark"), //备注
				companyDF("id").alias("company_id"), //公司id
				companyDF("companyName").as("company_name"), //公司名称
				areasDF("id").alias("area_id"),   //区域id
				areasDF("name").alias("area_name"), //区域名称
				expressPackageDF.col("id").alias("package_id"), //包裹id
				expressPackageDF.col("name").alias("package_name"), //包裹名称
				customerDF.col("id").alias("cid"),
				customerDF.col("name").alias("cname"),
				customerTypeDF("customerTypeCode").alias("ctype"),
				customerTypeDF("customerTypeName").alias("ctype_name"),
				dotDF("id").as("dot_id"),
				dotDF("dotName").as("dot_name"),
				col("day")
			)
		recordDetailDF.show(10, truncate = false)
		
		// step3. 返回宽表数据，保存至Kudu表中
		recordDetailDF
	}
	
	// SparkSQL程序执行入口
	def main(args: Array[String]): Unit = {
		execute(
			this.getClass, // 获取应用名称
			TableMapping.TRANSPORT_RECORD, // 事实表名称
			OfflineTableDefine.WAREHOUSE_DETAIL, // 宽表名称
			isLoadFullData = Configuration.IS_FIRST_RUNNABLE //
		)
	}
}
