package cn.fei.report

/**
 * 实时订单报表：从Kafka Topic实时消费订单数据，进行销售订单额统计，维度如下：
 * - 第一、总销售额：sum
 * - 第二、各省份销售额：province
 * - 第三、重点城市销售额：city
 * "北京市", "上海市", "深圳市", "广州市", "杭州市", "成都市", "南京市", "武汉市", "西安市" * TODO：每日实时统计，每天统计的数据为当日12:00 - 24：00产生的数据
 */
object DailyRealTimeOrderReport {

}
