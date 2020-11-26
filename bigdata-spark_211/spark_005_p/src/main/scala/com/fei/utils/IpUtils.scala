package com.fei.utils

import com.fei.etl.Region
import com.fei.config.ApplicationConfig
import org.lionsoul.ip2region.{DataBlock, DbConfig, DbSearcher}

/**
 * @description:IP地址解析工具类
 * @author: 飞
 * @date: 2020/11/24 0024 11:53
 */
/**
 * IP地址解析为省份和城市
 *
 * @return Region 省份城市信息
 */

object IpUtils {
  def convertIpToRegion(ip: String, dbSearcher: DbSearcher): Region = {
    // a. 依据IP地址解析
    val dataBlock = dbSearcher.btreeSearch(ip)
    val region: String = dataBlock.getRegion

    // 中国|0|海南省|海口市|教育网
    // b. 分割字符串，获取省份和城市
    val Array(_, _, province, city, _) = region.split("\\|")
    // c. 返回Region对象
    Region(ip, province, city)
  }
}
