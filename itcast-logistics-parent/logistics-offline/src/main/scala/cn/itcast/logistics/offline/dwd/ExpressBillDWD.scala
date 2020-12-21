package cn.itcast.logistics.offline.dwd

import cn.itcast.logistics.common.{CodeTypeMapping, Configuration, OfflineTableDefine, SparkUtils, TableMapping}
import cn.itcast.logistics.offline.OfflineApp
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.storage.StorageLevel

/**
 * 快递单主题开发：
 *      将快递单事实表的数据与相关维度表的数据进行关联JOIN，然后将拉宽后的数据写入到快递单宽表中
 * 采用DSL语义实现离线计算程序
 *      最终离线程序需要部署到服务器，每天定时执行（Azkaban定时调度）
 */
object ExpressBillDWD extends OfflineApp{
	/*
	 * 数据处理，实现步骤：
		 step1. 初始化SparkConf对象
		 step2. 创建SparkSession对象
		 ------------------------------------------------------
		 step3. 加载Kudu中的事实表和维度表的数据（将加载后的数据进行缓存）
		 step4. 定义维度表与事实表的关联
		 step5. 将拉宽后的数据再次写回到Kudu数据库中（DWD明细层）
		 step6. 将缓存的数据删除掉
		------------------------------------------------------
	 */
	override def execute(spark: SparkSession): Unit = {
		//导入隐式转换
		import  spark.implicits._
		
		// step3. 加载Kudu中的事实表和维度表的数据（将加载后的数据进行缓存）
		// 3.1：加载快递单事实表的数据
		val expressBillDF: DataFrame = getKuduSource(
			spark, TableMapping.EXPRESS_BILL, Configuration.IS_FIRST_RUNNABLE
		).persist(StorageLevel.DISK_ONLY_2) //将数据缓存两个节点的磁盘目录，避免单机故障导致的缓存数据丢失
		
		// 3.2：加载快递员维度表的数据
		val courierDF: DataFrame = getKuduSource(
			spark, TableMapping.COURIER, isLoadFullData = true
		).persist(StorageLevel.DISK_ONLY_2)
		
		// 3.2：加载客户维度表的数据
		val customerDF: DataFrame = getKuduSource(
			spark, TableMapping.CUSTOMER, isLoadFullData = true
		).persist(StorageLevel.DISK_ONLY_2)
		
		// 3.4：加载物流码表的数据
		val codesDF: DataFrame = getKuduSource(
			spark, TableMapping.CODES, isLoadFullData = true
		).persist(StorageLevel.DISK_ONLY_2)
		
		// 3.5：客户地址关联表的数据
		val addressMapDF: DataFrame = getKuduSource(
			spark, TableMapping.CONSUMER_ADDRESS_MAP, isLoadFullData = true
		).persist(StorageLevel.DISK_ONLY_2)
		// 3.6：加载地址表的数据
		val addressDF: DataFrame = getKuduSource(
			spark, TableMapping.ADDRESS, isLoadFullData = true
		).persist(StorageLevel.DISK_ONLY_2)
		
		// 3.7：加载包裹表的数据
		val pkgDF: DataFrame = getKuduSource(
			spark, TableMapping.PKG, isLoadFullData = true
		).persist(StorageLevel.DISK_ONLY_2)
		
		// 3.8：加载网点表的数据
		val dotDF: DataFrame = getKuduSource(
			spark, TableMapping.DOT, isLoadFullData = true
		).persist(StorageLevel.DISK_ONLY_2)
		
		// 3.9：加载公司网点表的数据
		val companyDotMapDF: DataFrame = getKuduSource(
			spark, TableMapping.COMPANY_DOT_MAP, isLoadFullData = true
		).persist(StorageLevel.DISK_ONLY_2)
		
		// 3.10：加载公司表的数据
		val companyDF: DataFrame = getKuduSource(
			spark, TableMapping.COMPANY, isLoadFullData = true
		).persist(StorageLevel.DISK_ONLY_2)
		
		//获取终端类型码表数据
		val orderTerminalTypeDF: DataFrame = codesDF
			.where($"type" === CodeTypeMapping.ORDER_TERMINAL_TYPE)
			.select(
				$"code".as("OrderTerminalTypeCode"),
				$"codeDesc".as("OrderTerminalTypeName")
			)
		
		//获取下单渠道类型码表数据
		val orderChannelTypeDF: DataFrame = codesDF
			.where($"type" === CodeTypeMapping.ORDER_CHANNEL_TYPE)
			.select(
				$"code".as("OrderChannelTypeCode"),
				$"codeDesc".as("OrderChannelTypeName")
			)
		
		// step4. 定义维度表与事实表的关联
		val joinType: String = "left_outer"
		val expressBillDetailDF: DataFrame = expressBillDF
			// 快递单表与快递员表进行关联
			.join(courierDF, expressBillDF("eid") === courierDF("id"), joinType)
			// 快递单表与客户表进行关联
			.join(customerDF, expressBillDF("cid")  === customerDF("id"), joinType)
			// 下单渠道表与快递单表关联
			.join(
				orderChannelTypeDF,
				orderChannelTypeDF("OrderChannelTypeCode") === expressBillDF("orderChannelId"),
				joinType
			)
			// 终端类型表与快递单表关联
			.join(
				orderTerminalTypeDF,
				orderTerminalTypeDF("OrderTerminalTypeCode") === expressBillDF("orderTerminalType"),
				joinType
			)
			// 客户地址关联表与客户表关联
			.join(addressMapDF, addressMapDF("consumerId") === customerDF("id"), joinType)
			// 地址表与客户地址关联表关联
			.join(addressDF, addressDF("id") === addressMapDF("addressId"), joinType)
			// 包裹表与快递单表关联
			.join(pkgDF, pkgDF("pwBill") === expressBillDF("expressNumber"), joinType)
			// 网点表与包裹表关联
			.join(dotDF, dotDF("id") === pkgDF("pwDotId"), joinType)
			// 公司网点关联表与网点表关联
			.join(companyDotMapDF, companyDotMapDF("dotId") === dotDF("id"), joinType)
			// 公司网点关联表与公司表关联
			.join(companyDF, companyDF("id") === companyDotMapDF("companyId"), joinType)
			// 虚拟列,可以根据这个日期列作为分区字段，可以保证同一天的数据保存在同一个分区中
			.withColumn(
				"day", date_format(expressBillDF("cdt"), "yyyyMMdd")
			)
			.sort(expressBillDF.col("cdt").asc) //根据快递单的创建时间顺序排序
			.select(
				expressBillDF("id"), // 快递单id
				expressBillDF("expressNumber").as("express_number"), //快递单编号
				expressBillDF("cid"), //客户id
				customerDF("name").as("cname"), //客户名称
				addressDF("detailAddr").as("caddress"), //客户地址
				expressBillDF("eid"), //员工id
				courierDF("name").as("ename"), //员工名称
				dotDF("id").as("dot_id"), //网点id
				dotDF("dotName").as("dot_name"), //网点名称
				companyDF("companyName").as("company_name"),//公司名称
				expressBillDF("orderChannelId").as("order_channel_id"), //下单渠道id
				orderChannelTypeDF("OrderChannelTypeName").as("order_channel_name"), //下单渠道id
				expressBillDF("orderDt").as("order_dt"), //下单时间
				orderTerminalTypeDF("OrderTerminalTypeCode").as("order_terminal_type"), //下单设备类型id
				orderTerminalTypeDF("OrderTerminalTypeName").as("order_terminal_type_name"), //下单设备类型id
				expressBillDF("orderTerminalOsType").as("order_terminal_os_type"),//下单设备操作系统
				expressBillDF("reserveDt").as("reserve_dt"),//预约取件时间
				expressBillDF("isCollectPackageTimeout").as("is_collect_package_timeout"),//是否取件超时
				expressBillDF("timeoutDt").as("timeout_dt"),//超时时间
				customerDF("type"),//客户类型
				expressBillDF("cdt"),//创建时间
				expressBillDF("udt"),//修改时间
				expressBillDF("remark"),//备注
				$"day"
			)
		expressBillDetailDF.show(10, truncate = false)
		
		// step5. 将拉宽后的数据再次写回到Kudu数据库中（DWD明细层）
		save(expressBillDetailDF, OfflineTableDefine.EXPRESS_BILL_DETAIL)
		
		// step6. 将缓存的数据删除掉
		expressBillDF.unpersist()
		courierDF.unpersist()
		customerDF.unpersist()
		orderChannelTypeDF.unpersist()
		orderTerminalTypeDF.unpersist()
		addressMapDF.unpersist()
		addressDF.unpersist()
		pkgDF.unpersist()
		dotDF.unpersist()
		companyDotMapDF.unpersist()
		companyDF.unpersist()
	}
	
	// SparkSQL 应用程序入口：MAIN 方法
	def main(args: Array[String]): Unit = {
		// a. 构建SparkConf对象，设置应用相关配置信息
		val sparkConf: SparkConf = SparkUtils.autoSettingEnv(
			SparkUtils.sparkConf(this.getClass)
		)
		val spark: SparkSession = SparkUtils.getSparkSession(sparkConf)
		spark.sparkContext.setLogLevel(Configuration.LOG_OFF)
		// b. 调用execute方法，加载源数据、处理转换数据和保存数据
		execute(spark)
		// c. 应用结束，关闭资源
		spark.stop()
	}
	
}
