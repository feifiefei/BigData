package cn.fei.task;

import cn.fei.bean.CleanBean;
import cn.fei.bean.IndexBean;
import cn.fei.config.QuotConfig;
import cn.fei.constant.KlineType;
import cn.fei.function.KeyFunction;
import cn.fei.function.MinIndexWindowFunction;
import cn.fei.inter.ProcessDataInterface;
import cn.fei.map.IndexKlineMap;
import cn.fei.sink.SinkMysql;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.types.Row;

/**
 * @Date 2020/11/3
 */
public class IndexKlineTask implements ProcessDataInterface {
    @Override
    public void process(DataStream<CleanBean> waterData) {
        /**
         * 开发步骤：
         * 1.数据分组
         * 2.划分时间窗口
         * 3.数据处理
         * 4.编写插入sql
         * 5.（日、周、月）K线数据写入
         * 数据转换、分组
         * 数据写入mysql
         */
        //1.数据分组
        SingleOutputStreamOperator<IndexBean> applyData = waterData.keyBy(new KeyFunction())
                .timeWindow(Time.minutes(1))
                //3.数据处理
                .apply(new MinIndexWindowFunction());

        //4.编写插入sql
        String sql = "replace into %s values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        //5.（日、周、月）K线数据写入
        //日
        applyData.map(new IndexKlineMap(KlineType.DAYK.getType(), KlineType.DAYK.getFirstTxDateType()))
                .keyBy(new KeySelector<Row, String>() {
                    @Override
                    public String getKey(Row value) throws Exception {
                        return value.getField(2).toString();
                    }
                }).addSink(new SinkMysql(String.format(sql, QuotConfig.config.getProperty("mysql.index.sql.day.table"))))
                .setParallelism(50);


        //周
        applyData.map(new IndexKlineMap(KlineType.WEEKK.getType(), KlineType.WEEKK.getFirstTxDateType()))
                .keyBy(new KeySelector<Row, String>() {
                    @Override
                    public String getKey(Row value) throws Exception {
                        return value.getField(2).toString();
                    }
                }).addSink(new SinkMysql(String.format(sql, QuotConfig.config.getProperty("mysql.index.sql.week.table"))))
        .setParallelism(50);

        //月
        applyData.map(new IndexKlineMap(KlineType.MONTHK.getType(), KlineType.MONTHK.getFirstTxDateType()))
                .keyBy(new KeySelector<Row, String>() {
                    @Override
                    public String getKey(Row value) throws Exception {
                        return value.getField(2).toString();
                    }
                }).addSink(new SinkMysql(String.format(sql, QuotConfig.config.getProperty("mysql.index.sql.month.table"))))
                .setParallelism(50);

    }
}
