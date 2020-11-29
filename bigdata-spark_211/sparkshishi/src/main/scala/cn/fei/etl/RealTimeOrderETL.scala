package cn.fei.etl

import cn.fei.config.ApplicationConfig
import cn.fei.utils.SparkUtils
import org.apache.spark.SparkFiles
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.lionsoul.ip2region.{DataBlock, DbConfig, DbSearcher}

/**
 * 订单数据实时ETL：实时从Kafka Topic 消费数据，进行过滤转换ETL，将其发送Kafka Topic，以便实时处理
 */
object RealTimeOrderETL {
  /**
   * 对流式数据StreamDataFrame进行ETL过滤清洗转换操作
   *
   * @param streamDF
   * @return
   */
  def streamingProcess(streamDF: DataFrame): DataFrame = {
    val session = streamDF.sparkSession
    import session.implicits._
    /// TODO: 对数据进行ETL操作，获取订单状态为0(打开)及转换IP地址为省份和城市
    // 1. 获取订单记录Order Record数据
    val orderStreamDF: DataFrame = streamDF
      .selectExpr("CAST(value AS STRING)")
      .as[String]
      //过滤数据：通话状态为success
      .filter(record=>null!=record&&record.trim().split(",").length>0)
      .select(
        get_json_object($"value", "$.orderId").as("orderId"), //
        get_json_object($"value", "$.userId").as("userId"), //
        get_json_object($"value", "$.orderTime").as("orderTime"), //
        get_json_object($"value", "$.ip").as("ip"), //
        get_json_object($"value", "$.orderMoney").as("orderMoney"), //
        get_json_object($"value", "$.orderStatus").cast(IntegerType).as("orderStatus") //
      )
    //过滤数据
    val filterStreamDF: Dataset[Row] = orderStreamDF
      .filter($"ip".isNotNull.and($"orderStatus" === 0))
    //广播字典数据
    session.sparkContext.addFile(ApplicationConfig.IPS_DATA_REGION_PATH)
    //自定义UDF函数，解析IP为省份和城市
    val ip_to_location = udf(
      (ip: String) => {
        //创建DbSearch对象，指定数据字典文件位置
        val dbSearcher: DbSearcher = new DbSearcher(new DbConfig(), SparkFiles.get("ip2region.db"))
        //传递IP地址，解析获取数据
        val dataBlock: DataBlock = dbSearcher.btreeSearch(ip)
        //获取解析省份和城市
        val region: String = dataBlock.getRegion
        val Array(_, _, province, city, _) = region.split("\\|")
        //返回二元组类型-》Hive或者SparkSQL数据类型就是struct结构化类型
        (province, city)
      }
    )
    //组合订单字段为struct类型，转换为json字符串
    filterStreamDF
      // 增加字段
      .withColumn("region", ip_to_location($"ip"))
      // 提取省份和城市字段
      .withColumn("province", $"region._1")
      .withColumn("city", $"region._2")
    // 如何将列转行为JSON格式数据，使用函数
      .select(
        $"orderId".as("key"),
        to_json(
          struct(
            $"orderId", $"userId", $"orderTime", $"ip",
            $"orderMoney", $"orderStatus", $"province", $"city"
          )
        ).as("value")
      )
  }

  def main(args: Array[String]): Unit = {
    //todo:创建sparksession实例对象
    val spark: SparkSession = SparkUtils.createSparkSession(this.getClass)
    // 2. 从KAFKA读取消费数据
    val kafkaStreamDF: DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS)
      .option("subscribe", ApplicationConfig.KAFKA_SOURCE_TOPICS)
      // 设置每批次消费数据最大值
      .option("maxOffsetsPerTrigger", ApplicationConfig.KAFKA_MAX_OFFSETS)
      .load()
    //进行ETL
    val etlStreamDF: DataFrame = streamingProcess(kafkaStreamDF)
    //设置流式应用输出，并启动
    val query = etlStreamDF
      .writeStream
      .outputMode(OutputMode.Append())
      .format("kafka")
      .option("kafka.bootstrap.servers", ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS)
      .option("topic", ApplicationConfig.KAFKA_ETL_TOPIC)
      // 设置检查点目录
      .option("checkpointLocation", ApplicationConfig.STREAMING_ETL_CKPT)
      // 流式应用，需要启动start
      .start()
    // 5. 流式查询等待流式应用终止
    query.awaitTermination()
    query.stop()

  }

}
