package cn.itcast.logistics.common

import java.util.Date

import org.apache.commons.lang3.time.FastDateFormat

/**
 * 时间处理工具类
 */
object DateHelper {
	
	/**
	 * 返回昨天的时间
	 */
	def getYesterday(format: String): String = {
		val dateFormat = FastDateFormat.getInstance(format)
		//当前时间减去一天（昨天时间）
		dateFormat.format(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24))
	}
	
	/**
	 * 返回今天的时间
	 */
	def getToday(format: String): String = {
		//获取指定格式的当前时间
		FastDateFormat.getInstance(format).format(new Date)
	}
	
	def main(args: Array[String]): Unit = {
		println(s"yesterday: ${getYesterday("yyyy-MM-dd")}")
		println(s"today: ${getToday("yyyy-MM-dd")}")
	}
	
}
