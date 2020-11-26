package com.fei.report

import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.{DataFrame, SaveMode}

/**
 * 广告区域统计：ads_region_analysis，区域维度：省份和城市
 */
object AdsRegionAnalysisReport {

  /*
  不同业务报表统计分析时，两步骤：
    i. 编写SQL分析
    ii. 将分析结果保存MySQL数据库表中
  */
  def doReport(dataframe: DataFrame): Unit = {
    val session = dataframe.sparkSession
    import session.implicits._

    // ======================== 1. 计算基本指标 ========================
    /*
      val resultDF: DataFrame = doReportWithDsl(dataframe)
      saveReportToMySQL(resultDF)
      System.exit(-1)
    */
    // 注册为临时视图
    dataframe.createOrReplaceTempView("tmp_view_pmt")
    // 编写SQL并执行
    val reportDF: DataFrame = session.sql(
      ReportSQLConstant.reportAdsRegionKpiSQL("tmp_view_pmt")
    )
    // 保存至MySQL数据库表中
    saveReportToMySQL(reportDF)
  }

  /**
   * 基于DSL（调用Dataset API函数）分析数据
   */
  def doReportWithDsl(dataframe: DataFrame): DataFrame = {
    // 获取SparkSession实例对象，并导入隐式转换
    val session = dataframe.sparkSession
    import session.implicits._
    // 导入SparkSQL函数库
    import org.apache.spark.sql.functions._

    val reportDF: DataFrame = dataframe
      // 1. 按照地域信息分组：province和city
      .groupBy($"province", $"city")
      // 2. 对组内数据进行聚合操作
      .agg(
        // 原始请求数量
        sum(
          when(
            $"requestmode" === 1 and $"processnode" >= 1, // 条件表达式，返回boolean
            1 // 条件表达式：true时值
          ).otherwise(0) // 条件表达式： false时值
        ).as("orginal_req_cnt"),
        // 有效请求：requestmode = 1 and processnode >= 2
        sum(
          when(
            $"requestmode".equalTo(1).and($"processnode".geq(2)),
            1
          ).otherwise(0)
        ).as("valid_req_cnt"),
        // 广告请求：requestmode = 1 and processnode = 3
        sum(
          when($"requestmode".equalTo(1)
            .and($"processnode".equalTo(3)), 1
          ).otherwise(0)
        ).as("ad_req_cnt"),
        // 参与竞价数
        sum(
          when($"adplatformproviderid".geq(100000)
            .and($"iseffective".equalTo(1))
            .and($"isbilling".equalTo(1))
            .and($"isbid".equalTo(1))
            .and($"adorderid".notEqual(0)), 1
          ).otherwise(0)
        ).as("join_rtx_cnt"),
        // 竞价成功数
        sum(
          when($"adplatformproviderid".geq(100000)
            .and($"iseffective".equalTo(1))
            .and($"isbilling".equalTo(1))
            .and($"iswin".equalTo(1))
            .and($"adorderid".notEqual(0)), 1
          ).otherwise(0)
        ).as("success_rtx_cnt"),
        // 广告主展示数: requestmode = 2 and iseffective = 1
        sum(
          when($"requestmode".equalTo(2)
            .and($"iseffective".equalTo(1)), 1
          ).otherwise(0)
        ).as("ad_show_cnt"),
        // 广告主点击数: requestmode = 3 and iseffective = 1 and adorderid != 0
        sum(
          when($"requestmode".equalTo(3)
            .and($"iseffective".equalTo(1))
            .and($"adorderid".notEqual(0)), 1
          ).otherwise(0)
        ).as("ad_click_cnt"),
        // 媒介展示数
        sum(
          when($"requestmode".equalTo(2)
            .and($"iseffective".equalTo(1))
            .and($"isbilling".equalTo(1))
            .and($"isbid".equalTo(1))
            .and($"iswin".equalTo(1)), 1
          ).otherwise(0)
        ).as("media_show_cnt"),
        // 媒介点击数
        sum(
          when($"requestmode".equalTo(3)
            .and($"iseffective".equalTo(1))
            .and($"isbilling".equalTo(1))
            .and($"isbid".equalTo(1))
            .and($"iswin".equalTo(1)), 1
          ).otherwise(0)
        ).as("media_click_cnt"),
        // DSP 广告消费
        sum(
          when($"adplatformproviderid".geq(100000)
            .and($"iseffective".equalTo(1))
            .and($"isbilling".equalTo(1))
            .and($"iswin".equalTo(1))
            .and($"adorderid".gt(200000))
            .and($"adcreativeid".gt(200000)), floor($"winprice" / 1000)
          ) otherwise (0)
        ).as("dsp_pay_money"),
        // DSP广告成本
        sum(
          when($"adplatformproviderid".geq(100000)
            .and($"iseffective".equalTo(1))
            .and($"isbilling".equalTo(1))
            .and($"isbid".equalTo(1))
            .and($"iswin".equalTo(1))
            .and($"adorderid".gt(200000))
            .and($"adcreativeid".gt(200000)), floor($"adpayment" / 1000)
          ) otherwise (0)
        ).as("dsp_cost_money")
      )
      // 3. 过滤非0数据，计算三率
      .where(
        $"join_rtx_cnt" =!= 0 and $"success_rtx_cnt" =!= 0 and
          $"ad_show_cnt" =!= 0 and $"ad_click_cnt" =!= 0 and
          $"media_show_cnt" =!= 0 and $"media_click_cnt" =!= 0
      )
      // 4. 计算三率
      .withColumn(
        "success_rtx_rate", //
        floor($"success_rtx_cnt" / $"join_rtx_cnt") // 保留两位有效数字
      )
      .withColumn(
        "ad_click_rate", //
        floor($"ad_click_cnt" / $"ad_show_cnt") // 保留两位有效数字
      )
      .withColumn(
        "media_click_rate", //
        floor($"media_click_cnt" / $"media_show_cnt") // 保留两位有效数字
      )
      // 5. 增加报表数据日期：report_date
      .withColumn(
        "report_date", //
        date_sub(current_date(), 1).cast(StringType)
      )
    //reportDF.printSchema()
    //reportDF.show(10, truncate = false)

    // 返回报表的数据
    reportDF
  }

  /*
  不同业务报表统计分析时，两步骤：
    i. 编写SQL分析
    ii. 将分析结果保存MySQL数据库表中
  */
  def doReportRate(dataframe: DataFrame): Unit = {
    val session = dataframe.sparkSession
    import session.implicits._

    // ======================== 1. 计算基本指标 ========================
    // 注册为临时视图
    dataframe.createOrReplaceTempView("tmp_view_pmt")
    // 编写SQL并执行
    val reportDF: DataFrame = session.sql(
      ReportSQLConstant.reportAdsRegionSQL("tmp_view_pmt")
    )
    // ======================== 2. 计算三率 ========================
    reportDF.createOrReplaceTempView("tmp_view_report")
    val resultDF: DataFrame = session.sql(
      ReportSQLConstant.reportAdsRegionRateSQL("tmp_view_report")
    )
    resultDF.printSchema()
    resultDF.show(20, truncate = false)

    // 保存至MySQL数据库表中
    saveReportToMySQL(resultDF)
  }

  /**
   * 将报表数据保存MySQL数据库，使用SparkSQL自带数据源API接口
   */
  def saveReportToMySQL(reportDF: DataFrame): Unit = {
    reportDF.write
      // TODO: 采用Append时，如果多次运行报表执行，主键冲突，所以不可用
      .mode(SaveMode.Append)
      // TODO：采用Overwrite时，将会覆盖以前所有报表，更加不可取
      //.mode(SaveMode.Overwrite)
      .format("jdbc")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("url", "jdbc:mysql://node1.itcast.cn:3306/?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true")
      .option("user", "root")
      .option("password", "123456")
      .option("dbtable", "itcast_ads_report.ads_region_analysis")
      .save()
  }

}
