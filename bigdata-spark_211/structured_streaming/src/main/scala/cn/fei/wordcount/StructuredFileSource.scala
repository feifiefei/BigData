package cn.fei.wordcount

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

/**
 * @description:使用Structured Streaming从目录中读取文件数据：统计年龄小于25岁的人群的爱好排行榜
 * @author: 飞
 * @date: 2020/11/27 0027 20:33
 */
object StructuredFileSource {
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
    //数据样本->jack，23，running
    val csvSChema: StructType = new StructType()
      .add("name", StringType, nullable = true)
      .add("age", IntegerType, nullable = true)
      .add("hobby", StringType, nullable = true)
    val inputSDF: DataFrame = spark.readStream
      .option("sep", ";")
      .option("header", "false")
      //指定schema信息
      .schema(csvSChema)
      .csv("file:///D:/datas/")
    // 依据业务需求，分析数据：统计年龄小于25岁的人群的爱好排行榜
    val resultDM = inputSDF
      .filter($"age" < 25)
      .groupBy($"hobby").count()
      .orderBy($"count".desc)


    // TODO: 3. 设置Streaming应用输出及启动
    val query = resultDM.writeStream
      // TODO: 设置输出模式：Complete表示将ResultTable中所有结果数据输出
      // TODO: 设置输出模式：Update表示将ResultTable中有更新结果数据输出
      .outputMode(OutputMode.Complete())
      .format("console")
      .option("numRows", "10")
      .option("truncate", "false")
      //流式应用，需要启动start
      .start()
    //流式查询等待流式应用
    query.awaitTermination()
    //等待所有任务运行完成才停止运行
    query.stop()

  }
}
