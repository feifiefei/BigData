package com.fei.task;

import com.fei.bean.CleanBean;
import com.fei.bean.StockBean;
import com.fei.config.QuotConfig;
import com.fei.constant.KlineType;
import com.fei.function.KeyFunction;
import com.fei.function.MinStockWindowFunction;
import com.fei.inter.ProcessDataInterface;
import com.fei.map.StockKlineMap;
import com.fei.sink.SinkMysql;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/3 0003 20:21
 */
public class StockKlineTask implements ProcessDataInterface {
    @Override
    public void process(DataStream<CleanBean> waterData) {
        /**
         * 开发步骤：
         * 1.新建侧边流分支（周、月）
         * 2.数据分组
         * 3.划分时间窗口
         * 4.数据处理
         * 5.分流、封装侧边流数据
         * 6.编写插入sql
         * 7.（日、周、月）K线数据写入
         * 数据转换
         * 数据分组
         * 数据写入mysql
         */
        //新建侧边流分支
        //存周k数据
        OutputTag<StockBean> weekOpt = new OutputTag<>("weekOpt", TypeInformation.of(StockBean.class));
        //存月k数据
        OutputTag<StockBean> monthOpt = new OutputTag<>("monthOpt", TypeInformation.of(StockBean.class));
        //数据分组
        SingleOutputStreamOperator<StockBean> processData = waterData.keyBy(new KeyFunction())
                .timeWindow(Time.minutes(1))
                //数据处理
                .apply(new MinStockWindowFunction())
                //分流、封装侧边流数据
                .process(new ProcessFunction<StockBean, StockBean>() {

                    @Override
                    public void processElement(StockBean value, Context ctx, Collector<StockBean> out) throws Exception {
                        //日K
                        out.collect(value);
                        //周k
                        ctx.output(weekOpt, value);
                        ctx.output(monthOpt, value);
                    }
                });
        //编写sql
        String sql = "replace into %s values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        //k线数据写入
        //日k
        //StockBean =》Row
        processData.map(new StockKlineMap(KlineType.DAYK.getType(), KlineType.DAYK.getFirstTxDateType()))
                .keyBy(new KeySelector<Row, String>() {
                    @Override
                    public String getKey(Row value) throws Exception {
                        return value.getField(2).toString();
                    }
                })
                .addSink(new SinkMysql(String.format(sql, QuotConfig.config.getProperty("mysql.stock.sql.day.table"))));

        //周k
        processData.map(new StockKlineMap(KlineType.WEEKK.getType(), KlineType.WEEKK.getFirstTxDateType()))
                .keyBy(new KeySelector<Row, String>() {
                    @Override
                    public String getKey(Row value) throws Exception {
                        return value.getField(2).toString();
                    }
                }).addSink(new SinkMysql(String.format(sql, QuotConfig.config.getProperty("mysql.stock.sql.week.table"))));

        //月K
        processData.map(new StockKlineMap(KlineType.MONTHK.getType(), KlineType.MONTHK.getFirstTxDateType()))
                .keyBy(new KeySelector<Row, String>() {
                    @Override
                    public String getKey(Row value) throws Exception {
                        return value.getField(2).toString();
                    }
                }).addSink(new SinkMysql(String.format(sql, QuotConfig.config.getProperty("mysql.stock.sql.month.table"))));


    }
}
