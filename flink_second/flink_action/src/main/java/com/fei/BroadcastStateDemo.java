package com.fei;

import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple6;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.BroadcastConnectedStream;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.util.Collector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description:todo： 需求：将实时流中的数据，与mysql中的配置变量数据，根据用户id进行组合，形成一个新的数据进行输出
 * @author: 飞
 * @date: 2020/10/26 0026 21:29
 */
public class BroadcastStateDemo {

    /**
     * 需求：将实时流中的数据，与mysql中的配置变量数据，根据用户id进行组合，形成一个新的数据进行输出
     * 知识点：用到了广播流，用到mapState
     * <p>
     * 开发步骤：
     * 1.获取流处理执行环境
     * 2.自定义数据源
     * 3.获取mysql数据源: Map<String, Tuple2<String, Integer>>
     * 4.定义mapState广播变量，获取mysql广播流
     * 5.事件流和广播流连接
     * 6.业务处理BroadcastProcessFunction，补全数据
     * 7.数据打印
     * 8.触发执行
     */
    public static void main(String[] args) throws Exception {
        //	1.获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.自定义数据源
        DataStreamSource<Tuple4<String, String, String, Integer>> source = env.addSource(new BroadcastSource());
        //3.获取mysql数据源: Map<String, Tuple2<String, Integer>>
        DataStreamSource<Map<String, Tuple2<String, Integer>>> mysqlSource = env.addSource(new MysqlBrocad());
        //4.定义mapState广播变量，获取mysql广播流
        //将mysql数据流转换成广播流数据
        MapStateDescriptor<Void, Map<String, Tuple2<String, Integer>>> ms = new MapStateDescriptor<>("ms", Types.VOID,
                Types.MAP(Types.STRING, Types.TUPLE(Types.STRING, Types.INT)));
        //广播流
        BroadcastStream<Map<String, Tuple2<String, Integer>>> broadcastSource = mysqlSource.broadcast(ms);

        //5.事件流和广播流连接
        BroadcastConnectedStream<Tuple4<String, String, String, Integer>, Map<String, Tuple2<String, Integer>>> connectData = source.connect(broadcastSource);
        //6.业务处理BroadcastProcessFunction，补全数据
        connectData.process(new BroadcastProcessFunction<Tuple4<String, String, String, Integer>, Map<String, Tuple2<String, Integer>>, Object>() {

            //事件流一侧的数据，对广播数据是只读的，不能修改广播数据
            @Override
            public void processElement(Tuple4<String, String, String, Integer> value, ReadOnlyContext ctx, Collector<Object> out) throws Exception {
                //先获取广播流数据
                ReadOnlyBroadcastState<Void, Map<String, Tuple2<String, Integer>>> broadcastState = ctx.getBroadcastState(ms);
                Map<String, Tuple2<String, Integer>> map = broadcastState.get(null);
                if (map != null) {
                    Tuple2<String, Integer> tmpMap = map.get(value.f0);
                    if (tmpMap != null) { //如果不为null，说明此key键即存在于mysql，也存在于事件流中，所以可以进行数据补全
                        out.collect(Tuple6.of(value.f0, value.f1, value.f2, value.f3, tmpMap.f0, tmpMap.f1));
                    }
                }

            }

            //广播流一侧的数据，可以对mapState广播流进行操作
            @Override
            public void processBroadcastElement(Map<String, Tuple2<String, Integer>> value, Context ctx, Collector<Object> out) throws Exception {
                //获取广播流对象
                BroadcastState<Void, Map<String, Tuple2<String, Integer>>> broadcastState = ctx.getBroadcastState(ms);
                broadcastState.put(null, value);
            }
        }).print();//7.数据打印

        //8.触发执行
        env.execute();
    }

    //数据源样例：
    //Tuple4.of("user_3", "2019-08-17 12:19:47", "browse", 1)
    //Tuple4.of("user_2", "2019-08-17 12:19:48", "click", 1)
    private static class BroadcastSource extends RichSourceFunction<Tuple4<String, String, String, Integer>> {
        @Override
        public void run(SourceContext<Tuple4<String, String, String, Integer>> ctx) throws Exception {
            while (true) {

                ctx.collect(Tuple4.of("user_3", "2019-08-17 12:19:47", "browse", 1));
                ctx.collect(Tuple4.of("user_2", "2019-08-17 12:19:48", "click", 1));
                TimeUnit.SECONDS.sleep(5);
            }
        }

        @Override
        public void cancel() {

        }
    }

    /**
     * 读取myql数据
     */
    private static class MysqlBrocad extends RichSourceFunction<Map<String, Tuple2<String, Integer>>> {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        @Override
        public void open(Configuration parameters) throws Exception {

            //初始化连接对象
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://node2:3306/test", "root", "123456");
            pst = conn.prepareStatement("select * from user_info");
            rs = pst.executeQuery();
        }

        @Override
        public void run(SourceContext<Map<String, Tuple2<String, Integer>>> ctx) throws Exception {
            Map<String, Tuple2<String, Integer>> map = new HashMap<>();
            //循环mysql每一行数据
            while (rs.next()) {
                String userId = rs.getString(1);
                String userName = rs.getString(2);
                int userAge = rs.getInt(3);
                map.put(userId, Tuple2.of(userName, userAge));
            }

            ctx.collect(map);
        }

        //关流
        @Override
        public void close() throws Exception {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }

            if (conn != null) {
                conn.close();
            }
        }

        @Override
        public void cancel() {

        }
    }

}
