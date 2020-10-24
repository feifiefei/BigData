package cn.itcast;

import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @description:todo：flink的分区方法
 * @author: 飞
 * @date: 2020/10/22 0022 18:13
 */
public class PartitionDemo {
    public static void main(String[] args) throws Exception {
        /**
         * 需求：
         * 对流中的元素使用各种分区,并输出
         */
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //fromElements是单线的
        DataStreamSource<Tuple2<Integer, String>> source = env.fromElements(Tuple2.of(1, "1"), Tuple2.of(2, "2"), Tuple2.of(3, "3"), Tuple2.of(4, "4"));
        //使用各种分区，获取分区结果数据
        DataStream<Tuple2<Integer, String>> res1 = source.global();//数据全部发往一个子线程里
        DataStream<Tuple2<Integer, String>> res2 = source.broadcast();//输出数据会被下游所有子线程进行接收，也就是每一个条数据，会全部广播一次
        DataStream<Tuple2<Integer, String>> res3 = source.forward();//上下游算子一对一接收，也就是说，上下游得算子，并行度要求一致
        DataStream<Tuple2<Integer, String>> res4 = source.shuffle(); //随机分配
        DataStream<Tuple2<Integer, String>> res5 = source.rebalance();//均匀分配
        DataStream<Tuple2<Integer, String>> res6 = source.rescale(); //轮询
        DataStream<Tuple2<Integer, String>> res7 = source.partitionCustom(new Partitioner<Object>() {
            @Override
            public int partition(Object o, int i) {
                return o.hashCode() % 2;
            }
        }, 1);
        res1.print();
        res2.print();
        res3.print();
        res4.print();
        res5.print();
        res6.print();
        res7.print();

        env.execute();

    }
}
