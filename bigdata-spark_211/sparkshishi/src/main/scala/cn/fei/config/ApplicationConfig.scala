package cn.fei.config

import com.typesafe.config.{Config, ConfigFactory}

/**
 * @description:加载应用Application属性配置文件config.properties获取属性值
 * @author: 飞
 * @date: 2020/11/27 0027 22:47
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
  Kafka 相关配置信息
  */
  lazy val KAFKA_BOOTSTRAP_SERVERS: String = config.getString("kafka.bootstrap.servers")
  lazy val KAFKA_AUTO_OFFSET_RESET: String = config.getString("kafka.auto.offset.reset")
  lazy val KAFKA_SOURCE_TOPICS: String = config.getString("kafka.source.topics")
  lazy val KAFKA_ETL_TOPIC: String = config.getString("kafka.etl.topic")
  lazy val KAFKA_MAX_OFFSETS: String = config.getString("kafka.max.offsets.per.trigger")
  lazy val KAFKA_ZK_URL: String = config.getString("kafka.zk.url")
  lazy val STREAMING_ETL_GROUP_ID: String = config.getString("streaming.etl.group.id")
  /*
  Streaming流式应用，检查点目录
  */
  lazy val STREAMING_ETL_CKPT: String = config.getString("streaming.etl.ckpt")
  lazy val STREAMING_HBASE_CKPT: String = config.getString("streaming.hbase.ckpt")
  lazy val STREAMING_ES_CKPT: String = config.getString("streaming.es.ckpt")
  lazy val STREAMING_AMT_TOTAL_CKPT: String = config.getString("streaming.amt.total.ckpt")
  lazy val STREAMING_AMT_PROVINCE_CKPT: String = config.getString("streaming.amt.province.ckpt")
  lazy val STREAMING_AMT_CITY_CKPT: String = config.getString("streaming.amt.city.ckpt")
  /*
  Streaming流式应用，停止文件
  */
  lazy val STOP_ETL_FILE: String = config.getString("stop.etl.file")
  lazy val STOP_HBASE_FILE: String = config.getString("stop.hbase.file")
  lazy val STOP_ES_FILE: String = config.getString("stop.es.file")
  lazy val STOP_STATE_FILE: String = config.getString("stop.state.file")
  /*
  HBase 数据库连接信息及表的信息
  */
  lazy val HBASE_ZK_HOSTS: String = config.getString("hbase.zk.hosts")
  lazy val HBASE_ZK_PORT: String = config.getString("hbase.zk.port")
  lazy val HBASE_ZK_ZNODE: String = config.getString("hbase.zk.znode")
  lazy val HBASE_ORDER_TABLE: String = config.getString("hbase.order.table")
  lazy val HBASE_ORDER_TABLE_FAMILY: String = config.getString("hbase.table.family")
  lazy val HBASE_ORDER_TABLE_COLUMNS: Array[String] = config.getString("hbase.table.columns").split(",")
  /*
  Elasticsearch 连接信息
  */
  lazy val ES_NODES: String = config.getString("es.nodes")
  lazy val ES_PORT: String = config.getString("es.port")
  lazy val ES_INDEX_AUTO_CREATE: String = config.getString("es.index.auto.create")
  lazy val ES_WRITE_OPERATION: String = config.getString("es.write.operation")
  lazy val ES_INDEX_NAME: String = config.getString("es.index.name")
  lazy val ES_MAPPING_ID: String = config.getString("es.mapping.id")
  /*
  Redis 数据库
  */
  lazy val REDIS_HOST: String = config.getString("redis.host")
  lazy val REDIS_PORT: String = config.getString("redis.port")
  lazy val REDIS_DB: String = config.getString("redis.db")
  // 解析IP地址字典数据文件存储路径
  lazy val IPS_DATA_REGION_PATH: String = config.getString("ipdata.region.path")
}
