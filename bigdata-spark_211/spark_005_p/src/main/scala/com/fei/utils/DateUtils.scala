package com.fei.utils

import java.util.{Calendar, Date}

import org.apache.commons.lang3.time.FastDateFormat

/**
 * @description:日期时间工具类
 * @author: 飞
 * @date: 2020/11/24 0024 12:05
 */
object DateUtils {
  /**
   * 获取当前的日期，格式为:20190710
   */
  def getTodayDate(): String = {
    // a. 获取当前日期
    val nowDate = new Date()
    // b. 转换日期格式
    FastDateFormat.getInstance("yyyy-MM-dd").format(nowDate)
  }
  /**
   * 获取昨日的日期，格式为:20190710
   */
  def getYesterdayDate(): String = {
    // a. 获取Calendar对象
    val calendar: Calendar = Calendar.getInstance()
    // b. 获取昨日日期
    calendar.add(Calendar.DATE, -1)
    // c. 转换日期格式
    FastDateFormat.getInstance("yyyy-MM-dd").format(calendar)
  }
}
