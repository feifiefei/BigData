package cn.fei.store.hbase


import cn.fei.config.ApplicationConfig
import cn.fei.utils.{SparkUtils, StreamingUtils}
import org.apache.spark.internal.Logging
import org.apache.spark.sql.streaming.{OutputMode, StreamingQuery}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
/**
 * @description:
 * @author: 飞
 * @date: 2020/11/28 0028 21:15
 */
object RealTimeOrder2HBase extends Logging{
  def main(args: Array[String]): Unit = {
    // 1. 获取SparkSession实例对象
    val spark: SparkSession = SparkUtils.createSparkSession(this.getClass)
    import org.apache.spark.sql.functions._
    import spark.implicits._
    // 2. 从KAFKA读取消费数据
    val kafkaStreamDF: DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS)
      .option("subscribe", ApplicationConfig.KAFKA_ETL_TOPIC)
      // 设置每批次消费数据最大值
      .option("maxOffsetsPerTrigger", ApplicationConfig.KAFKA_MAX_OFFSETS)
      .load()
    // 从Kafka Streaming DataFrame中获取消息数据Message
    val orderStreamDS: Dataset[String] = kafkaStreamDF
      // 将value转换为String字符串类型
      .selectExpr("CAST(value AS STRING)")
      // 将DataFrame转换为Dataset
      .as[String]
      // 过滤数据
      .filter(line => null != line && line.trim.length > 0)
    // 4. 将数据保存至Elasticsearch索引中
    val query = orderStreamDS
      .toDF()
      .writeStream
      .queryName("query-store-hbase")
      // 设置追加模式Append
      .outputMode(OutputMode.Append())
      // TODO: 调用foreachBatch函数，将每批次DataFrame数据存储至HBase表中
      .foreachBatch{(batchDF: DataFrame, _: Long) =>
        batchDF.rdd.foreachPartition{ iter =>
          val datas: Iterator[String] = iter.map(row => row.getAs[String]("value"))
          val isInsertSuccess: Boolean = HBaseDao.insert(
            ApplicationConfig.HBASE_ORDER_TABLE,
            ApplicationConfig.HBASE_ORDER_TABLE_FAMILY,
            ApplicationConfig.HBASE_ORDER_TABLE_COLUMNS,
            datas
          )
          logWarning(s"Insert Datas To HBase: $isInsertSuccess")
        }
      }
      // 设置检查点目录
      .option("checkpointLocation", ApplicationConfig.STREAMING_HBASE_CKPT)
      .start()
    // TODO: 5. 通过扫描HDFS文件，优雅的关闭停止StreamingQuery
    StreamingUtils.stopStructuredStreaming(query, ApplicationConfig.STOP_HBASE_FILE)
  }
}
