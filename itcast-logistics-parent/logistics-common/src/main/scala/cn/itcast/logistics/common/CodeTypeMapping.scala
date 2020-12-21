package cn.itcast.logistics.common

/**
 * 定义物流字典编码类型映射工具类
 */
object CodeTypeMapping {
	//注册渠道
	val REGISTER_CHANNEL: Int = 1
	//揽件状态
	val COLLECT_STATUS: Int = 2
	//派件状态
	val DISPATCH_STATUS: Int = 3
	//快递员状态
	val COURIER_STATUS: Int = 4
	//地址类型
	val ADDRESS_TYPE: Int = 5
	//网点状态
	val Dot_Status: Int = 6
	//员工状态
	val STAFF_STATUS: Int = 7
	//是否保价
	val IS_INSURED: Int = 8
	//运输工具类型
	val TRANSPORT_TYPE: Int = 9
	//运输工具状态
	val TRANSPORT_STATUS: Int = 10
	//仓库类型
	val WAREHOUSE_TYPE: Int = 11
	//是否租赁
	val IS_RENT: Int = 12
	//货架状态
	val GOODS_SHELVES_STATUE: Int = 13
	//回执单状态
	val RECEIPT_STATUS: Int = 14
	//出入库类型
	val WAREHOUSING_TYPE: Int = 15
	//客户类型
	val CUSTOM_TYPE: Int = 16
	//下单终端类型
	val ORDER_TERMINAL_TYPE: Int = 17
	//下单渠道类型
	val ORDER_CHANNEL_TYPE: Int = 18
}
