package cn.itcast.logistics.common

import org.apache.commons.lang3.SystemUtils
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * Spark 操作的工具类
 */
object SparkUtils {
	
	/**
	 * 创建SparkConf对象
	 */
	lazy val sparkConf = (clazz: Class[_]) => {
		val conf: SparkConf = new SparkConf()
			//设置应用的名称
			.set("spark.app.name", clazz.getSimpleName.stripSuffix("$"))
			//设置时区
			.set("spark.sql.session.timeZone", "Asia/Shanghai")
			//设置单个分区可容纳的最大字节数，默认是128M， 等同于block块的大小
			.set("spark.sql.files.maxPartitionBytes", "134217728")
			//设置合并小文件的阈值，避免每个小文件占用一个分区的情况
			.set("spark.sql.files.openCostInBytes", "134217728")
			//设置join操作时可以广播到worker节点的最大字节大小，可以避免shuffle操作
			.set("spark.sql.autoBroadcastJoinThreshold", "67108864")
		//返回sparkConf对象
		conf
	}
	
	/**
	 * 预定义可用于window和linux中的运行模式
	 */
	lazy val autoSettingEnv = (sparkConf: SparkConf) =>{
		//本地运行环境
		if(SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_MAC){
			//本地环境LOCAL_HADOOP_HOME
			System.setProperty("hadoop.home.dir", Configuration.LOCAL_HADOOP_HOME)
			//设置运行环境和checkpoint路径
			sparkConf
				.set("spark.master", "local[*]")
				.set("spark.sql.streaming.checkpointLocation", Configuration.SPARK_APP_WIN_CHECKPOINT_DIR)
				.set("spark.sql.warehouse.dir", Configuration.SPARK_APP_WIN_DATA_DIR)
				//设置join或者shuffle的时候使用的分区数，默认情况下分区数是200
				.set("spark.sql.shuffle.partitions", "4")
		}else{
			//集群运行环境（生产环境），使用spark-submit提交应用时，通过 --conf spark.sql.shuffle.partitions设置分区数目
			sparkConf.set("spark.master", "yarn")
				.set("spark.sql.streaming.checkpointLocation", Configuration.SPARK_APP_DFS_CHECKPOINT_DIR)
				.set("spark.sql.warehouse.dir", Configuration.SPARK_APP_DFS_DATA_DIR)
		}
		//返回sparkConf对象
		sparkConf
	}
	
	/**
	 * 创建sparkSession对象
	 * @param sparkConf
	 */
	def getSparkSession(sparkConf: SparkConf): SparkSession = {
		SparkSession.builder()
			.config(sparkConf)
			.getOrCreate()
	}
	
}
