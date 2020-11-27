package cn.fei.etl

/**
 * 订单数据实时ETL：实时从Kafka Topic 消费数据，进行过滤转换ETL，将其发送Kafka Topic，以便实时处理
 * TODO：基于StructuredStreaming实现，Kafka作为Source和Sink
 */
object streamingProcess {
  /**
   * 对流式数据StreamDataFrame进行ETL过滤清洗转换操作
   */
  // TODO: 对数据进行ETL操作，获取订单状态为0(打开)及转换IP地址为省份和城市
  // 1. 获取订单记录Order Record数据
  // 获取value字段的值，转换为String类型
  // 转换为Dataset类型
  // 过滤数据：通话状态为success
  // 自定义UDF函数，解析IP地址为省份和城市
//  session.sparkContext.addFile(ApplicationConfig.IPS_DATA_REGION_PATH)
//  val ip_to_location: UserDefinedFunction = udf(
//    (ip: String) => {
//      val dbSearcher = new DbSearcher(new DbConfig(), SparkFiles.get("ip2region.db"))
//      // 依据IP地址解析
//      val dataBlock: DataBlock = dbSearcher.btreeSearch(ip)
//      // 中国|0|海南省|海口市|教育网
//      val region: String = dataBlock.getRegion
//      // 分割字符串，获取省份和城市
//      val Array(_, _, province, city, _) = region.split("\\|")
//      // 返回Region对象
//      (province, city)
//    } )
  // 2. 其他订单字段，按照订单状态过滤和转换IP地址
  // 提取订单字段
  // {"orderId":"20200518213916455000009","userId":"300000991","orderTime":"2020-05-18 21:39:16.455","ip":"222.16.48.97","orderMoney":415.3,"orderStatus":0}

}
