package cn.itcast;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.concurrent.TimeUnit;


/**
 * @description:todo： 检查点的设置
 * @author: 飞
 * @date: 2020/10/23 0023 17:33
 */
public class CheckPointDemo {
    public static void main(String[] args) throws Exception {
        /**
         * 5.1.5 代码演示
         * 开发步骤：
         * 	1.初始化环境
         * 	2.设置检查点
         * 	3.自定义source
         * 	4.数据转换、分组、求和
         * 	5.数据打印
         */
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000L);//检查点触发间隔时间
        env.setStateBackend(new FsStateBackend("file:///checkpoint"));//本地
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);//制作检查点最大线程数
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);//制作最小间隔时间
        env.getCheckpointConfig().setCheckpointTimeout(60000);//检查点制作超时时间，超时会被丢弃
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.AT_LEAST_ONCE);//默认值，强一致性，保证数据只消费一次
        //当任务取消时保存检查点
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //当任务取消时删除检查点
        //env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);
        env.addSource(new CheckpointSourse())
                .keyBy(0)
                .sum(1)
                .print();
        env.execute();
    }

    private static class CheckpointSourse extends RichSourceFunction<Tuple2<String, Integer>> {

        @Override
        public void run(SourceContext<Tuple2<String, Integer>> ctx) throws Exception {
            int count = 0;
            while (true) {
                count++;
                ctx.collect(Tuple2.of("aa", count));
                TimeUnit.SECONDS.sleep(1);
                if (count > 10) {
                    throw new RuntimeException();
                }
            }
        }

        @Override
        public void cancel() {

        }
    }
}
