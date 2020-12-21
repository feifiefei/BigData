package cn.itcast.logistics.etl.realtime

import cn.itcast.logistics.common.beans.logistics.AreasBean
import cn.itcast.logistics.common.beans.parser.{CanalMessageBean, OggMessageBean}
import cn.itcast.logistics.common.{Configuration, SparkUtils, TableMapping, Tools}
import cn.itcast.logistics.etl.parser.DataParser
import com.alibaba.fastjson.JSON
import org.apache.kudu.spark.kudu.KuduContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, Encoders, SparkSession}

object KuduStreamApp extends StreamApp {
	
	/**
	 * 针对物流Logistics系统中相关操作数据，进行解析封装至JavaBean对象，最终保存到Kudu表
	 * @param streamDS 从Kafka获取物流消息Message并封装Bean中数据集Dataset
	 */
	def saveKuduLogistics(streamDS: Dataset[OggMessageBean]): Unit = {
		// 导入隐式变量所在包
		import cn.itcast.logistics.common.BeanImplicit._
		
		// ===================================================================
		// 从OggMessageBean中提取[数据getValue]和[操作类型op_type] ，封装至数据对应表的JavaBean对象中
		// ===================================================================
		// 获取 tbl_areas 表数据，封装对象
		val areasDS: Dataset[AreasBean] = streamDS
			// 依据表的名称进行过滤，获取对应表的操作数据
			//.where($"table" === TableMapping.AREAS)  // DSL编程，类似SQL语句
			.filter(bean => TableMapping.AREAS.equals(bean.getTable)) // 类似RDD 转换函数
			// 条用编写数据解析器DataParser解析数据，封装实例对象
			.map(bean => DataParser.toAreas(bean))
		save(areasDS.toDF(), TableMapping.AREAS)
		/*
		areasBeanDS.writeStream
			.outputMode(OutputMode.Update())
			.queryName("logistics-areas-query")
			.format("console")
			.option("numRows", "20").option("truncate", "false")
			.start()
		*/
		val warehouseSendVehicleDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.WAREHOUSE_SEND_VEHICLE)
			.map(bean => DataParser.toWarehouseSendVehicle(bean))
			.toDF()
		save(warehouseSendVehicleDF, TableMapping.WAREHOUSE_SEND_VEHICLE)
		
		val waybillLineDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.WAYBILL_LINE)
			.map(bean => DataParser.toWaybillLine(bean))
			.toDF()
		save(waybillLineDF, TableMapping.WAYBILL_LINE)
		
		val chargeStandardDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.CHARGE_STANDARD)
			.map(bean => DataParser.toChargeStandard(bean))
			.toDF()
		save(chargeStandardDF, TableMapping.CHARGE_STANDARD)
		
		val codesDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.CODES)
			.map(bean => DataParser.toCodes(bean))
			.toDF()
		save(codesDF, TableMapping.CODES)
		
		val collectPackageDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.COLLECT_PACKAGE)
			.map(bean => DataParser.toCollectPackage(bean))
			.toDF()
		save(collectPackageDF, TableMapping.COLLECT_PACKAGE)
		
		val companyDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.COMPANY)
			.map(bean => DataParser.toCompany(bean))
			.toDF()
		save(companyDF, TableMapping.COMPANY)
		
		val companyDotMapDF = streamDS
			.filter(bean => bean.getTable == TableMapping.COMPANY_DOT_MAP)
			.map(bean => DataParser.toCompanyDotMap(bean))
			.toDF()
		save(companyDotMapDF, TableMapping.COMPANY_DOT_MAP)
		
		val companyTransportRouteMaDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.COMPANY_TRANSPORT_ROUTE_MA)
			.map(bean => DataParser.toCompanyTransportRouteMa(bean))
			.toDF()
		save(companyTransportRouteMaDF, TableMapping.COMPANY_TRANSPORT_ROUTE_MA)
		
		val companyWarehouseMapDF: DataFrame = streamDS.
			filter(bean => bean.getTable == TableMapping.COMPANY_WAREHOUSE_MAP)
			.map(bean => DataParser.toCompanyWarehouseMap(bean))
			.toDF()
		save(companyWarehouseMapDF, TableMapping.COMPANY_WAREHOUSE_MAP)
		
		val consumerSenderInfoDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.CONSUMER_SENDER_INFO)
			.map(bean => DataParser.toConsumerSenderInfo(bean))
			.toDF()
		save(consumerSenderInfoDF, TableMapping.CONSUMER_SENDER_INFO)
		
		val courierDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.COURIER)
			.map(bean => DataParser.toCourier(bean))
			.toDF()
		save(courierDF, TableMapping.COURIER)
		
		val deliverPackageDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.DELIVER_PACKAGE)
			.map(bean => DataParser.toDeliverPackage(bean))
			.toDF()
		save(deliverPackageDF, TableMapping.DELIVER_PACKAGE)
		
		val deliverRegionDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.DELIVER_REGION)
			.map(bean => DataParser.toDeliverRegion(bean))
			.toDF()
		save(deliverRegionDF, TableMapping.DELIVER_REGION)
		
		val deliveryRecordDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.DELIVERY_RECORD)
			.map(bean => DataParser.toDeliveryRecord(bean))
			.toDF()
		save(deliveryRecordDF, TableMapping.DELIVERY_RECORD)
		
		val departmentDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.DEPARTMENT)
			.map(bean => DataParser.toDepartment(bean))
			.toDF()
		save(departmentDF, TableMapping.DEPARTMENT)
		
		val dotDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.DOT)
			.map(bean => DataParser.toDot(bean))
			.toDF()
		save(dotDF, TableMapping.DOT)
		
		val dotTransportToolDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.DOT_TRANSPORT_TOOL)
			.map(bean => DataParser.toDotTransportTool(bean))
			.toDF()
		save(dotTransportToolDF, TableMapping.DOT_TRANSPORT_TOOL)
		
		val driverDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.DRIVER)
			.map(bean => DataParser.toDriver(bean))
			.toDF()
		save(driverDF, TableMapping.DRIVER)
		
		val empDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.EMP)
			.map(bean => DataParser.toEmp(bean))
			.toDF()
		save(empDF, TableMapping.EMP)
		
		val empInfoMapDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.EMP_INFO_MAP)
			.map(bean => DataParser.toEmpInfoMap(bean))
			.toDF()
		save(empInfoMapDF, TableMapping.EMP_INFO_MAP)
		
		val expressBillDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.EXPRESS_BILL)
			.map(bean => DataParser.toExpressBill(bean))
			.toDF()
		save(expressBillDF, TableMapping.EXPRESS_BILL)
		
		val expressPackageDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.EXPRESS_PACKAGE)
			.map(bean => DataParser.toExpressPackage(bean))
			.toDF()
		save(expressPackageDF, TableMapping.EXPRESS_PACKAGE)
		
		val fixedAreaDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.FIXED_AREA)
			.map(bean => DataParser.toFixedArea(bean))
			.toDF()
		save(fixedAreaDF, TableMapping.FIXED_AREA)
		
		val goodsRackDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.GOODS_RACK)
			.map(bean => DataParser.toGoodsRack(bean))
			.toDF()
		save(goodsRackDF, TableMapping.GOODS_RACK)
		
		val jobDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.JOB)
			.map(bean => DataParser.toJob(bean))
			.toDF()
		save(jobDF, TableMapping.JOB)
		
		val outWarehouseDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.OUT_WAREHOUSE)
			.map(bean => DataParser.toOutWarehouse(bean))
			.toDF()
		save(outWarehouseDF, TableMapping.OUT_WAREHOUSE)
		
		val outWarehouseDetailDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.OUT_WAREHOUSE_DETAIL)
			.map(bean => DataParser.toOutWarehouseDetail(bean))
			.toDF()
		save(outWarehouseDetailDF, TableMapping.OUT_WAREHOUSE_DETAIL)
		
		val pkgDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.PKG)
			.map(bean => DataParser.toPkg(bean))
			.toDF()
		save(pkgDF, TableMapping.PKG)
		
		val postalStandardDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.POSTAL_STANDARD)
			.map(bean => DataParser.toPostalStandard(bean))
			.toDF()
		save(postalStandardDF, TableMapping.POSTAL_STANDARD)
		
		val pushWarehouseDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.PUSH_WAREHOUSE)
			.map(bean => DataParser.toPushWarehouse(bean))
			.toDF()
		save(pushWarehouseDF, TableMapping.PUSH_WAREHOUSE)
		
		val pushWarehouseDetailDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.PUSH_WAREHOUSE_DETAIL)
			.map(bean => DataParser.toPushWarehouseDetail(bean))
			.toDF()
		save(pushWarehouseDetailDF, TableMapping.PUSH_WAREHOUSE_DETAIL)
		
		val routeDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.ROUTE)
			.map(bean => DataParser.toRoute(bean))
			.toDF()
		save(routeDF, TableMapping.ROUTE)
		
		val serviceEvaluationDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.SERVICE_EVALUATION)
			.map(bean => DataParser.toServiceEvaluation(bean))
			.toDF()
		save(serviceEvaluationDF, TableMapping.SERVICE_EVALUATION)
		
		val storeGridDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.STORE_GRID)
			.map(bean => DataParser.toStoreGrid(bean))
			.toDF()
		save(storeGridDF, TableMapping.STORE_GRID)
		
		val transportToolDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.TRANSPORT_TOOL)
			.map(bean => DataParser.toTransportTool(bean))
			.toDF()
		save(transportToolDF, TableMapping.TRANSPORT_TOOL)
		
		val vehicleMonitorDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.VEHICLE_MONITOR)
			.map(bean => DataParser.toVehicleMonitor(bean))
			.toDF()
		save(vehicleMonitorDF, TableMapping.VEHICLE_MONITOR)
		
		val warehouseDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.WAREHOUSE)
			.map(bean => DataParser.toWarehouse(bean))
			.toDF()
		save(warehouseDF, TableMapping.WAREHOUSE)
		
		val warehouseEmpDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.WAREHOUSE_EMP)
			.map(bean => DataParser.toWarehouseEmp(bean))
			.toDF()
		save(warehouseEmpDF, TableMapping.WAREHOUSE_EMP)
		
		val warehouseRackMapDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.WAREHOUSE_RACK_MAP)
			.map(bean => DataParser.toWarehouseRackMap(bean))
			.toDF()
		save(warehouseRackMapDF, TableMapping.WAREHOUSE_RACK_MAP)
		
		val warehouseReceiptDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.WAREHOUSE_RECEIPT)
			.map(bean => DataParser.toWarehouseReceipt(bean))
			.toDF()
		save(warehouseReceiptDF, TableMapping.WAREHOUSE_RECEIPT)
		
		val warehouseReceiptDetailDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.WAREHOUSE_RECEIPT_DETAIL)
			.map(bean => DataParser.toWarehouseReceiptDetail(bean))
			.toDF()
		save(warehouseReceiptDetailDF, TableMapping.WAREHOUSE_RECEIPT_DETAIL)
		
		val warehouseTransportToolDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.WAREHOUSE_TRANSPORT_TOOL)
			.map(bean => DataParser.toWarehouseTransportTool(bean))
			.toDF()
		save(warehouseTransportToolDF, TableMapping.WAREHOUSE_TRANSPORT_TOOL)
		
		val warehouseVehicleMapDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.WAREHOUSE_VEHICLE_MAP)
			.map(bean => DataParser.toWarehouseVehicleMap(bean))
			.toDF()
		save(warehouseVehicleMapDF, TableMapping.WAREHOUSE_VEHICLE_MAP)
		
		val waybillDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.WAY_BILL)
			.map(bean => DataParser.toWaybill(bean))
			.toDF()
		save(waybillDF, TableMapping.WAY_BILL)
		
		val waybillStateRecordDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.WAYBILL_STATE_RECORD)
			.map(bean => DataParser.toWaybillStateRecord(bean))
			.toDF()
		save(waybillStateRecordDF, TableMapping.WAYBILL_STATE_RECORD)
		
		val workTimeDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.WORK_TIME)
			.map(bean => DataParser.toWorkTime(bean))
			.toDF()
		save(workTimeDF, TableMapping.WORK_TIME)
		
		val transportRecordDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.TRANSPORT_RECORD)
			.map(bean => DataParser.toTransportRecordBean(bean))
			.toDF()
		save(transportRecordDF, TableMapping.TRANSPORT_RECORD)
	}
	
	/**
	 * 针对CRM系统中相关操作数据，进行解析封装至JavaBean对象，最终保存到Kudu表
	 * @param streamDS 从Kafka获取物流消息Message并封装Bean中数据集Dataset
	 */
	def saveKuduCrm(streamDS: Dataset[CanalMessageBean]): Unit = {
		// 导入隐式变量所在包
		import cn.itcast.logistics.common.BeanImplicit._
		
		// ===================================================================
		// 从CanalMessageBean中提取[数据data]和[操作类型type] ，封装至数据对应表的JavaBean对象中
		// ===================================================================
		val addressDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.ADDRESS)
			.map(bean => DataParser.toAddress(bean))(AddressBeanEncoder)
			.toDF()
		save(addressDF, TableMapping.ADDRESS)
		
		val customerDF : DataFrame= streamDS
			.filter(bean => bean.getTable == TableMapping.CUSTOMER)
			.map(bean => DataParser.toCustomer(bean))(CustomerBeanEncoder)
			.toDF()
		save(customerDF, TableMapping.CUSTOMER)

		val consumerAddressMapDF: DataFrame = streamDS
			.filter(bean => bean.getTable == TableMapping.CONSUMER_ADDRESS_MAP)
			.map(bean => DataParser.toCustomerAddress(bean))(CustomerAddressBeanEncoder)
			.toDF()
		save(consumerAddressMapDF, TableMapping.CONSUMER_ADDRESS_MAP)
	}
	
	/**
	 * 数据的处理：包含从数据源加载数据、对数据进行转换ETL操作及最终将数据保存至外部存储引擎（比如ES、Kudu和CK）
	 *
	 * @param sparkConf SparkConf实例对象，设置应用相关配置
	 */
	override def execute(sparkConf: SparkConf): Unit = {
		/*
			数据处理的步骤：
				step1. 创建SparkSession实例对象，传递SparkConf
				step2. 从Kafka数据源实时消费数据
				step3. 对JSON格式字符串数据进行转换处理
				step4. 获取消费每条数据字段信息
				step5. 将解析过滤获取的数据写入同步至Kudu表
			 */
		// step1. 创建SparkSession实例对象，传递SparkConf
		val spark: SparkSession = SparkUtils.getSparkSession(sparkConf)
		import spark.implicits._
		spark.sparkContext.setLogLevel(Configuration.LOG_OFF)
		
		// step2. 从Kafka数据源实时消费数据
		// 获取物流相关数据
		val logisticsDF: DataFrame = getKafkaSource(spark, Configuration.KAFKA_LOGISTICS_TOPIC)
		val logisticsBeanDS: Dataset[OggMessageBean] = logisticsDF
			.as[String] // 将DataFrame转换为Dataset，由于DataFrame中只有一个字段value，类型是String
			// 对Dataset中每个分区数据进行操作，每个分区数据封装在迭代器中
			.mapPartitions{iter =>
				//val xx: Iterator[String] = iter
				iter
					.filter(jsonStr => null != jsonStr && jsonStr.trim.length > 0 )// 过滤数据
					// 解析JSON格式数据, 使用FastJson类库
					.map{jsonStr => JSON.parseObject(jsonStr, classOf[OggMessageBean]) }
			}(Encoders.bean(classOf[OggMessageBean]))
		
		// 获取CRM相关数据
		val crmDF: DataFrame = getKafkaSource(spark, Configuration.KAFKA_CRM_TOPIC)
		implicit val canalEncoder: Encoder[CanalMessageBean] = Encoders.bean(classOf[CanalMessageBean])
		val crmBeanDS: Dataset[CanalMessageBean] = crmDF
			.as[String]
			.mapPartitions{iter =>
				iter
					.filter(jsonStr => null != jsonStr && jsonStr.trim.length > 0 )// 过滤数据
					// 解析JSON格式数据, 使用FastJson类库
					.map{jsonStr => JSON.parseObject(jsonStr, classOf[CanalMessageBean]) }
			}
		
		// 调用方法，数据转换为Bean对象及保存Kudu表
		saveKuduLogistics(logisticsBeanDS)
		//saveKuduCrm(crmBeanDS)
		
		spark.streams.active.foreach(query => println(s"准备启动查询Query： ${query.name}"))
		spark.streams.awaitAnyTermination()
	}
	
	
	/**
	 * 数据的处理：包含从数据源加载数据、对数据进行转换ETL操作及最终将数据保存至外部存储引擎（比如ES、Kudu和CK）
	 *
	 * @param sparkConf SparkConf实例对象，设置应用相关配置
	 */
	def executeFromBean(sparkConf: SparkConf): Unit = {
		/*
			数据处理的步骤：
				step1. 创建SparkSession实例对象，传递SparkConf
				step2. 从Kafka数据源实时消费数据
				step3. 对JSON格式字符串数据进行转换处理
				step4. 获取消费每条数据字段信息
				step5. 将解析过滤获取的数据写入同步至Kudu表
			 */
		// step1. 创建SparkSession实例对象，传递SparkConf
		val spark: SparkSession = SparkUtils.getSparkSession(sparkConf)
		import spark.implicits._
		spark.sparkContext.setLogLevel(Configuration.LOG_OFF)
		
		// step2. 从Kafka数据源实时消费数据
		// 获取物流相关数据
		val logisticsDF: DataFrame = getKafkaSource(spark, Configuration.KAFKA_LOGISTICS_TOPIC)
		val logisticsBeanDS: Dataset[OggMessageBean] = logisticsDF
			.as[String] // 将DataFrame转换为Dataset，由于DataFrame中只有一个字段value，类型是String
			// 对Dataset中每个分区数据进行操作，每个分区数据封装在迭代器中
			.mapPartitions{iter =>
				//val xx: Iterator[String] = iter
				iter
					.filter(jsonStr => null != jsonStr && jsonStr.trim.length > 0 )// 过滤数据
					// 解析JSON格式数据, 使用FastJson类库
					.map{jsonStr => JSON.parseObject(jsonStr, classOf[OggMessageBean]) }
			}(Encoders.bean(classOf[OggMessageBean]))
		
		// 获取CRM相关数据
		val crmDF: DataFrame = getKafkaSource(spark, Configuration.KAFKA_CRM_TOPIC)
		implicit val canalEncoder: Encoder[CanalMessageBean] = Encoders.bean(classOf[CanalMessageBean])
		val crmBeanDS: Dataset[CanalMessageBean] = crmDF
			.as[String]
			.mapPartitions{iter =>
				//val xx: Iterator[String] = iter
				iter
					.filter(jsonStr => null != jsonStr && jsonStr.trim.length > 0 )// 过滤数据
					// 解析JSON格式数据, 使用FastJson类库
					.map{jsonStr => JSON.parseObject(jsonStr, classOf[CanalMessageBean]) }
			}
		
		// 消费Topic数据，打印控制台
		logisticsBeanDS.writeStream
			.outputMode(OutputMode.Update())
			.queryName("logistics-query")
			.format("console")
			.option("numRows", "20").option("truncate", "false")
			.start()
		crmBeanDS.writeStream
			.outputMode(OutputMode.Update())
			.queryName("crm-query")
			.format("console")
			.option("numRows", "20").option("truncate", "false")
			.start()
		
		spark.streams.active.foreach(query => println(s"准备启动查询Query： ${query.name}"))
		spark.streams.awaitAnyTermination()
	}
	
	/**
	 * 数据的处理：包含从数据源加载数据、对数据进行转换ETL操作及最终将数据保存至外部存储引擎（比如ES、Kudu和CK）
	 *
	 * @param sparkConf SparkConf实例对象，设置应用相关配置
	 */
	def executeFromKafka(sparkConf: SparkConf): Unit = {
		/*
			数据处理的步骤：
				step1. 创建SparkSession实例对象，传递SparkConf
				step2. 从Kafka数据源实时消费数据
				step3. 对JSON格式字符串数据进行转换处理
				step4. 获取消费每条数据字段信息
				step5. 将解析过滤获取的数据写入同步至Kudu表
			 */
		// step1. 创建SparkSession实例对象，传递SparkConf
		val spark: SparkSession = SparkUtils.getSparkSession(sparkConf)
		spark.sparkContext.setLogLevel(Configuration.LOG_OFF)
		
		// step2. 从Kafka数据源实时消费数据
		// 获取物流相关数据
		val logisticsDF: DataFrame = getKafkaSource(spark, Configuration.KAFKA_LOGISTICS_TOPIC)
		// 获取CRM相关数据
		val crmDF: DataFrame = getKafkaSource(spark, Configuration.KAFKA_CRM_TOPIC)
		
		// 消费Topic数据，打印控制台
		logisticsDF.writeStream
			.outputMode(OutputMode.Update())
			.queryName("logistics-query")
			.format("console")
			.option("numRows", "20").option("truncate", "false")
			.start()
		crmDF.writeStream
			.outputMode(OutputMode.Update())
			.queryName("crm-query")
			.format("console")
			.option("numRows", "20").option("truncate", "false")
			.start()
		spark.streams.active.foreach(query => println(s"准备启动查询Query： ${query.name}"))
		spark.streams.awaitAnyTermination()
	}
	
	/**
	 * 数据的保存
	 *
	 * @param dataframe         保存数据集DataFrame
	 * @param tableName         保存表的名称
	 * @param isAutoCreateTable 是否自动创建表，默认创建表
	 */
	def saveKudu(dataframe: DataFrame, tableName: String,
	                  isAutoCreateTable: Boolean = true): Unit = {
		// a. 当isAutoCreateTable为true时，表示自动创建表
		if(isAutoCreateTable){
			Tools.autoCreateKuduTable(tableName, dataframe)
		}
		// b. TODO： 保存数据至Kudu表，此处简化实际业务代码，应该依据Op_Type对Kudu数据进行Insert、Update、Delete操作。
		dataframe
			.writeStream
			.format(Configuration.SPARK_KUDU_FORMAT)
			.outputMode(OutputMode.Append())
			.option("kudu.master", Configuration.KUDU_RPC_ADDRESS)
			.option("kudu.table", tableName)
			.queryName(s"query-${tableName}-${Configuration.SPARK_KUDU_FORMAT}")
			.start()
	}
	
	/**
	 * 数据的保存
	 * @param streamDF         保存数据集DataFrame
	 * @param tableName         保存表的名称
	 * @param isAutoCreateTable 是否自动创建表，默认创建表
	 */
	override def save(streamDF: DataFrame, tableName: String,
	                  isAutoCreateTable: Boolean = true): Unit = {
		// a. 当isAutoCreateTable为true时，表示自动创建表
		if(isAutoCreateTable){
			Tools.autoCreateKuduTable(s"ods_${tableName}", streamDF.drop(col("opType")))
		}
		// b. TODO： 保存数据至Kudu表，应该依据Op_Type对Kudu数据进行Insert、Update、Delete操作。
		/*
			1. 当Op_Type为Insert和Update时，属于插入更新操作： Upsert
			2. 当Op_Type为Delete时，属于删除操作，依据Kudu表的主键删除数据: Delete
			
			回顾Spark与Kudu集成
				方式一：KuduContext 进行DDL（创建表，修改表等）和DML（CRUD等）操作
				方式二：SparkSQL实现外部数据源，SparkSession操作，加载数据和保存数据
		 */
		// 第一步. 构建Kudu的上下文对象：KuduContext
		val kuduContext: KuduContext = new KuduContext(
			Configuration.KUDU_RPC_ADDRESS, streamDF.sparkSession.sparkContext
		)
		// 第二步、 依据opType划分数据，将数据分为2部分：Upsert操作和Delete操作
		// Upsert操作，通过过滤获取插入和更新数据
		val upsertStreamDF: DataFrame = streamDF
    			.filter(
				    upper(col("opType")) === "INSERT" ||
				    upper(col("opType")) === "UPDATE"
			    )
				// 删除opType列数据
    			.drop(col("opType"))
		upsertStreamDF.writeStream
			.outputMode(OutputMode.Append())
			.queryName(s"query-${tableName}-${Configuration.SPARK_KUDU_FORMAT}-upsert")
    		.foreachBatch((batchDF: DataFrame, _: Long) => {
			    if(! batchDF.isEmpty) kuduContext.upsertRows(batchDF, s"ods_${tableName}")
		    })
			.start()
		
		// Delete操作，通过过滤获取删除数据
		val deleteStreamDF: DataFrame = streamDF
			.filter(upper(col("opType")) === "DELETE")
			.select(col("id"))
		deleteStreamDF.writeStream
			.outputMode(OutputMode.Append())
			.queryName(s"query-${tableName}-${Configuration.SPARK_KUDU_FORMAT}-delete")
			.foreachBatch((batchDF: DataFrame, _: Long) => {
				if(! batchDF.isEmpty) kuduContext.deleteRows(batchDF, s"ods_${tableName}")
			})
			.start()
	}
	
	// StructuredStreaming应用程序入口：MAIN 方法
	def main(args: Array[String]): Unit = {
		// 第一步、构建SparkConf对象，设置应用基本配置
		/*
			val sparkConf = SparkUtils.sparkConf(this.getClass)
			val conf = SparkUtils.autoSettingEnv(sparkConf)
		*/
		val sparkConf: SparkConf = SparkUtils.autoSettingEnv(SparkUtils.sparkConf(this.getClass))
		
		// 第二步、调度execute方法，具体处理数据，从数据源、数据转换和数据保存输出
		execute(sparkConf)
	}
	
}
