package cn.itcast.logistics.common

import java.util.{Locale, ResourceBundle}

/**
 * 读取配置文件的工具类
 */
object Configuration {
	/**
	 * 定义配置文件操作的对象
	 */
	private lazy val resourceBundle: ResourceBundle = ResourceBundle.getBundle(
		"config", new Locale("zh", "CN")
	)
	private lazy val SEP = ":"
	
	// CDH-6.2.1
	lazy val BIGDATA_HOST: String = resourceBundle.getString("bigdata.host")
	
	// HDFS
	lazy val DFS_URI: String = resourceBundle.getString("dfs.uri")
	
	// Local FS
	lazy val LOCAL_FS_URI: String = resourceBundle.getString("local.fs.uri")
	
	// Kafka
	lazy val KAFKA_BROKER_HOST: String = resourceBundle.getString("kafka.broker.host")
	lazy val KAFKA_BROKER_PORT: Integer = Integer.valueOf(resourceBundle.getString("kafka.broker.port"))
	lazy val KAFKA_INIT_TOPIC: String = resourceBundle.getString("kafka.init.topic")
	lazy val KAFKA_LOGISTICS_TOPIC: String = resourceBundle.getString("kafka.logistics.topic")
	lazy val KAFKA_CRM_TOPIC: String = resourceBundle.getString("kafka.crm.topic")
	lazy val KAFKA_ADDRESS: String = KAFKA_BROKER_HOST + SEP + KAFKA_BROKER_PORT
	
	// Spark
	lazy val LOG_OFF = "OFF"
	lazy val LOG_DEBUG = "DEBUG"
	lazy val LOG_INFO = "INFO"
	lazy val LOCAL_HADOOP_HOME = "D:/BigdataUser/hadoop-3.0.0"
	lazy val SPARK_KAFKA_FORMAT = "kafka"
	lazy val SPARK_KUDU_FORMAT = "kudu"
	lazy val SPARK_ES_FORMAT = "es"
	lazy val SPARK_CLICK_HOUSE_FORMAT = "clickhouse"
	
	// ZooKeeper
	lazy val ZOOKEEPER_HOST: String = resourceBundle.getString("zookeeper.host")
	lazy val ZOOKEEPER_PORT: Integer = Integer.valueOf(resourceBundle.getString("zookeeper.port"))
	
	// Kudu
	lazy val KUDU_RPC_HOST: String = resourceBundle.getString("kudu.rpc.host")
	lazy val KUDU_RPC_PORT: Integer = Integer.valueOf(resourceBundle.getString("kudu.rpc.port"))
	lazy val KUDU_HTTP_HOST: String = resourceBundle.getString("kudu.http.host")
	lazy val KUDU_HTTP_PORT: Integer = Integer.valueOf(resourceBundle.getString("kudu.http.port"))
	lazy val KUDU_RPC_ADDRESS: String = KUDU_RPC_HOST + SEP + KUDU_RPC_PORT
	
	// ClickHouse
	lazy val CLICK_HOUSE_DRIVER: String = resourceBundle.getString("clickhouse.driver")
	lazy val CLICK_HOUSE_URL: String = resourceBundle.getString("clickhouse.url")
	lazy val CLICK_HOUSE_USER: String = resourceBundle.getString("clickhouse.user")
	lazy val CLICK_HOUSE_PASSWORD: String = resourceBundle.getString("clickhouse.password")
	
	// ElasticSearch
	lazy val ELASTICSEARCH_HOST: String = resourceBundle.getString("elasticsearch.host")
	lazy val ELASTICSEARCH_RPC_PORT: Integer = Integer.valueOf(resourceBundle.getString("elasticsearch.rpc.port"))
	lazy val ELASTICSEARCH_HTTP_PORT: Integer = Integer.valueOf(resourceBundle.getString("elasticsearch.http.port"))
	lazy val ELASTICSEARCH_ADDRESS: String = ELASTICSEARCH_HOST + SEP + ELASTICSEARCH_HTTP_PORT
	
	// Azkaban
	lazy val IS_FIRST_RUNNABLE: java.lang.Boolean = java.lang.Boolean.valueOf(resourceBundle.getString("app.first.runnable"))
	
	// ## Data path of ETL program output ##
	// # Run in the yarn mode in Linux
	lazy val SPARK_APP_DFS_CHECKPOINT_DIR: String = resourceBundle.getString("spark.app.dfs.checkpoint.dir") // /apps/logistics/dat-hdfs/spark-checkpoint
	lazy val SPARK_APP_DFS_DATA_DIR: String = resourceBundle.getString("spark.app.dfs.data.dir") // /apps/logistics/dat-hdfs/warehouse
	lazy val SPARK_APP_DFS_JARS_DIR: String = resourceBundle.getString("spark.app.dfs.jars.dir") // /apps/logistics/jars
	
	// # Run in the local mode in Linux
	lazy val SPARK_APP_LOCAL_CHECKPOINT_DIR: String = resourceBundle.getString("spark.app.local.checkpoint.dir") // /apps/logistics/dat-local/spark-checkpoint
	lazy val SPARK_APP_LOCAL_DATA_DIR: String = resourceBundle.getString("spark.app.local.data.dir") // /apps/logistics/dat-local/warehouse
	lazy val SPARK_APP_LOCAL_JARS_DIR: String = resourceBundle.getString("spark.app.local.jars.dir") // /apps/logistics/jars
	
	// # Running in the local Mode in Windows
	lazy val SPARK_APP_WIN_CHECKPOINT_DIR: String = resourceBundle.getString("spark.app.win.checkpoint.dir") // D://apps/logistics/dat-local/spark-checkpoint
	lazy val SPARK_APP_WIN_DATA_DIR: String = resourceBundle.getString("spark.app.win.data.dir") // D://apps/logistics/dat-local/warehouse
	lazy val SPARK_APP_WIN_JARS_DIR: String = resourceBundle.getString("spark.app.win.jars.dir") // D://apps/logistics/jars
	
	// # Oracle JDBC & # MySQL JDBC
	lazy val DB_ORACLE_URL: String = resourceBundle.getString("db.oracle.url")
	lazy val DB_ORACLE_USER: String = resourceBundle.getString("db.oracle.user")
	lazy val DB_ORACLE_PASSWORD: String = resourceBundle.getString("db.oracle.password")
	
	lazy val DB_MYSQL_DRIVER: String = resourceBundle.getString("db.mysql.driver")
	lazy val DB_MYSQL_URL: String = resourceBundle.getString("db.mysql.url")
	lazy val DB_MYSQL_USER: String = resourceBundle.getString("db.mysql.user")
	lazy val DB_MYSQL_PASSWORD: String = resourceBundle.getString("db.mysql.password")
	
}
