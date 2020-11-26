package cn.fei.parquet

import java.util.Properties

import org.apache.spark.sql.{DataFrame, DataFrameReader, SparkSession}

/**
 * @description:sparksql读rdbms数据库
 * @author: 飞
 * @date: 2020/11/23 0023 13:12
 */
object SparkSQLMysql {
  def main(args: Array[String]): Unit = {
    //构建spark执行环境
    val spark: SparkSession = SparkSession.builder()
      .master("local[3]")
      .appName(this.getClass.getSimpleName.stripSuffix("$"))
      .config("spark.sql.shuffle.partitions", "4")
      .getOrCreate()
    import spark.implicits._
    //读取mysql数据
    //简洁版方式
    val props = new Properties()
    props.put("user", "root")
    props.put("password", "123456")
    props.put("driver", "com.mysql.cj.jdbc.Driver")
    val empDF: DataFrame = spark.read
      .jdbc(
        "jdbc:mysql://node1.itcast.cn:3306/?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true",
        "db_test.emp",
        props
      )
    //    empDF.printSchema()
    //    empDF.show(10, truncate = false)
    //标准格式
    val sqlStr = "(select e.ename,e.sal,d.deptname from emp e join dept d on e.deptno = d.deptno) as tmp"
    val myDF: DataFrame = spark.read
      .format("jdbc")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("url", "jdbc:mysql://node1.itcast.cn:3306/db_test?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true")
      .option("user", "root")
      .option("password", "123456")
      .option("dbtable", sqlStr)
      .load()
    myDF.printSchema()
    myDF.show(10, truncate = false)


    //    Thread.sleep(1000000L)
    spark.stop()

  }

}
