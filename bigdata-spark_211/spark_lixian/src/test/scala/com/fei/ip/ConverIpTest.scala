package com.fei.ip

import org.lionsoul.ip2region.{DataBlock, DbConfig, DbSearcher}

/**
 * @description:测试iP2REgin
 * @author: 飞
 * @date: 2020/11/24 0024 11:47
 */
object ConverIpTest {
  def main(args: Array[String]): Unit = {
    //指定字典位置
    val dbSearcher = new DbSearcher(new DbConfig(), "dataset/ip2region.db")
    //传递ip
    val dataBlock: DataBlock = dbSearcher.btreeSearch("222.17.255.76")
    //解析
    val region: String = dataBlock.getRegion
    //    println(region)

    val Array(_, _, pronvice, city, _) = region.split("\\|")
    println(s"省份：${pronvice},城市：${city}")
  }
}
