package cn.fei.query

import cn.fei.sougou.SogouRecord
import com.hankcs.hanlp.HanLP
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/22 0022 8:48
 */
/**
 * 用户查询日志(SogouQ)分析，数据来源Sogou搜索引擎部分网页查询需求及用户点击情况的网页查询日志数据集合。
 * 1. 搜索关键词统计，使用HanLP中文分词
 * 2. 用户搜索次数统计
 * 3. 搜索时间段统计
 * 数据格式：
 * 访问时间\t用户ID\t[查询词]\t该URL在返回结果中的排名\t用户点击的顺序号\t用户点击的URL
 * 其中，用户ID是根据用户使用浏览器访问搜索引擎时的Cookie信息自动赋值，即同一次使用浏览器输入的不同查询对应同一个用户ID
 **/
object SogouQueryAnalysis {
  def main(args: Array[String]): Unit = {
    // 在Spark 应用程序中，入口为：SparkContext，必须创建实例对象，加载数据和调度程序执行
    val sc: SparkContext = {
      // 创建SparkConf对象，设置应用相关信息，比如名称和master
      val sparkConf: SparkConf = new SparkConf()
        .setAppName(this.getClass.getSimpleName.stripSuffix("$"))
        .setMaster("local[2]")
      // 构建SparkContext实例对象，传递SparkConf
      new SparkContext(sparkConf)
    }
    //从本地文件系统读取日志
    val rawLogsRDD: RDD[String] = sc.textFile("datas/SogouQ.sample", minPartitions = 2)
    //    println(s"Count = ${rawLogsRDD.count()}")
    //    println(s"First: \n\t${rawLogsRDD.first()}")
    //解析数据（先过滤不合格数据），封装样例类SogouRecord对象
    val recordsRDD: RDD[SogouRecord] = rawLogsRDD
      .filter(line => null != line && line.trim.split("\\s+").length == 6)
      .mapPartitions { iter =>
        iter.map { line =>
          val arr: Array[String] = line.trim.split("\\s+")
          //构建SogouRecord
          SogouRecord(
            arr(0), arr(1), arr(2).replaceAll("\\[|\\]", ""),
            arr(3).toInt, arr(4).toInt, arr(5)
          )
        }
      }
    //    println(s"Count = ${recordsRDD.count()}")
    //    println(recordsRDD.first())
    //考虑到后续对解析封装RDD进行多次使用，所以进行持久化缓存
    recordsRDD.persist(StorageLevel.MEMORY_AND_DISK).count() //count函数，触发缓存
    //需求分析
    /**
     * 需求一：搜索关键词统计，使用HanLP中文分次
     */
    val top10SearchArray: Array[(String, Int)] = recordsRDD
      .flatMap { record =>
        val queryWords: String = record.queryWords
        //使用HanLP进行分词
        val terms = HanLP.segment(queryWords)
        //获取每个分词
        import scala.collection.JavaConverters._
        terms.asScala.map(term => term.word)
      } //转化为二元组，表示每个搜索关键词出现一次
      .mapPartitions(iter => iter.map(word => (word, 1)))
      //聚合
      .reduceByKey((tmp, item) => tmp + item)
      //降序排序
      .sortBy(tuple => tuple._2, ascending = false)
      .take(10)
    top10SearchArray.foreach(println)
  }
}
