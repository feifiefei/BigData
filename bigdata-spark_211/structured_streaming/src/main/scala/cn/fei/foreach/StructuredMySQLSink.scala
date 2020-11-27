package cn.fei.foreach

import org.apache.spark.sql.streaming.{OutputMode, StreamingQuery}
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @description:* 使用Structured Streaming从TCP Socket实时读取数据，进行词频统计，将结果存储到MySQL数据库表中
 * @author: 飞
 * @date: 2020/11/27 0027 21:22
 */
object StructuredMySQLSink {
  def main(args: Array[String]): Unit = {
    // 构建SparkSession实例对象
    val spark: SparkSession = SparkSession.builder()
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      .master("local[2]")
      // 设置Shuffle分区数目
      .config("spark.sql.shuffle.partitions", "2")
      .getOrCreate()
    // 导入隐式转换和函数库
    import spark.implicits._
    // 1. 从Kafka读取数据，底层采用New Consumer API
    val inputStreamDF: DataFrame = spark.readStream
      .format("socket")
      .option("host", "node1.itcast.cn")
      .option("port", 9999)
      .load()
    // 2. 业务分析：词频统计WordCount
    val resultStreamDF: DataFrame = inputStreamDF
      // 转换为Dataset类型
      .as[String]
      // 过滤数据
      .filter(line => null != line && line.trim.length > 0)
      // 分割单词
      .flatMap(line => line.trim.split("\\s+"))
      // 按照单词分组，聚合
      .groupBy($"value").count()
    // 设置Streaming应用输出及启动
    val query: StreamingQuery = resultStreamDF.writeStream
    // 对流式应用输出来说，设置输出模式，Update表示有数据更新才输出，没数据更新不输出
      .outputMode(OutputMode.Update())
    // TODO: def foreach(writer: ForeachWriter[T]): DataStreamWriter[T]
      .foreach(new MySQLForeachWriter)
      .start()
    // 查询器等待流式应用终止
    query.awaitTermination()
    query.stop() // 等待所有任务运行完成才停止运行
  }
}
