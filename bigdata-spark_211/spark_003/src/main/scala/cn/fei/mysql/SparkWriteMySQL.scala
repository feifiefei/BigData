package cn.fei.mysql

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/21 0021 20:54
 */
object SparkWriteMySQL {
  def main(args: Array[String]): Unit = {
    // 1. 在Spark 应用程序中，入口为：SparkContext，必须创建实例对象，加载数据和调度程序执行
    val sc: SparkContext = {
      // 创建SparkConf对象，设置应用相关信息，比如名称和master
      val sparkConf: SparkConf = new SparkConf()
        .setAppName(this.getClass.getSimpleName.stripSuffix("$"))
        .setMaster("local[2]")
      // 构建SparkContext实例对象，传递SparkConf
      new SparkContext(sparkConf)
    }
    // 2. 第一步、从LocalFS读取文件数据，sc.textFile方法，将数据封装到RDD中
    val inputRDD: RDD[String] = sc.textFile("datas/wordcount.data")
    // 3. 第二步、调用RDD中高阶函数，进行处理转换处理，函数：flapMap、map和reduceByKey
    val resultRDD: RDD[(String, Int)] = inputRDD
      // TODO: 过滤
      .filter(line => null != line && line.trim.length > 0)
      // a. 对每行数据按照分割符分割
      .flatMap(line => line.trim.split("\\s+"))
      // b. 将每个单词转换为二元组，表示出现一次
      .map(word => (word, 1))
      .reduceByKey((temp, item) => temp + item)
    // TODO: 将结果数据resultRDD保存至MySQL表中
    /*
      a. 对结果数据降低分区数目
      b. 针对每个分区数据进行操作
        每个分区数据插入数据库时，创建一个连接Connection
      c. 批次插入每个分区数据
        addBatch
        executeBatch
      d. 事务性
        手动提交事务，并且还原原来事务
     */
    resultRDD
      //对结果降低分区数目
      .coalesce(1)
      //针对每个分区数据进行操作
      .foreachPartition(iter => {
        val a: Iterator[(String, Int)] = iter
        saveToMySQLV1(iter)
      })
    sc.stop()
  }

  /**
   * 定义一个方法，将RDD中分区数据保存至MySQL表版本二(批量加事务—）
   */
  def saveToMySQLV2(iter: Iterator[(String, Int)]): Unit = {
    Class.forName("com.mysql.cj.jdbc.Driver")
    var conn: Connection = null
    var pstmt: PreparedStatement = null
    try {
      //构建连接
      conn = DriverManager.getConnection(
        "jdbc:mysql://node1.itcast.cn:3306/?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true",
        "root",
        "123456"
      )
      pstmt = conn.prepareCall("INSERT INTO db_test.tb_wordcount (word, count) VALUES(?, ?)")
      //todo:考虑事务性，一个分区的数据全部保存或者全部不保存
      val autocommit = conn.getAutoCommit //获取数据库默认事物提交方式
      conn.setAutoCommit(false)
      //插入数据
      iter.foreach {
        case (word, count) =>
          pstmt.setString(1, word)
          pstmt.setInt(2, count)
          //加入一个批次中
          pstmt.addBatch()
      }
      //批量执行批次
      pstmt.executeBatch()
      conn.commit() //手动提交事物，进行批量插入
      //还原数据库原来事物
      conn.setAutoCommit(autocommit)

    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      pstmt.close()
      conn.close()
    }
  }

  /**
   * 定义一个方法，将RDD中分区数据保存至MySQL表，第一个版本
   */
  def saveToMySQLV1(iter: Iterator[(String, Int)]): Unit = {
    //加载驱动类
    Class.forName("com.mysql.cj.jdbc.Driver")
    //声明变量
    var conn: Connection = null
    var pstmt: PreparedStatement = null
    try {
      //创建连接
      conn = DriverManager.getConnection(
        "jdbc:mysql://node1.itcast.cn:3306/?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true",
        "root",
        "123456"
      )
      pstmt = conn.prepareStatement("INSERT INTO db_test.tb_wordcount (word, count) VALUES(?, ?)")
      //插入数据
      iter.foreach { case (word, count) =>
        pstmt.setString(1, word)
        pstmt.setInt(2, count)
        pstmt.execute()
      }

    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (null != pstmt) pstmt.close()
      if (null != conn) conn.close()

    }

  }
}


