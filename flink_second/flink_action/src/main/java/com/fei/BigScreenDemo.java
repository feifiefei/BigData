package com.fei;

import com.mchange.v1.cachedstore.Vacuumable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @Date 2020/10/26
 * 模拟双十一大屏数据刷新
 */
public class BigScreenDemo {

    /**
     * 需求：
     * 1.实时计算出当天零点截止到当前时间的销售总额
     * 2.计算出各个分类的销售top3
     * 3.每秒钟更新一次统计结果
     *
     * 涉及得知识点
     * (1)滑动事件窗口（time.day(1),time.second(1)）
     * (2)top3：优先级队列和排血
     * 数据聚合和分组（时间字段）
     */

    /**
     * 开发步骤：
     * 1.初始化环境
     * 2.自定义数据源
     * Tuple2<String,Double>:表示种类和金额
     * 3.数据分组
     * 4.划分时间窗口(按天划分、按秒触发)
     * 5.聚合计算
     * 实现：AggregateFunction、WindowFunction
     * 封装中间结果CategoryPojo(category,totalPrice,dateTime)
     * 6.计算结果
     * （1）时间分组
     * （2）划分时间窗口
     * （3）窗口处理：遍历、排序
     * 7.打印数据
     * 8.触发执行
     */

    public static void main(String[] args) throws Exception {
        //1.初始化环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.自定义数据源
        DataStreamSource<Tuple2<String, Double>> source = env.addSource(new BigSource());
        //3.数据分组
        //商品聚合数据，获取结果值
        SingleOutputStreamOperator<CategoryPojo> aggDate = source.keyBy(0)
                //4.划分时间窗口(按天划分、按秒触发)
                .timeWindow(Time.days(1), Time.seconds(1))
                //5.聚合计算
                .aggregate(new AggregateBigScreen(), new WindowFunctionBigScreen());

        //6.计算结果
        SingleOutputStreamOperator<Object> result = aggDate.keyBy("dateTime")
                //（2）划分时间窗口
                .timeWindow(Time.seconds(1))
                //（3）窗口处理：遍历、排序
                .apply(new WinFunction());

        //7.数据打印
        result.print();

        //8.触发执行
        env.execute();
    }

    private static class BigSource implements SourceFunction<Tuple2<String, Double>> {
        @Override
        public void run(SourceContext<Tuple2<String, Double>> ctx) throws Exception {
            String category[] = {
                    "女装", "男装",
                    "图书", "家电",
                    "洗护", "美妆",
                    "运动", "游戏",
                    "户外", "家具",
                    "乐器", "办公"
            };
            Random random = new Random();
            while (true) {
                for (String str : category) {
                    ctx.collect(Tuple2.of(str, random.nextDouble()));
                }
                TimeUnit.MILLISECONDS.sleep(200);
            }

        }

        @Override
        public void cancel() {

        }
    }

    /**
     * 数据聚合累加使用,对商品销售额进行累加
     */
    private static class AggregateBigScreen implements AggregateFunction<Tuple2<String, Double>, Double, Double> {
        @Override
        public Double createAccumulator() {
            return 0D;
        }

        @Override
        public Double add(Tuple2<String, Double> value, Double accumulator) {
            return value.f1 + accumulator;
        }

        @Override
        public Double getResult(Double accumulator) {
            return accumulator;
        }

        @Override
        public Double merge(Double a, Double b) {
            return a + b;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    //CategoryPojo(category,totalPrice,dateTime)
    public static class CategoryPojo {
        private String category; //商品分类
        private Double totalPrice;
        private String dateTime;
    }

    //分组字段需要使用tuple
    private static class WindowFunctionBigScreen implements WindowFunction<Double, CategoryPojo, Tuple, TimeWindow> {
        @Override
        public void apply(Tuple tuple, TimeWindow window, Iterable<Double> input, Collector<CategoryPojo> out) throws Exception {

            //获取累加金额数据
            Double totalPrice = input.iterator().next();

            //分组键就是商品分类数据
            String category = ((Tuple1<String>) tuple).f0;

            //封装中间结果CategoryPojo(category,totalPrice,dateTime)
            CategoryPojo categoryPojo = new CategoryPojo();
            categoryPojo.setTotalPrice(totalPrice);
            categoryPojo.setCategory(category);
            //设置时间字段
            String date = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
            categoryPojo.setDateTime(date);
            //数据收集
            out.collect(categoryPojo);
        }
    }

    //遍历、排序、优先级队列

    private static class WinFunction implements WindowFunction<CategoryPojo, Object, Tuple, TimeWindow> {
        @Override
        public void apply(Tuple tuple, TimeWindow window, Iterable<CategoryPojo> input, Collector<Object> out) throws Exception {

            //新建优先级队列
            //3表示只能存储3条数据
            PriorityQueue<CategoryPojo> queue = new PriorityQueue<>(3, new Comparator<CategoryPojo>() {
                @Override
                public int compare(CategoryPojo o1, CategoryPojo o2) {
                    return o1.getTotalPrice() >= o2.getTotalPrice() ? 1 : -1; //注意：不能够进行完全排序，只能进行局部排序，若想全局排序，需要先转成数组再排序
                }
            });

            //定义所有商品分类得销售总额变量；
            //这一秒之内得所有商品分类销售总额
            Double totalCategoryPrice =0d;
            for (CategoryPojo categoryPojo : input) {

                //实现top3,首先需要把数据加载到优先级队列里，并且是3条
                if(queue.size() <3){
                    queue.add(categoryPojo);
                }else{
                    //获取对顶数据
                    CategoryPojo peek = queue.peek();
                    if(peek.getTotalPrice()< categoryPojo.getTotalPrice()){

                        //删除对顶数据
                        queue.poll();
                        //添加最新数据到队列底部
                        queue.add(categoryPojo);
                    }
                }

                totalCategoryPrice +=categoryPojo.getTotalPrice();
            }

            //优先级队列，全局排序
            List<String> collect = queue.stream().sorted(new Comparator<CategoryPojo>() {
                @Override
                public int compare(CategoryPojo o1, CategoryPojo o2) {
                    return o1.getTotalPrice() >= o2.getTotalPrice() ? 1 : -1; //大于表示升序排列，小于表示降序排列
                }
            })
                    //使用map转换，目的：打印得数据更清晰
                    .map(line -> "分类：" + line.getCategory() + ",销售额：" + line.getTotalPrice())
                    //收集转换之后得每一条数据
                    .collect(Collectors.toList());

            String result = "所有商品销售总额："+totalCategoryPrice +"\n ,top3:"+collect;
            out.collect(result);
        }
    }
}
