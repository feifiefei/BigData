package com.fei.config

import com.typesafe.config.{Config, ConfigFactory}

/**
 * @description:加载应用Application属性配置文件config.properties获取属性值
 * @author: 飞
 * @date: 2020/11/24 0024 11:14
 */
object ApplicationConfig {
  // 加载属性文件
  private val config: Config = ConfigFactory.load("config.properties")
  /*
  运行模式，开发测试为本地模式，测试生产通过--master传递
  */
  lazy val APP_LOCAL_MODE: Boolean = config.getBoolean("app.is.local")
  lazy val APP_SPARK_MASTER: String = config.getString("app.spark.master")
  /*
  是否集成Hive及Hive MetaStore地址信息
  */
  lazy val APP_IS_HIVE: Boolean = config.getBoolean("app.is.hive")
  lazy val APP_HIVE_META_STORE_URLS: String = config.getString("app.hive.metastore.uris")
  /*
  数据库连接四要素信息
  */
  lazy val MYSQL_JDBC_DRIVER: String = config.getString("mysql.jdbc.driver")
  lazy val MYSQL_JDBC_URL: String = config.getString("mysql.jdbc.url")
  lazy val MYSQL_JDBC_USERNAME: String = config.getString("mysql.jdbc.username")
  lazy val MYSQL_JDBC_PASSWORD: String = config.getString("mysql.jdbc.password")
  // 数据文件存储路径
  lazy val DATAS_PATH: String = config.getString("datas.path")
  // 解析IP地址字典数据文件存储路径
  lazy val IPS_DATA_REGION_PATH: String = config.getString("ipdata.region.path")

}
