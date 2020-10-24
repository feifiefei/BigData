package cn.itcast;

import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.core.fs.FileSystem;


import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/21 0021 17:37
 */
public class PartitionDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        List<Tuple3<Integer, Long, String>> list = new ArrayList<>();
        list.add(Tuple3.of(1, 1L, "Hello"));
        list.add(Tuple3.of(2, 2L, "Hello"));
        list.add(Tuple3.of(3, 2L, "Hello"));
        list.add(Tuple3.of(4, 3L, "Hello"));
        list.add(Tuple3.of(5, 3L, "Hello"));
        list.add(Tuple3.of(6, 3L, "hehe"));
        list.add(Tuple3.of(7, 4L, "hehe"));
        list.add(Tuple3.of(8, 4L, "hehe"));
        list.add(Tuple3.of(9, 4L, "hehe"));
        list.add(Tuple3.of(10, 4L, "hehe"));
        list.add(Tuple3.of(11, 5L, "hehe"));
        list.add(Tuple3.of(12, 5L, "hehe"));
        list.add(Tuple3.of(13, 5L, "hehe"));
        list.add(Tuple3.of(14, 5L, "hehe"));
        list.add(Tuple3.of(15, 5L, "hehe"));
        list.add(Tuple3.of(16, 6L, "hehe"));
        list.add(Tuple3.of(17, 6L, "hehe"));
        list.add(Tuple3.of(18, 6L, "hehe"));
        list.add(Tuple3.of(19, 6L, "hehe"));
        list.add(Tuple3.of(20, 6L, "hehe"));
        list.add(Tuple3.of(21, 6L, "hehe"));
        //加载数据
        DataSource<Tuple3<Integer, Long, String>> source = env.fromCollection(list);
        /**
         * 1.Hash分区
         * 2.区间范围分区
         * 3.自定义分区
         * 4.排序分区
         */
        //1.hash分区，根据指定字段的hash值进行分区
//        source
////                .partitionByHash(0)
////                .writeAsText("hash", FileSystem.WriteMode.OVERWRITE);
////        //触发执行
////        env.execute();
//        //2.区间范围分区
//        source
//                .partitionByRange(0)
//                .writeAsText("range", FileSystem.WriteMode.NO_OVERWRITE);
//
//
//        env.execute();
        //3.自定义分区,参数1：自定义翻去方法；参数2：指定字段进行自定义分区
//        source.partitionCustom(new Partitioner<Object>() {
//            @Override
//            public int partition(Object o, int i) {
//                return o.hashCode() % 2;
//            }
//        }, 1).writeAsText("custom", FileSystem.WriteMode.OVERWRITE);
//        env.execute();
        //4.排序分区，默认生成一个排序文件
        source.sortPartition(0, Order.DESCENDING).writeAsText("sort", FileSystem.WriteMode.OVERWRITE);
        env.execute();    }
}
