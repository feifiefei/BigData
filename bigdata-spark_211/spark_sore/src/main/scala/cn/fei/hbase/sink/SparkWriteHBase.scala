package cn.fei.hbase.sink

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:spark写入hbase
 * @author: 飞
 * @date: 2020/11/21 0021 19:00
 */
object SparkWriteHBase {
  def main(args: Array[String]): Unit = {
    //构建spark执行环境
    val sc: SparkContext = {
      val conf = new SparkConf()
        .setAppName(this.getClass.getSimpleName.stripSuffix("$"))
        .setMaster("local[2]")
      SparkContext.getOrCreate(conf)
    }
    //读取数据
    val inputRDD: RDD[String] = sc.textFile("datas/wordcount.data")
    //数据处理
    val resultRDD: RDD[(String, Int)] = inputRDD.flatMap(line => line.split("\\s+"))
      .map(word => (word, 1))
      .reduceByKey((temp, item) => temp + item)
    //将处理过的数据打印到控制台
    //    resultRDD.foreach(count => print(count))
    //将结果存入hbase
    // TODO: 1、将数据写入到HBase表中, 使用saveAsNewAPIHadoopFile函数，要求RDD是(key, Value)
    // TODO: 组装RDD[(ImmutableBytesWritable, Put)]
    /**
     * HBase表的设计：
     * 表的名称：htb_wordcount
     * Rowkey: word
     * 列簇: info
     * 字段名称： count
     */
    val putsRDD: RDD[(ImmutableBytesWritable, Put)] = resultRDD.map { case (word, count) =>
      //创建rowKey对象
      val wordBytes: Array[Byte] = Bytes.toBytes(word)
      val rowKey: ImmutableBytesWritable = new ImmutableBytesWritable(wordBytes)
      //创建put对象，设置列名称
      val put = new Put(wordBytes)
      put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("count"), Bytes.toBytes(count.toString))
      rowKey -> put
    }
    //todo:2、调用RDD中saveAsNewAPIHadoopFile保存数据
    val conf: Configuration = HBaseConfiguration.create()
    //设置连接的Zookeeper属性
    conf.set("hbase.zookeeper.quorum", "node1.itcast.cn")
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("zookeeper.znode.parent", "/hbase")
    //设置将数据保存的Hbase表的名称
    conf.set(TableOutputFormat.OUTPUT_TABLE, "htb_wordcount")
    /*
		  def saveAsNewAPIHadoopFile(
		      path: String,
		      keyClass: Class[_],
		      valueClass: Class[_],
		      outputFormatClass: Class[_ <: NewOutputFormat[_, _]],
		      conf: Configuration = self.context.hadoopConfiguration
		  ): Unit
		 */
    putsRDD.saveAsNewAPIHadoopFile(
      "datas/spark/hbase/write-wordcount",
      classOf[ImmutableBytesWritable],
      classOf[Put],
      classOf[TableOutputFormat[ImmutableBytesWritable]],
      conf
    )
    sc.stop()


  }
}
