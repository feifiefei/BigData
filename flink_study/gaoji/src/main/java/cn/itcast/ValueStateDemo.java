package cn.itcast;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


/**
 * @description:todo: StateValue
 * @author: 飞
 * @date: 2020/10/23 0023 16:20
 */
public class ValueStateDemo {
    public static void main(String[] args) throws Exception {
        /**
         * 开发步骤：
         * 1.获取流处理执行环境
         * 2.加载数据源
         * 3.数据分组
         * 4.数据转换，定义ValueState,保存中间结果
         * 5.数据打印
         * 6.触发执行
         */
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Tuple2<String, Long>> source = env.fromElements(
                Tuple2.of("北京", 1L),
                Tuple2.of("上海", 2L),
                Tuple2.of("北京", 6L),
                Tuple2.of("上海", 8L),
                Tuple2.of("北京", 3L),
                Tuple2.of("上海", 4L)
        );
        source.keyBy(0)
                //数据转换，定义valueState，保存中间结果
                .map(new RichMapFunction<Tuple2<String, Long>, Object>() {
                    ValueState<Long> vs = null;

                    //初始化状态,通过上下文对象获取
                    @Override
                    public void open(Configuration parameters) throws Exception {
                        vs = getRuntimeContext().getState(new ValueStateDescriptor<Long>("vs", Long.class));
                    }

                    @Override
                    public Object map(Tuple2<String, Long> value) throws Exception {
                        //获取中间结果，状态数据
                        Long count = vs.value() == null ? 0L : vs.value();
                        count += count + value.f1;
                        //更新状态
                        vs.update(count);
                        return Tuple2.of(value.f0, count);
                    }
                })
                .print();
        env.execute();
    }
}
