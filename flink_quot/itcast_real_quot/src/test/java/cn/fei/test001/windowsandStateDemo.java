package cn.fei.test001;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @description:
 */
public class windowsandStateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Tuple2<String, Integer>> source = env.fromElements(
                Tuple2.of("java", 1),
                Tuple2.of("python", 3),
                Tuple2.of("java", 2),
                Tuple2.of("scala", 2),
                Tuple2.of("python", 1),
                Tuple2.of("java", 1),
                Tuple2.of("scala", 2));
        source.keyBy(0)
                .map(new RichMapFunction<Tuple2<String, Integer>, Tuple2<String, Integer>>() {
                    private MapState<String, Integer> va = null;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        va = getRuntimeContext().getMapState(new MapStateDescriptor<String, Integer>("va", String.class, Integer.class));
                    }

                    @Override
                    public Tuple2<String, Integer> map(Tuple2<String, Integer> value) throws Exception {
                        Integer cnt = va.get(value.f0) == null ? 0 : va.get(value.f0);
                        cnt += value.f1;
                        va.put(value.f0, value.f1);
                        return Tuple2.of(value.f0, cnt);
                    }
                }).print();
        env.execute();
    }
}
