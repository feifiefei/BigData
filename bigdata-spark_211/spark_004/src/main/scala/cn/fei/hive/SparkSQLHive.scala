package cn.fei.hive

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

/**
 * @description:parkSQL集成Hive，读取Hive表的数据进行分析
 * @author: 飞
 * @date: 2020/11/22 0022 16:54
 */
object SparkSQLHive {
  def main(args: Array[String]): Unit = {
    //todo:继承hive，创建Sparksession对象时，设置HIveMeStore服务地址
    val spark: SparkSession = SparkSession.builder()
      .master("local[2]")
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      //设置sparksql在shuffle阶段的分区数据，实际项目根据数据量调整
      .config("spark.sql.shuffle.partitions", "2")
      //显示指示继承Hive
      .enableHiveSupport()
      //设置HiveMestore服务地址
      .config("hive.metastore.uris", "thrift://node1.itcast.cn:9083")
      .getOrCreate()
    import spark.implicits._
    //DSL数据分析
    spark.read
      .table("db_hive.emp")
      .groupBy($"deptno")
      .agg(round(avg($"sal"), 2).as("avg_sal"))
//      .show(10, truncate = false)
    //SQL方式
    spark.sql(
      """
        |select deptno,round(avg(sal),2) as vag_sql from db_hive.emp group by deptno
        |""".stripMargin)
      .show(10, truncate = false)
    Thread.sleep(1000000l)
    spark.stop()
  }
}
