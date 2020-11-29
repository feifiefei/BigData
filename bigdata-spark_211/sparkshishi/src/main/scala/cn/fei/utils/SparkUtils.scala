package cn.fei.utils
import cn.fei.config.ApplicationConfig
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
/**
 * @description:工具类：构建SparkSession和StreamingContext实例对象
 * @author: 飞
 * @date: 2020/11/27 0027 22:49
 */
object SparkUtils {
  /**
   * 获取SparkSession实例对象，传递Class对象
   * @param
   * @return SparkSession对象实例
   */
  def createSparkSession(clazz: Class[_]): SparkSession = {
    // 1. 构建SparkConf对象
    val sparkConf: SparkConf = new SparkConf()
      .setAppName(clazz.getSimpleName.stripSuffix("$"))
      .set("spark.debug.maxToStringFields", "2000")
      .set("spark.sql.debug.maxToStringFields", "2000")
    // 2. 判断应用是否本地模式运行，如果是设置值
    if(ApplicationConfig.APP_LOCAL_MODE){
      sparkConf
        .setMaster(ApplicationConfig.APP_SPARK_MASTER)
        // 设置Shuffle时分区数目
        .set("spark.sql.shuffle.partitions", "3") }
    // 3. 获取SparkSession实例对象
    val session: SparkSession = SparkSession
      .builder()
      .config(sparkConf)
      .getOrCreate()
    // 4. 返回实例
    session
  }
}
