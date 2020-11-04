package com.fei.map;

import com.fei.bean.StockBean;
import com.fei.config.QuotConfig;
import com.fei.constant.Constant;
import com.fei.util.DateUtil;
import org.apache.commons.net.ntp.TimeStamp;
import org.apache.flink.api.common.functions.RichMapFunction;

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/2 0002 17:22
 */
public class StockPutHdfsMap extends RichMapFunction<StockBean, String> {
    //定义字符串分割字段字符
    String sp = QuotConfig.config.getProperty("hdfs.seperator");

    @Override
    public String map(StockBean value) throws Exception {
        /**
         * 开发步骤:
         * 1.定义字符串字段分隔符
         * 2.日期转换和截取：date类型
         * 3.新建字符串缓存对象
         * 4.封装字符串数据
         *
         * 字符串拼装字段顺序：
         * Timestamp|date|secCode|secName|preClosePrice|openPirce|highPrice|
         * lowPrice|closePrice|tradeVol|tradeAmt|tradeVolDay|tradeAmtDay|source
         */
        //2.日期转换和截取：date类型,离线预警是按照日进行统计和分析的，所以需要格式化成date类（yyyy-MM-dd）
        String trideDate = DateUtil.longTimeToString(value.getEventTime(), Constant.format_YYYYMMDDHHMMSS);
        //新建字符串缓存对象
        StringBuilder builder = new StringBuilder();
        //封装字符串数据库
        builder.append(new TimeStamp(value.getEventTime())).append(sp)
                .append(trideDate).append(sp)
                .append(value.getSecName()).append(sp)
                .append(value.getPreClosePrice()).append(sp)
                .append(value.getOpenPrice()).append(sp)
                .append(value.getHighPrice()).append(sp)
                .append(value.getLowPrice()).append(sp)
                .append(value.getClosePrice()).append(sp)
                .append(value.getTradeVol()).append(sp)
                .append(value.getTradeAmt()).append(sp)
                .append(value.getTradeVolDay()).append(sp)
                .append(value.getTradeAmtDay()).append(sp)
                .append(value.getSource());
        return builder.toString();

    }
}
