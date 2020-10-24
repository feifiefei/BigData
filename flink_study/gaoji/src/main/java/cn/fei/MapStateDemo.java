package cn.itcast;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @description:todo: StateMap
 * @author: 飞
 * @date: 2020/10/23 0023 16:30
 */
public class MapStateDemo {
    public static void main(String[] args) throws Exception {
        /**
         * 开发步骤：
         *  1.获取流处理执行环境
         *       2.加载数据源
         *       3.数据分组
         *       4.数据转换，定义MapState,保存中间结果
         *       5.数据打印
         *       6.触发执行
         */
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Tuple2<String, Integer>> source = env.fromElements(
                Tuple2.of("java", 1),
                Tuple2.of("python", 3),
                Tuple2.of("java", 2),
                Tuple2.of("scala", 2),
                Tuple2.of("python", 1),
                Tuple2.of("java", 1),
                Tuple2.of("scala", 2)
        );
        source.keyBy(0)
                .map(new RichMapFunction<Tuple2<String, Integer>, Object>() {
                    MapState<String, Integer> ms = null;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        ms = getRuntimeContext().getMapState(new MapStateDescriptor<String, Integer>("ms", String.class, Integer.class));
                    }

                    @Override
                    public Object map(Tuple2<String, Integer> value) throws Exception {
                        int count = ms.get(value.f0) == null ? 0 : ms.get(value.f0);
                        count += value.f1;

                        ms.put(value.f0, count);
                        return Tuple2.of(value.f0, count);


                    }
                }).print();
        env.execute();


    }
}
