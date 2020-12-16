package cn.fei.test001;

import cn.fei.bean.Message;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @description:
 */
public class MaliceUser {
    public static void main(String[] args) throws Exception {
        //设置执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //设置数据源
        DataStreamSource<Message> message = env.fromCollection(
                Arrays.asList(
                        new Message("1", "TMD", 1558430842000L),//2019-05-21 17:27:22
                        new Message("1", "TMD", 1558430843000L),//2019-05-21 17:27:23
                        new Message("1", "TMD", 1558430845000L),//2019-05-21 17:27:25
                        new Message("1", "TMD", 1558430850000L),//2019-05-21 17:27:30
                        new Message("1", "TMD", 1558430851000L),//2019-05-21 17:27:30
                        new Message("2", "TMD", 1558430851000L),//2019-05-21 17:27:31
                        new Message("1", "TMD", 1558430852000L)//2019-05-21 17:27:32
                )

        );
        //设置时间戳和水印
        message.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Message>(Time.seconds(2)) {
            @Override
            public long extractTimestamp(Message element) {
                return element.getEventTime();
            }
        });
        //定义模式规则
        Pattern<Message, Message> cep = Pattern.<Message>begin("begin")
                .where(new IterativeCondition<Message>() {
                    @Override
                    public boolean filter(Message value, Context<Message> ctx) throws Exception {
                        return value.getMsg().equals("TMD");
                    }
                }).times(2)
                .within(Time.seconds(5));
        //应用规则
        PatternStream<Message> resultofCEP = CEP.pattern(message.keyBy(Message::getUserId), cep);
        //获取符合结果的数据
        SingleOutputStreamOperator<List<Message>> begin = resultofCEP.select(new PatternSelectFunction<Message, List<Message>>() {
            @Override
            public List<Message> select(Map<String, List<Message>> pattern) throws Exception {
                return pattern.get("begin");
            }
        });
        begin.print("骂人的孙子");
        env.execute();
    }
}
