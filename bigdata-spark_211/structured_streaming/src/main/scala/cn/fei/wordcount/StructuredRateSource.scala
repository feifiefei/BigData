package cn.fei.wordcount

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

/**
 * @description:数据源：Rate Source，以每秒指定的行数生成数据，每个输出行包含一个timestamp和value。
 * @author: 飞
 * @date: 2020/11/27 0027 20:57
 */
object StructuredRateSource {
  def main(args: Array[String]): Unit = {
    //构建Sparksession实例对象
    val spark: SparkSession = SparkSession.builder()
      .master("local[2]")
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      .config("spark.sql.shuffle.partitions", "2")
      .getOrCreate()
    // 导入隐式转换和函数库

    import spark.implicits._
    // / TODO: 从文件系统，监控目录，读取CSV格式数据
    // TODO：从Rate数据源实时消费数据
    val rateStreamDF: DataFrame = spark.readStream
      .format("rate")
      .option("rowsPerSecond", "10") // 每秒生成数据数目
      .option("rampUpTime", "0s") // 每条数据生成间隔时间
      .option("numPartitions", "2") // 分区数目
      .load()


    // TODO: 3. 设置Streaming应用输出及启动
    val query = rateStreamDF.writeStream
      // TODO: 设置输出模式：Complete表示将ResultTable中所有结果数据输出
      // TODO: 设置输出模式：Update表示将ResultTable中有更新结果数据输出
      .outputMode(OutputMode.Append())
      .format("console")
      .option("numRows", "10")
      .option("truncate", "false")
      .trigger(Trigger.ProcessingTime("2 seconds"))
      .trigger(Trigger.Once())
      .trigger(Trigger.Continuous("1 second"))
      //流式应用，需要启动start
      .start()
    //流式查询等待流式应用
    query.awaitTermination()
    //等待所有任务运行完成才停止运行
    query.stop()
  }
}
