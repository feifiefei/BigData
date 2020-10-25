package com.fei;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.concurrent.TimeUnit;

/**
 * @description:todo：flink不同的重启策略
 * @author: 飞
 * @date: 2020/10/25 0025 17:37
 */
public class CheckpointDemo {
    public static void main(String[] args) throws Exception {
        /**
         * 开发步骤：
         * 1.初始化环境
         * 2.设置检查点
         * 3.自定义source
         * 4.数据转换、分组、求和
         * 5.数据打印
         * 6.触发执行
         */
        //初始化环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置检查点
        env.enableCheckpointing(5000L);
//        env.setStateBackend(new FsStateBackend("hdfs://node1:8020/check"));
        //设置存储本地
        env.setStateBackend(new FsStateBackend("file:///checkpoint"));
//        env.setStateBackend(new FsStateBackend(args[0]));
        //制作检查点最大线程数
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //检查点之间的最小间隔时间
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        //检查点制作超时时间，如果超时，会被丢弃
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        //默认值，强一致性，保证数据只消费一次
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //当任务取消时，保留检查点，企业中主要使用的方式，需要手动删除无效检查点
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //当任务取消的时候，会删除检查点
        //env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);
        //如果不配置重启策略，程序会无限重启
        //如果配置了重启策略，程序会自动拉起，当拉起次数超过配置的重启次数之后，程序异常
        /**
         * 1:固定延迟重启策略
         * 尝试重启三次，每次重启间隔时间是3s
         * 如果重启次数超过三次，程序直接宕机，不再重启
         */
        //env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, Time.seconds(3)));
        /**
         * 2:失败率重启策略
         * 需要设置3个参数：第一个参数：是失败重启次数；第二个参数：是在一分钟之内的时间重启3次，第三个参数，每次重启的间隔时间
         * 在60s之内，失败重启次数超过3次，直接宕机
         */
//        env.setRestartStrategy(RestartStrategies.failureRateRestart(3, Time.seconds(60), Time.seconds(5)));
        /**
         * 3:不重启策略
         * 只要程序异常停机一次，直接宕机，不会拉起
         */
        env.setRestartStrategy(RestartStrategies.noRestart());
        //自定义source
        env.addSource(new CheckpointSource())
                .keyBy(0)
                .sum(1)
                .print();
        env.execute();
    }

    private static class CheckpointSource extends RichSourceFunction<Tuple2<String, Integer>> {

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
