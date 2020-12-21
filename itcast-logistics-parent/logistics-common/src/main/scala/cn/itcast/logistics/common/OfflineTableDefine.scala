package cn.itcast.logistics.common

/**
 * 自定义离线计算结果表
 */
object OfflineTableDefine {
	
	//快递单明细表
	val EXPRESS_BILL_DETAIL: String = "tbl_express_bill_detail"
	//快递单指标结果表
	val EXPRESS_BILL_SUMMARY: String = "tbl_express_bill_summary"
	
	//运单明细表
	val WAY_BILL_DETAIL: String = "tbl_waybill_detail"
	//运单指标结果表
	val WAY_BILL_SUMMARY: String = "tbl_waybill_summary"
	
	//仓库明细表
	val WAREHOUSE_DETAIL: String = "tbl_warehouse_detail"
	//仓库指标结果表
	val WAREHOUSE_SUMMARY: String = "tbl_warehouse_summary"
	
	//网点车辆明细表
	val DOT_TRANSPORT_TOOL_DETAIL: String = "tbl_dot_transport_tool_detail"
	//仓库车辆明细表
	val WAREHOUSE_TRANSPORT_TOOL_DETAIL: String = "tbl_warehouse_transport_tool_detail"
	
	//网点车辆指标结果表
	val DOT_TRANSPORT_TOOL_SUMMARY: String = "tbl_dot_transport_tool_summary"
	//仓库车辆指标结果表
	val WAREHOUSE_TRANSPORT_TOOL_SUMMARY: String = "tbl_warehouse_transport_tool_summary"
	
	//客户明细表数据
	val CUSTOMER_DETAIL: String = "tbl_customer_detail"
	//客户指标结果表数据
	val CUSTOMER_SUMMERY: String = "tbl_customer_summary"
	
}

