package cn.fei.mock

/**
 * 订单实体类（Case Class） * @param orderId 订单ID
 * @param userId 用户ID
 * @param orderTime 订单日期时间
 * @param ip 下单IP地址
 * @param orderMoney 订单金额
 * @param orderStatus 订单状态
 */
case class OrderRecord(
     orderId: String,
     userId: String,
     orderTime: String,
     ip: String,
     orderMoney: Double,
     orderStatus: Int
                      )
