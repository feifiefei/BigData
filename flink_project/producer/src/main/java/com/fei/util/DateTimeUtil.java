package com.fei.util;

import java.util.Calendar;

/**
 * @Date 2020/10/29
 */
public class DateTimeUtil {
    /**
     * 开发步骤：
     * 1.新建Calendar对象，设置日期
     * 2.设置开市时间
     * 3.设置闭市时间
     */

    public static long openTime = 0l; //开市时间
    public static long closeTime = 0l; //闭市时间

    static {

        //1.新建Calendar对象，设置日期
        Calendar calendar = Calendar.getInstance();

        //2.设置开市时间
        calendar.set(Calendar.HOUR_OF_DAY, 9); //24小时制的9点
        //calendar.set(Calendar.HOUR,9); //12小时值得9点
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);

        //获取开市时间
        openTime = calendar.getTime().getTime();

        //3.设置闭市时间
        calendar.set(Calendar.HOUR_OF_DAY, 23); //晚上学习得时候，时间设置大一点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        //获取闭市时间
        closeTime = calendar.getTime().getTime();
    }

    public static void main(String[] args) {

        System.out.println(openTime);
        System.out.println(closeTime);
    }

}
