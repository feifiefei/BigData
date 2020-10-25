package com.fei;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description:todo:flink的流处理之sql
 * @author: 飞
 * @date: 2020/10/25 0025 20:52
 */
public class StreamSqlDemo {
    public static void main(String[] args) throws Exception {
        /**
         * 开发步骤：
         * 	1.获取流处理执行环境
         * 	2.自定义数据源
         * 	3.获取表执行环境
         * 	4.设置事件时间、水位线
         * 	5.创建视图，sql
         * 	6.表转换成流
         * 	7.打印，执行
         *
         * 划分窗口：tumble(eventTime, interval '5' second)
         */
        //获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //自定义数据源
        DataStreamSource<Order> orderSource = env.addSource(new StreamSourceFunction());
        //获取表执行环境
        StreamTableEnvironment tblEnv = StreamTableEnvironment.create(env);
        //设置事件时间、水位线
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //水位线
        SingleOutputStreamOperator<Order> waterData = orderSource.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Order>(Time.seconds(0)) {
            @Override
            public long extractTimestamp(Order o) {
                return o.getEventTime();
            }
        });
        //创建视图，sql
        //设置字段的时候，需要制定事件时间字段，通过eventTime.rowtime
        tblEnv.createTemporaryView("tbl", waterData, "orderId,userId,money,eventTime.rowtime");
        //书写查询sql
        //每隔5秒统计最近5秒的每个用户的订单总数、订单的最大金额、订单的最小金额
        String sql = "select userId,count(*),max(money),min(money) from tbl group by userId,tumble(eventTime,interval '5' second)";
        //表转换成流
        //查询SQL
        Table table = tblEnv.sqlQuery(sql);
        DataStream<Row> result = tblEnv.toAppendStream(table, Row.class);
        result.print();
        env.execute();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    //Order(订单ID，用户ID，订单金额，事件时间)
    public static class Order {
        private String orderId; //订单ID
        private Integer userId; //用户ID
        private Integer money;  //订单金额
        private Long eventTime; //事件时间
    }

    private static class StreamSourceFunction extends RichSourceFunction <Order>{

        @Override
        public void run(SourceContext cxt) throws Exception {
            Random random = new Random();
            while (true) {
                cxt.collect(new Order(UUID.randomUUID().toString(), random.nextInt(3), random.nextInt(100), System.currentTimeMillis()));
                TimeUnit.SECONDS.sleep(1);

            }

        }

        @Override
        public void cancel() {

        }
    }
}
