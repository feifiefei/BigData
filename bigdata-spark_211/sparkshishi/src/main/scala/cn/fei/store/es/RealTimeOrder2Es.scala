package cn.fei.store.es

import cn.fei.config.ApplicationConfig
import cn.fei.utils.{SparkUtils, StreamingUtils}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.{OutputMode, StreamingQuery}
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}
/**
 * @description:StructuredStreaming 实时消费Kafka Topic中数据，存入到Elasticsearch索引中
 * @author: 飞
 * @date: 2020/11/27 0027 23:17
 */
object RealTimeOrder2Es {
  def main(args: Array[String]): Unit = {
    // TODO: 1. 创建SparkSession实例对象
    val spark: SparkSession = SparkUtils.createSparkSession(this.getClass)
    import spark.implicits._

    // TODO: 2. 从Kafka消费数据
    val kafkaStreamDF: DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS)
      .option("subscribe", ApplicationConfig.KAFKA_ETL_TOPIC)
      // 设置每批次消费数据最大值
      .option("maxOffsetsPerTrigger", ApplicationConfig.KAFKA_MAX_OFFSETS)
      .load()
    // TODO: 3. 从JSON字符串中提取各个字段值，采用from_json函数
    /*
  {
    "orderId": "20201128145020837000002",
    "userId": "400000843",
    "orderTime": "2020-11-28 14:50:20.837",
    "ip": "123.235.249.37",
    "orderMoney": "65.04",
    "orderStatus": 0,
    "province": "山东省",
    "city": "青岛市"
    */
    // Schema信息
    val orderSchema: StructType = new StructType()
      .add("orderId", StringType, nullable = true)
      .add("userId", StringType, nullable = true)
      .add("orderTime", StringType, nullable = true)
      .add("ip", StringType, nullable = true)
      .add("orderMoney", StringType, nullable = true)
      .add("orderStatus", IntegerType, nullable = true)
      .add("province", StringType, nullable = true)
      .add("city", StringType, nullable = true)
    val orderStreamDF: DataFrame = kafkaStreamDF
      .selectExpr("cast(value as STRING")
      .as[String]
      .filter(line => null != line && line.trim.length > 0)
      // 提取字段
      .select(from_json($"value", orderSchema).as("order"))
      .select($"order.*")
    // TODO: 4. 将流式Streaming DataFrame数据直接保存至Elasticsearch索引中
    val query: StreamingQuery = orderStreamDF
      .writeStream
      .outputMode(OutputMode.Append())
      // 设置检查点目录
      .option("checkpointLocation", ApplicationConfig.STREAMING_ES_CKPT)
      .format("es")
      .option("es.nodes", ApplicationConfig.ES_NODES)
      .option("es.port", ApplicationConfig.ES_PORT)
      .option("es.index.auto.create", ApplicationConfig.ES_INDEX_AUTO_CREATE)
      .option("es.write.operation", ApplicationConfig.ES_WRITE_OPERATION)
      .option("es.mapping.id", ApplicationConfig.ES_MAPPING_ID)
      .start(ApplicationConfig.ES_INDEX_NAME)
    // TODO: 5. 通过扫描HDFS文件，优雅的关闭停止StreamingQuery
    StreamingUtils.stopStructuredStreaming(query, ApplicationConfig.STOP_ES_FILE)
  }
}
