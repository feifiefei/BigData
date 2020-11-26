package com.fei.report

import java.sql.{Connection, DriverManager, PreparedStatement}

import com.fei.config.ApplicationConfig
import org.apache.spark.internal.Logging
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StringType

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/24 0024 15:48
 */
object RegionStateReport extends Logging{
  def doReport(dataFrame: DataFrame) = {
    import dataFrame.sparkSession.implicits._
    //编写DSL分析
    //    dataFrame
    //      .groupBy($"province", $"city")
    //      .count()
    //      //添加一个字段
    //      .withColumn("report_date", date_sub(current_date(), 1).cast(StringType))
    //      .orderBy($"count".desc)
    val reportDF = dataFrame
      .groupBy($"data_str", $"province", $"city")
      .count()
      //重命名
      .withColumnRenamed("data_str", "report_date")
      .orderBy($"count".desc)
    //saveReportToMysql(reportDF)
    saveToMysql(reportDF)

  }

  /**
   * 自定义保存mysql，调用foreachPartition这个方法
   * @param dataframe
   */
  def saveToMysql(dataframe: DataFrame)={
    Class.forName(ApplicationConfig.MYSQL_JDBC_DRIVER)
    var conn: Connection = null
    var pstmt: PreparedStatement = null
    try{
      // 2. 获取连接
      conn = DriverManager.getConnection(
        ApplicationConfig.MYSQL_JDBC_URL, //
        ApplicationConfig.MYSQL_JDBC_USERNAME, //
        ApplicationConfig.MYSQL_JDBC_PASSWORD //
      )
      val insertSql =
        """
				  |INSERT INTO
				  |itcast_ads_report.region_stat_analysis(report_date, province, city, count)
				  |VALUES(?, ?, ?, ?)
				  |ON DUPLICATE KEY UPDATE count=VALUES(count)
				  |""".stripMargin
      pstmt = conn.prepareStatement(insertSql)

      // 获取当前数据库事务
      val autoCommit: Boolean = conn.getAutoCommit
      conn.setAutoCommit(false)

      // 3. 插入数据
      dataframe.foreach{row =>
        // TODO: 设置参数值
        pstmt.setString(1, row.getAs[String]("report_date"))
        pstmt.setString(2, row.getAs[String]("province"))
        pstmt.setString(3, row.getAs[String]("city"))
        pstmt.setLong(4, row.getAs[Long]("count"))

        // 加入批次
        pstmt.addBatch()
      }
      logWarning("开始批量插入Mysql")
      // 批量插入
      pstmt.executeBatch()

      // 手动提交
      conn.commit()
      conn.setAutoCommit(autoCommit) // 还原数据库原来事务状态
    }catch {
      case e: Exception => e.printStackTrace()
    }finally {
      // 4. 关闭连接
      if(null != pstmt) pstmt.close()
      if(null != conn) conn.close()

    }
  }


  /**
   * 内置保存mysql
   * @param dataFrame
   */
  def saveReportToMysql(dataFrame: DataFrame): Unit = {
    dataFrame
      .coalesce(1)
      .write
      .mode(SaveMode.Append)
      .format("jdbc")
      .option("driver", ApplicationConfig.MYSQL_JDBC_DRIVER)
      .option("url", ApplicationConfig.MYSQL_JDBC_URL)
      .option("user", ApplicationConfig.MYSQL_JDBC_USERNAME)
      .option("password", ApplicationConfig.MYSQL_JDBC_PASSWORD)
      .option("dbtable", "itcast_ads_report.region_start_analysis")
      .save()
  }

}
