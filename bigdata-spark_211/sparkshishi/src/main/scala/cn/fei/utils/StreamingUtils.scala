package cn.fei.utils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.streaming.StreamingContext

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/27 0027 23:14
 */
object StreamingUtils {
  /**
   * 当应用启动以后，循环判断 HDFS上目录下某个文件（监控文件）是否存在，如果存在就优雅停止应用
   * -a). 启动流式应用时创建目录
   * ${HADOOP_HOME}/bin/hdfs dfs -mkdir -p /spark/streaming
   * -b). 关闭应用时，HDFS创建文件stop
   * ${HADOOP_HOME}/bin/hdfs dfs -touchz /spark/streaming/stop
   * -c). 启动应用时，删除文件stop
   * ${HADOOP_HOME}/bin/hdfs dfs -rm -R /spark/streaming/stop
   */
  def stopStreaming(ssc: StreamingContext, monitorFile: String): Unit = {}
  /**
   * 当应用启动以后，循环判断 HDFS上目录下某个文件（监控文件）是否存在，如果存在就优雅停止应用
   * -a). 启动流式应用时创建目录
   * ${HADOOP_HOME}/bin/hdfs dfs -mkdir -p /spark/order-apps/stop
   * -b). 关闭应用时，HDFS创建文件stop
   * ${HADOOP_HOME}/bin/hdfs dfs -touchz /spark/order-apps/stop/etl-stop
   * -c). 启动应用时，删除文件stop
   * ${HADOOP_HOME}/bin/hdfs dfs -rm -R /spark/order-apps/stop/etl-stop
   */
  def stopStructuredStreaming(query: StreamingQuery, monitorFile: String): Unit = {}
  /**
   * 判断是否存在 mark file
   * @param
   */
//  def isExistsMonitorFile(monitorFile: String, conf: Configuration): Boolean={}
}
