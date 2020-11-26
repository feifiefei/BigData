package cn.fei.hbase.source

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/21 0021 20:22
 */
object SparkReadHBase {
  def main(args: Array[String]): Unit = {
    //在Spark应用程序中，入口为：SparkContext，必须创建实例化对象，加载数据和调度程序执行
    val sc: SparkContext = {
      //创建sparkconf对象，设置应用相关信息
      val sparkConf: SparkConf = new SparkConf()
        .setAppName(this.getClass.getSimpleName.stripSuffix("$"))
        .setMaster("local[2]")
        //todo:设置使用Kryo序列化方式
        .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        //注册序列化的数据类型
        .registerKryoClasses(Array(classOf[ImmutableBytesWritable], classOf[Result]))
      //构建SparkContext实例化对象，传递SparkConf
      new SparkContext(sparkConf)
    }
    //从HBase表读取数据，调用RDD方法：newAPIHadoopRDD
    val conf: Configuration = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum", "node1.itcast.cn")
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("zookeeper.znode.parent", "/hbase")
    //设置将数据保存的HBase的表名称
    conf.set(TableInputFormat.INPUT_TABLE, "htb_wordcount")
    /*
		  def newAPIHadoopRDD[K, V, F <: NewInputFormat[K, V]](
		      conf: Configuration = hadoopConfiguration,
		      fClass: Class[F],
		      kClass: Class[K],
		      vClass: Class[V]
		  ): RDD[(K, V)]
		 */
    val hbaseRDD: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(
      conf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result]
    )
    println(s"count=${hbaseRDD.count()}")
    //打印HBase表样本数据
    hbaseRDD.take(6).foreach { case (rowKey, result) =>
      //获取Hbase表每行数据中各列数据
      val cells: Array[Cell] = result.rawCells()
      cells.foreach { cell =>
        val family: String = Bytes.toString(CellUtil.cloneFamily(cell))
        val column: String = Bytes.toString(CellUtil.cloneQualifier(cell))
        val value: String = Bytes.toString(CellUtil.cloneValue(cell))
        println(
          //s"key = ${Bytes.toString(rowkey.get())}, " +  // TODO: 此行代码有问题，底层迭代器有BUG
          s"key = ${Bytes.toString(result.getRow)}, " +
            s"column=${family}:${column}, timestamp=${cell.getTimestamp}, value=${value}"
        )
      }
    }
    sc.stop()
  }
}
