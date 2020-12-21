package cn.itcast.logistics.offline.dwd

import cn.itcast.logistics.common.{CodeTypeMapping, Configuration, OfflineTableDefine, TableMapping}
import cn.itcast.logistics.offline.AbstractOfflineApplication
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

/**
 * 运单主题开发：
 *      将运单事实表的数据与相关维度表的数据进行关联，然后将拉宽后的数据写入到运单宽表中
 */
object WaybillDWD extends AbstractOfflineApplication{
	/**
	 * 对数据集DataFrame按照业务需求编码，由子类复杂实现
	 *
	 * @param dataframe 数据集，表示加载事实表的数据
	 * @return 处理以后数据集
	 */
	override def process(dataframe: DataFrame): DataFrame = {
		/**
		 * 针对离线报表业务【对事实表进行拉链操作】开发，步骤如下：
		 * 1). 加载维度表的数据
		 * 2). 事实表与维度表进行关联JOIN，使用leftJoin
		 * 3). 添加字段，日期字段day
		 */
		// 1). 加载维度表的数据
		// 加载快递员表
		val courierDF: DataFrame = loadKuduSource(TableMapping.COURIER, isLoadFullData = true)
		// 加载网点表
		val dotDF: DataFrame =  loadKuduSource(TableMapping.DOT, isLoadFullData = true)
		// 加载区域表
		val areasDF: DataFrame =  loadKuduSource(TableMapping.AREAS, isLoadFullData = true)
		// 加载转运记录表
		val recordDF: DataFrame =  loadKuduSource(TableMapping.TRANSPORT_RECORD, isLoadFullData = true)
		// 加载起始仓库表
		val startWarehouseDF: DataFrame = loadKuduSource(TableMapping.WAREHOUSE, isLoadFullData = true)
		// 加载到达仓库表
		val endWarehouseDF: DataFrame = loadKuduSource(TableMapping.WAREHOUSE, isLoadFullData = true)
		// 加载车辆表
		val toolDF: DataFrame = loadKuduSource(TableMapping.TRANSPORT_TOOL, isLoadFullData = true)
		// 加载线路表
		val routeDF: DataFrame = loadKuduSource(TableMapping.ROUTE, isLoadFullData = true)
		// 加载起始仓库关联表
		val startCompanyWarehouseDF: DataFrame = loadKuduSource(TableMapping.COMPANY_WAREHOUSE_MAP, isLoadFullData = true)
		// 加载到达仓库关联表
		val endCompanyWarehouseDF: DataFrame = loadKuduSource(TableMapping.COMPANY_WAREHOUSE_MAP, isLoadFullData = true)
		// 加载起始仓库所在公司表
		val startCompanyDF: DataFrame = loadKuduSource(TableMapping.COMPANY, isLoadFullData = true)
		// 加载到达仓库所在公司表
		val endCompanyDF: DataFrame = loadKuduSource(TableMapping.COMPANY, isLoadFullData = true)
		// 加载物流码表
		val codesDF: DataFrame = loadKuduSource(TableMapping.CODES, isLoadFullData = true)
		// 加载客户表
		val customerDF: DataFrame = loadKuduSource(TableMapping.CUSTOMER, isLoadFullData = true)
		
		// 下单渠道类型表
		val orderChannelTypeDF: DataFrame = codesDF
			.where(col("type") === CodeTypeMapping.ORDER_CHANNEL_TYPE)
			.select(
				col("code").as("orderChannelTypeCode"), col("codeDesc").as("orderChannelTypeName")
			)
		// 客户类型表
		val customerTypeDF: DataFrame = codesDF
			.where(col("type") === CodeTypeMapping.CUSTOM_TYPE)
			.select(
				col("code").as("customerTypeCode"), col("codeDesc").as("customerTypeName")
			)
		
		// 2). 事实表与维度表进行关联JOIN，使用leftJoin
		val left_outer = "left_outer"
		val wayBillDF: DataFrame = dataframe
		val wayBillDetailDF = wayBillDF
			// 运单表与快递员表进行关联
			.join(courierDF, wayBillDF("eid") === courierDF("id"), left_outer)
			// 网点表与快递员表进行关联
			.join(dotDF, courierDF("dotId") === dotDF("id"), left_outer)
			// 网点表与区域表进行关联
			.join(areasDF, areasDF("id") === dotDF("manageAreaId"), left_outer)
			// 转运记录表与运单表关联
			.join(recordDF, recordDF("pwWaybillNumber") === wayBillDF("waybillNumber"), left_outer)
			// 起始仓库与转运记录表关联
			.join(startWarehouseDF, startWarehouseDF("id") === recordDF("swId"), left_outer)
			// 到达仓库与转运记录表关联
			.join(endWarehouseDF, endWarehouseDF("id") === recordDF("ewId"), left_outer)
			// 转运记录表与交通工具表关联
			.join(toolDF, toolDF("id") === recordDF("transportToolId"), left_outer)
			// 转运记录表与路线表关联
			.join(routeDF, routeDF("id") === recordDF("routeId"), left_outer)
			// 起始仓库表与仓库公司关联表关联
			.join(startCompanyWarehouseDF, startCompanyWarehouseDF("warehouseId") === startWarehouseDF("id"), left_outer)
			// 公司表与起始仓库公司关联表关联
			.join(startCompanyDF, startCompanyDF("id") === startCompanyWarehouseDF("companyId"), left_outer)
			// 到达仓库表与仓库公司关联表关联
			.join(endCompanyWarehouseDF, endCompanyWarehouseDF("warehouseId") === endWarehouseDF("id"), left_outer)
			// 公司表与到达仓库公司关联表关联
			.join(endCompanyDF, endCompanyDF("id") === endCompanyWarehouseDF("companyId"), left_outer)
			// 运单表与客户表关联
			.join(customerDF, customerDF("id") === wayBillDF("cid"), left_outer)
			// 下单渠道表与运单表关联
			.join(orderChannelTypeDF, orderChannelTypeDF("orderChannelTypeCode") ===  wayBillDF("orderChannelId"), left_outer)
			// 客户类型表与客户表关联
			.join(customerTypeDF, customerTypeDF("customerTypeCode") === customerDF("type"), left_outer)
			// 3). 添加字段，日期字段day增加日期列
			.withColumn("day", date_format(wayBillDF("cdt"), "yyyyMMdd"))
			// 根据运单表的创建时间顺序排序
			.sort(wayBillDF.col("cdt").asc)
			.select(
				wayBillDF("id"), //运单id
				wayBillDF("expressBillNumber").as("express_bill_number"), //快递单编号
				wayBillDF("waybillNumber").as("waybill_number"), //运单编号
				wayBillDF("cid"), //客户id
				customerDF("name").as("cname"), //客户名称
				customerDF("type").as("ctype"), //客户类型
				customerTypeDF("customerTypeName").as("ctype_name"), //客户类型名称
				wayBillDF("eid"), //快递员id
				courierDF("name").as("ename"), //快递员名称
				dotDF("id").as("dot_id"), //网点id
				dotDF("dotName").as("dot_name"), //网点名称
				areasDF("id").as("area_id"), //区域id
				areasDF("name").as("area_name"), //区域名称
				wayBillDF("orderChannelId").as("order_channel_id"), //渠道id
				orderChannelTypeDF("orderChannelTypeName").as("order_chanel_name"), //渠道名称
				wayBillDF("orderDt").as("order_dt"), //下单时间
				wayBillDF("orderTerminalType").as("order_terminal_type"), //下单设备类型
				wayBillDF("orderTerminalOsType").as("order_terminal_os_type"), //下单设备操作系统类型
				wayBillDF("reserveDt").as("reserve_dt"), //预约取件时间
				wayBillDF("isCollectPackageTimeout").as("is_collect_package_timeout"), //是否取件超时
				wayBillDF("pkgId").as("pkg_id"), //订装ID
				wayBillDF("pkgNumber").as("pkg_number"), //订装编号
				wayBillDF("timeoutDt").as("timeout_dt"), //超时时间
				wayBillDF("transformType").as("transform_type"), //运输方式
				wayBillDF("deliveryAddr").as("delivery_addr"),
				wayBillDF("deliveryCustomerName").as("delivery_customer_name"),
				wayBillDF("deliveryMobile").as("delivery_mobile"),
				wayBillDF("deliveryTel").as("delivery_tel"),
				wayBillDF("receiveAddr").as("receive_addr"),
				wayBillDF("receiveCustomerName").as("receive_customer_name"),
				wayBillDF("receiveMobile").as("receive_mobile"),
				wayBillDF("receiveTel").as("receive_tel"),
				wayBillDF("cdt"),
				wayBillDF("udt"),
				wayBillDF("remark"),
				recordDF("swId").as("sw_id"),
				startWarehouseDF("name").as("sw_name"),
				startCompanyDF("id").as("sw_company_id"),
				startCompanyDF("companyName").as("sw_company_name"),
				recordDF("ewId").as("ew_id"),
				endWarehouseDF("name").as("ew_name"),
				endCompanyDF("id").as("ew_company_id"),
				endCompanyDF("companyName").as("ew_company_name"),
				toolDF("id").as("tt_id"),
				toolDF("licensePlate").as("tt_name"),
				recordDF("routeId").as("route_id"),
				concat(routeDF("startStation"), routeDF("endStation")).as("route_name"),
				col("day")
			)
		wayBillDetailDF.show(10, truncate = false)
		
		// 4). 返回数据
		wayBillDetailDF
	}
	
	def main(args: Array[String]): Unit = {
		// 调用模板基类中模板方法，传递事实表名称（加载表）和宽表名称（保存表）
		execute(
			this.getClass,
			TableMapping.WAY_BILL,
			OfflineTableDefine.WAY_BILL_DETAIL,
			isLoadFullData = Configuration.IS_FIRST_RUNNABLE
		)
	}
	
}
