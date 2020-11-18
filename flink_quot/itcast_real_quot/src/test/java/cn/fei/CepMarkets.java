package cn.fei;

import cn.fei.bean.Product;
import cn.fei.util.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.util.Collector;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Date 2020/11/4
 */
public class CepMarkets {

    public static void main(String[] args) throws Exception {

        /**
         * 如果商品售价在1分钟之内有连续两次超过预定商品价格阀值就发送告警信息。
         */
        /**
         * 1.获取流处理执行环境
         * 2.设置事件时间、并行度
         * 3.整合kafka
         * 4.数据转换
         * 5.process获取bean,设置status，并设置水印时间
         * 6.定义匹配模式，设置时间长度
         * 7.匹配模式（分组）
         * 8.查询告警数据
         */
        //1.获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.设置事件时间、并行度
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //3.整合kafka
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "node01:9092");
        properties.setProperty("group.id", "cep");
        FlinkKafkaConsumer011<String> kafkaConsumer = new FlinkKafkaConsumer011<>("cep", new SimpleStringSchema(), properties);
        //从头设置从头消费,与偏移量无关
        kafkaConsumer.setStartFromEarliest();

        //加载自定义数据源
        DataStreamSource<String> source = env.addSource(kafkaConsumer);
        //4.数据转换
        SingleOutputStreamOperator<Product> mapData = source.map(new MapFunction<String, Product>() {
            @Override
            public Product map(String value) throws Exception {

                //将字符串解析成json
                JSONObject json = JSON.parseObject(value);

                return new Product(
                        json.getLongValue("goodsId"),
                        json.getDouble("goodsPrice"),
                        json.getString("goodsName"),
                        json.getString("alias"),
                        json.getLongValue("orderTime"),
                        false
                );
            }
        });

        //5.process获取bean,设置status，并设置水印时间
        SingleOutputStreamOperator<Product> productData = mapData.process(new ProcessFunction<Product, Product>() {
            JedisCluster jedisCluster = null;

            @Override
            public void open(Configuration parameters) throws Exception {
                //获取redis连接对象
                jedisCluster = RedisUtil.getJedisCluster();
            }

            //此方法得目的：比较商品价格和阀值数据，设置status
            @Override
            public void processElement(Product value, Context ctx, Collector<Product> out) throws Exception {
                //获取redis阀值数据
                String threshold = jedisCluster.hget("product", value.getGoodsName());
                if (value.getGoodsPrice() > Double.valueOf(threshold)) {
                    value.setStatus(true); //表示超过阀值数据
                }
                out.collect(value);
            }
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Product>(Time.seconds(0)) { //并设置水印时间
            @Override
            public long extractTimestamp(Product element) {
                return element.getOrderTime();
            }
        });

        //如果商品售价在1分钟之内有连续两次超过预定商品价格阀值就发送告警信息。
        //6.定义匹配模式，设置时间长度
        Pattern<Product, Product> pattern = Pattern.<Product>begin("begin")
                .where(new SimpleCondition<Product>() {
                    @Override
                    public boolean filter(Product value) throws Exception {
                        return value.getStatus() == true;
                    }
                }).next("next")
                .where(new SimpleCondition<Product>() {
                    @Override
                    public boolean filter(Product value) throws Exception {
                        return value.getStatus() == true;
                    }
                }).within(Time.minutes(1));

        //7.匹配模式（分组）
        PatternStream<Product> cep = CEP.pattern(productData.keyBy(Product::getGoodsId), pattern);
        //查询匹配到得规则数据
        cep.select(new PatternSelectFunction<Product, Object>() {
            @Override
            public Object select(Map<String, List<Product>> pattern) throws Exception {

                List<Product> next = pattern.get("next");
                return next;
            }
        }).print();

        //触发执行
        env.execute();
    }
}