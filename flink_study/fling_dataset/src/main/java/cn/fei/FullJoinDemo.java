package cn.itcast;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/21 0021 17:15
 */
public class FullJoinDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // 2.加载数据源
        DataSource<Tuple2<Integer, String>> sourceName = env.fromElements(Tuple2.of(1, "tom"), Tuple2.of(2, "jack"), Tuple2.of(3, "rose"));
        DataSource<Tuple2<Integer, String>> sourceCity = env.fromElements(Tuple2.of(1, "北京"), Tuple2.of(2, "上海"), Tuple2.of(4, "广州"));
        sourceName.fullOuterJoin(sourceCity)
                .where(0)
                .equalTo(0)
                .with(new JoinFunction<Tuple2<Integer, String>, Tuple2<Integer, String>, Object>() {
                    @Override
                    public Object join(Tuple2<Integer, String> first, Tuple2<Integer, String> second) throws Exception {
                        if (first == null) {
                            return Tuple3.of(second.f0, null, second.f1);
                        } else if (second == null) {
                            return Tuple3.of(first.f0, first.f1, null);
                        } else {
                            return Tuple3.of(first.f0, first.f1, second.f1);
                        }

                    }
                })
                .print();
    }
}
