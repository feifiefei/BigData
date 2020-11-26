package com.fei.report

import com.fei.config.ApplicationConfig
import com.fei.utils.SparkUtils
import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.functions.{current_date, date_sub}
import org.apache.spark.sql.types.StringType
import org.apache.spark.storage.StorageLevel

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/24 0024 15:23
 */
/**
 * 针对广告点击数据，依据需求进行报表开发，具体说明如下：
 * - 各地域分布统计：region_stat_analysis
 * - 广告区域统计：ads_region_analysis
 * - 广告APP统计：ads_app_analysis
 * - 广告设备统计：ads_device_analysis
 * - 广告网络类型统计：ads_network_analysis
 * - 广告运营商统计：ads_isp_analysis
 * - 广告渠道统计：ads_channel_analysis
 */
object PmtReportRunner {
  def main(args: Array[String]): Unit = {
    System.setProperty("user.name", "root")
    System.setProperty("HADOOP_USER_NAME", "root")
    // 1. 创建SparkSession实例对象
    val spark = SparkUtils.createSparkSession(this.getClass)
    import spark.implicits._
    // 2. 从Hive表中加载广告ETL数据，日期过滤
    val pmtDF: Dataset[Row] = spark.read
      .format("hive")
      .table("itcast_ads.pmt_info")
      .where($"date_str".equalTo(date_sub(current_date(), 1).cast(StringType)))

    // 3. 依据不同业务需求开发报表
    /*
    不同业务报表统计分析时，两步骤：
    i. 编写SQL或者DSL分析
    ii. 将分析结果保存MySQL数据库表中
    */
    //判断是否有数据，如果没有数据，结束进程
    if (pmtDF.isEmpty) System.exit(-1)
    //缓存
    pmtDF.persist(StorageLevel.MEMORY_AND_DISK)
    // 3.1. 地域分布统计：region_stat_analysis
    RegionStateReport.doReport(pmtDF)
    // 3.2. 广告区域统计：ads_region_analysis
    AdsRegionAnalysisReport.doReport(pmtDF)
    // 4. 应用结束，关闭资源
    pmtDF.unpersist()
    spark.stop()
  }
}
