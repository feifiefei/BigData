package cn.fei.task;

import cn.fei.bean.CleanBean;
import cn.fei.bean.StockBean;
import cn.fei.config.QuotConfig;
import cn.fei.constant.KlineType;
import cn.fei.function.KeyFunction;
import cn.fei.function.MinStockWindowFunction;
import cn.fei.inter.ProcessDataInterface;
import cn.fei.map.StockKlineMap;
import cn.fei.sink.SinkMysql;
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
 * @Date 2020/11/3
 * 个股K线
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

        //1.新建侧边流分支（周、月）
        //存周K数据
        OutputTag<StockBean> weekOpt = new OutputTag<>("weekOpt", TypeInformation.of(StockBean.class));
        //存月K数据
        OutputTag<StockBean> monthOpt = new OutputTag<>("monthOpt", TypeInformation.of(StockBean.class));
        //2.数据分组
        SingleOutputStreamOperator<StockBean> processData = waterData.keyBy(new KeyFunction())
                .timeWindow(Time.minutes(1)) //窗口是时间1分钟
                //4.数据处理
                .apply(new MinStockWindowFunction())
                //5.分流、封装侧边流数据
                .process(new ProcessFunction<StockBean, StockBean>() {
                    @Override
                    public void processElement(StockBean value, Context ctx, Collector<StockBean> out) throws Exception {

                        //日K
                        out.collect(value);
                        //周K
                        ctx.output(weekOpt, value);
                        //月K
                        ctx.output(monthOpt, value);
                    }
                });
        //6.编写插入sql,%s ：格式化转换符
        String sql = "replace into %s values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        /**
         * 7.（日、周、月）K线数据写入
         * 数据转换
         * 数据分组
         * 数据写入mysql
         */
        // 7.（日、周、月）K线数据写入
        //日K
        //StockBean => Row
        processData.map(new StockKlineMap(KlineType.DAYK.getType(),KlineType.DAYK.getFirstTxDateType()))
                .keyBy(new KeySelector<Row, String>() {
                    @Override
                    public String getKey(Row value) throws Exception {
                        return value.getField(2).toString();
                    }
                }).addSink(new SinkMysql(String.format(sql,QuotConfig.config.getProperty("mysql.stock.sql.day.table"))));

        //周k
        processData.map(new StockKlineMap(KlineType.WEEKK.getType(),KlineType.WEEKK.getFirstTxDateType()))
                .keyBy(new KeySelector<Row, String>() {
                    @Override
                    public String getKey(Row value) throws Exception {
                        return value.getField(2).toString();
                    }
                }).addSink(new SinkMysql(String.format(sql, QuotConfig.config.getProperty("mysql.stock.sql.week.table"))));

        //月K
        processData.map(new StockKlineMap(KlineType.MONTHK.getType(),KlineType.MONTHK.getFirstTxDateType()))
                .keyBy(new KeySelector<Row, String>() {
                    @Override
                    public String getKey(Row value) throws Exception {
                        return value.getField(2).toString();
                    }
                }).addSink(new SinkMysql(String.format(sql,QuotConfig.config.getProperty("mysql.stock.sql.month.table"))));



    }
}
