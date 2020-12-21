package cn.itcast.logistics.common

import cn.itcast.logistics.common.beans.logistics._
import cn.itcast.logistics.common.beans.crm._
import cn.itcast.logistics.common.beans.parser._
import org.apache.spark.sql.{Encoder, Encoders}

/**
 * 扩展自定义POJO的隐式转换实现
 */
object BeanImplicit {
	
	//定义javaBean的隐式转换
	implicit val OggMessageBeanEncoder: Encoder[OggMessageBean] = Encoders.bean(classOf[OggMessageBean])
	implicit val CanalMessageBeanEncoder: Encoder[CanalMessageBean] = Encoders.bean(classOf[CanalMessageBean])
	
	// Logistics Bean
	implicit val AreasBeanEncoder: Encoder[AreasBean] = Encoders.bean(classOf[AreasBean])
	implicit val ChargeStandardBeanEncoder: Encoder[ChargeStandardBean] = Encoders.bean(classOf[ChargeStandardBean])
	implicit val CodesBeanEncoder: Encoder[CodesBean] = Encoders.bean(classOf[CodesBean])
	implicit val CollectPackageBeanEncoder: Encoder[CollectPackageBean] = Encoders.bean(classOf[CollectPackageBean])
	implicit val CompanyBeanEncoder: Encoder[CompanyBean] = Encoders.bean(classOf[CompanyBean])
	implicit val CompanyDotMapBeanEncoder: Encoder[CompanyDotMapBean] = Encoders.bean(classOf[CompanyDotMapBean])
	implicit val CompanyTransportRouteMaBeanEncoder: Encoder[CompanyTransportRouteMaBean] = Encoders.bean(classOf[CompanyTransportRouteMaBean])
	implicit val CompanyWarehouseMapBeanEncoder: Encoder[CompanyWarehouseMapBean] = Encoders.bean(classOf[CompanyWarehouseMapBean])
	implicit val ConsumerSenderInfoBeanEncoder: Encoder[ConsumerSenderInfoBean] = Encoders.bean(classOf[ConsumerSenderInfoBean])
	implicit val CourierBeanEncoder: Encoder[CourierBean] = Encoders.bean(classOf[CourierBean])
	implicit val DeliverPackageBeanEncoder: Encoder[DeliverPackageBean] = Encoders.bean(classOf[DeliverPackageBean])
	implicit val DeliverRegionBeanEncoder: Encoder[DeliverRegionBean] = Encoders.bean(classOf[DeliverRegionBean])
	implicit val DeliveryRecordBeanEncoder: Encoder[DeliveryRecordBean] = Encoders.bean(classOf[DeliveryRecordBean])
	implicit val DepartmentBeanEncoder: Encoder[DepartmentBean] = Encoders.bean(classOf[DepartmentBean])
	implicit val DotBeanEncoder: Encoder[DotBean] = Encoders.bean(classOf[DotBean])
	implicit val DotTransportToolBeanEncoder: Encoder[DotTransportToolBean] = Encoders.bean(classOf[DotTransportToolBean])
	implicit val DriverBeanEncoder: Encoder[DriverBean] = Encoders.bean(classOf[DriverBean])
	implicit val EmpBeanEncoder: Encoder[EmpBean] = Encoders.bean(classOf[EmpBean])
	implicit val EmpInfoMapBeanEncoder: Encoder[EmpInfoMapBean] = Encoders.bean(classOf[EmpInfoMapBean])
	implicit val ExpressBillBeanEncoder: Encoder[ExpressBillBean] = Encoders.bean(classOf[ExpressBillBean])
	implicit val ExpressPackageBeanEncoder: Encoder[ExpressPackageBean] = Encoders.bean(classOf[ExpressPackageBean])
	implicit val FixedAreaBeanEncoder: Encoder[FixedAreaBean] = Encoders.bean(classOf[FixedAreaBean])
	implicit val GoodsRackBeanEncoder: Encoder[GoodsRackBean] = Encoders.bean(classOf[GoodsRackBean])
	implicit val JobBeanEncoder: Encoder[JobBean] = Encoders.bean(classOf[JobBean])
	implicit val OutWarehouseBeanEncoder: Encoder[OutWarehouseBean] = Encoders.bean(classOf[OutWarehouseBean])
	implicit val OutWarehouseDetailBeanEncoder: Encoder[OutWarehouseDetailBean] = Encoders.bean(classOf[OutWarehouseDetailBean])
	implicit val PkgBeanEncoder: Encoder[PkgBean] = Encoders.bean(classOf[PkgBean])
	implicit val PostalStandardBeanEncoder: Encoder[PostalStandardBean] = Encoders.bean(classOf[PostalStandardBean])
	implicit val PushWarehouseBeanEncoder: Encoder[PushWarehouseBean] = Encoders.bean(classOf[PushWarehouseBean])
	implicit val PushWarehouseDetailBeanEncoder: Encoder[PushWarehouseDetailBean] = Encoders.bean(classOf[PushWarehouseDetailBean])
	implicit val RouteBeanEncoder: Encoder[RouteBean] = Encoders.bean(classOf[RouteBean])
	implicit val ServiceEvaluationBeanEncoder: Encoder[ServiceEvaluationBean] = Encoders.bean(classOf[ServiceEvaluationBean])
	implicit val StoreGridBeanEncoder: Encoder[StoreGridBean] = Encoders.bean(classOf[StoreGridBean])
	implicit val TransportToolBeanEncoder: Encoder[TransportToolBean] = Encoders.bean(classOf[TransportToolBean])
	implicit val VehicleMonitorBeanEncoder: Encoder[VehicleMonitorBean] = Encoders.bean(classOf[VehicleMonitorBean])
	implicit val WarehouseBeanEncoder: Encoder[WarehouseBean] = Encoders.bean(classOf[WarehouseBean])
	implicit val WarehouseEmpBeanEncoder: Encoder[WarehouseEmpBean] = Encoders.bean(classOf[WarehouseEmpBean])
	implicit val WarehouseRackMapBeanEncoder: Encoder[WarehouseRackMapBean] = Encoders.bean(classOf[WarehouseRackMapBean])
	implicit val WarehouseReceiptBeanEncoder: Encoder[WarehouseReceiptBean] = Encoders.bean(classOf[WarehouseReceiptBean])
	implicit val WarehouseReceiptDetailBeanEncoder: Encoder[WarehouseReceiptDetailBean] = Encoders.bean(classOf[WarehouseReceiptDetailBean])
	implicit val WarehouseSendVehicleBeanEncoder: Encoder[WarehouseSendVehicleBean] = Encoders.bean(classOf[WarehouseSendVehicleBean])
	implicit val WarehouseTransportToolBeanEncoder: Encoder[WarehouseTransportToolBean] = Encoders.bean(classOf[WarehouseTransportToolBean])
	implicit val WarehouseVehicleMapBeanEncoder: Encoder[WarehouseVehicleMapBean] = Encoders.bean(classOf[WarehouseVehicleMapBean])
	implicit val WaybillBeanEncoder: Encoder[WaybillBean] = Encoders.bean(classOf[WaybillBean])
	implicit val WaybillLineBeanEncoder: Encoder[WaybillLineBean] = Encoders.bean(classOf[WaybillLineBean])
	implicit val WaybillStateRecordBeanEncoder: Encoder[WaybillStateRecordBean] = Encoders.bean(classOf[WaybillStateRecordBean])
	implicit val WorkTimeBeanEncoder: Encoder[WorkTimeBean] = Encoders.bean(classOf[WorkTimeBean])
	implicit val TransportRecordBeanEncoder: Encoder[TransportRecordBean] = Encoders.bean(classOf[TransportRecordBean])
	
	// CRM Bean
	implicit val CustomerBeanEncoder: Encoder[CustomerBean] = Encoders.bean(classOf[CustomerBean])
	implicit val AddressBeanEncoder: Encoder[AddressBean] = Encoders.bean(classOf[AddressBean])
	implicit val CustomerAddressBeanEncoder: Encoder[CustomerAddressBean] = Encoders.bean(classOf[CustomerAddressBean])
	
}
