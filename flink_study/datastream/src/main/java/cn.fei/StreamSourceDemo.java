package cn.itcast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/22 0022 10:07
 */
public class StreamSourceDemo {
    public static void main(String[] args) throws Exception {
        /**
         *
         * 开发步骤：
         * 1、获取执行环境
         * 2、自定义数据源
         * 3、数据处理
         * */
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.addSource(new MySource()).setParallelism(1).print();
        env.execute();

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Order {
        private String OrderId;
        private Integer UserId;
        private Integer orderPrice;
        private Long timestamp;

    }

    private static class MySource implements SourceFunction<Order> {
        private Boolean flag = true;

        @Override
        public void run(SourceContext sourceContext) throws Exception {
            while (flag) {
                TimeUnit.SECONDS.sleep(1);//睡一秒
                Random random = new Random();
                String OrderId = UUID.randomUUID().toString();//生成随机id
                Integer UserId = random.nextInt(3);
                Integer orderPrice = random.nextInt(100);
                Long timestamp = System.currentTimeMillis();
                sourceContext.collect(new Order(OrderId, UserId, orderPrice, timestamp));


            }
        }

        @Override
        public void cancel() {
            flag = false;
        }
    }
}
