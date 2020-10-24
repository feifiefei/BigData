package cn.itcast;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisClusterConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.flink.util.Collector;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashSet;


/**
 * @description:todo：flink连接redis
 * @author: 飞
 * @date: 2020/10/22 0022 20:31
 */
public class SinkRedisDemo {

    public static void main(String[] args) throws Exception {
        /**
         * 需求
         * 将单词统计求和结果数据，通过自定义Sink保存到Redis
         */
        /**
         * 开发步骤：
         * 1.获取流处理执行环境
         * 2.加载socket数据，开启nc
         * 3.数据转换/求和（flatMap/map/keyBy/sum）
         * 4.自定义sink对象:RedisSink
         *   （1）定义redis连接池（集群）
         *   （2）实现redisMapper
         * 5.触发执行
         */
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> source = env.socketTextStream("node1", 8090);
        SingleOutputStreamOperator<Tuple2<String, Integer>> res = source
                .flatMap(new FlatMapFunction<String, String>() {
                    @Override
                    public void flatMap(String s, Collector<String> collector) throws Exception {
                        String[] s1 = s.split(" ");
                        for (String s2 : s1) {
                            collector.collect(s2);
                        }
                    }
                })
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String s) throws Exception {
                        return Tuple2.of(s, 1);
                    }
                })
                .keyBy(0)
                .sum(1);
        //自定义sink对象
        //定义redis连接池
        HashSet<InetSocketAddress> set = new HashSet<>();
        set.add(new InetSocketAddress(InetAddress.getByName("node1"), 7001));
        set.add(new InetSocketAddress(InetAddress.getByName("node1"), 7002));
        set.add(new InetSocketAddress(InetAddress.getByName("node1"), 7003));
        //连接池
        FlinkJedisClusterConfig clusterConfig = new FlinkJedisClusterConfig.Builder().setMaxTotal(5)
                .setMinIdle(3)
                .setMinIdle(1)
                .setNodes(set)
                .build();

        //实现redisMapper
        res.addSink(new RedisSink<>(clusterConfig, new RedisMapper<Tuple2<String, Integer>>() {
            //设置redis：数据类型
            @Override
            public RedisCommandDescription getCommandDescription() {
                return new RedisCommandDescription(RedisCommand.HSET, "redisKey");
            }

            //具体写入数据得key
            @Override
            public String getKeyFromData(Tuple2<String, Integer> data) {
                return data.f0;
            }

            //具体写入数据得求和值
            @Override
            public String getValueFromData(Tuple2<String, Integer> data) {
                return data.f1.toString();
            }


        }));
        env.execute();
    }
}
