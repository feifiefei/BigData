package cn.fei.wordcount

import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @description:使用Structured Streaming从TCP Socket实时读取数据，进行词频统计，将结果打印到控制台。
 * @author: 飞
 * @date: 2020/11/27 0027 20:00
 */
object StructuredWordCount {
  def main(args: Array[String]): Unit = {
    //构建Sparksession实例对象
    val spark: SparkSession = SparkSession.builder()
      .master("local[2]")
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      .config("spark.sql.shuffle.partitions", "2")
      .getOrCreate()
    // 导入隐式转换和函数库

    import spark.implicits._
    // TODO: 1. 从TCP Socket 读取数据
    val inputSDF: DataFrame = spark.readStream
      .format("socket")
      .option("host", "node1.itcast.cn")
      .option("port", 9999)
      .load()
    // TODO: 2. 业务分析：词频统计WordCount
    val resultDM: DataFrame = inputSDF
      .as[String]
      .filter(line => null != line && line.trim.split("\\s+").length > 0)
      .flatMap(value => value.trim.split("\\s+"))
      .groupBy($"value").count() //按照单词分组，聚合
    // TODO: 3. 设置Streaming应用输出及启动
    val query = resultDM.writeStream
      // TODO: 设置输出模式：Complete表示将ResultTable中所有结果数据输出
      // TODO: 设置输出模式：Update表示将ResultTable中有更新结果数据输出
      .outputMode(OutputMode.Update())
      .format("console")
      .option("numRows", "10")
      .option("truncate", "false")
      //流式应用，需要启动start
      .start
    //流式查询等待流式应用
    query.awaitTermination()
    //等待所有任务运行完成才停止运行
    query.stop()

  }
}
