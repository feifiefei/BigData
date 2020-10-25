package com.fei;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description:todo: 读取mysql数据(异步IO ） / / flinkmysql连接池
 * @author: 飞
 * @date: 2020/10/25 0025 19:36
 */
public class AyncReadMysqlDemo {

    public static void main(String[] args) throws Exception {
        /**
         * 开发步骤：
         * 1.获取流处理执行环境
         * 2.自定义数据源
         * 3.异步IO<AsyncDataStream>
         * 方式一：JDBCClient
         * 方式二：线程池
         * 4.数据打印
         * 5.触发执行
         */
        //1.获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 2.自定义数据源
        DataStreamSource<CategoryInfo> source = env.addSource(new AsyncIoSource());

        // 方式一：JDBCClient
        AsyncDataStream.unorderedWait(source, new AsyncIo1(), 60, TimeUnit.SECONDS).print();//4.数据打印


        //5.触发执行
        env.execute();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryInfo {
        private Integer id;
        private String name;
    }

    private static class AsyncIoSource extends RichSourceFunction<CategoryInfo> {
        @Override
        public void run(SourceContext<CategoryInfo> ctx) throws Exception {
            Integer[] ids = {1, 2, 3, 4, 5};
            for (Integer id : ids) {
                ctx.collect(new CategoryInfo(id, null));
            }
        }

        @Override
        public void cancel() {

        }
    }

    //执行查询mysql
    private static class AsyncIo1 extends RichAsyncFunction<CategoryInfo, CategoryInfo> {
        JDBCClient jdbcClient = null;

        //初始化连接池
        @Override
        public void open(Configuration parameters) throws Exception {
            //（3）Jdbc连接参数设置：
            JsonObject jsonObject = new JsonObject();
            jsonObject
                    .put("driver_class", "com.mysql.jdbc.Driver")
                    .put("url", "jdbc:mysql://node2:3306/test")
                    .put("user", "root")
                    .put("password", "123456");
            //（4）设置连接池设置
            VertxOptions vertxOptions = new VertxOptions();
            vertxOptions.setEventLoopPoolSize(10); //循环线程数
            vertxOptions.setWorkerPoolSize(10); //最大线程数
            Vertx vertx = Vertx.vertx(vertxOptions);
            jdbcClient = JDBCClient.create(vertx, jsonObject);
        }

        @Override
        public void asyncInvoke(CategoryInfo input, ResultFuture<CategoryInfo> resultFuture) throws Exception {

            //获取连接对象
            jdbcClient.getConnection(new Handler<AsyncResult<SQLConnection>>() {
                @Override
                public void handle(AsyncResult<SQLConnection> event) {
                    //获取连接对象
                    event.result().query("select * from t_category where id = " +  input.getId(), new Handler<AsyncResult<ResultSet>>() {
                        @Override
                        public void handle(AsyncResult<ResultSet> event) {
                            //获取异步返回结果
                            ResultSet result = event.result();
                            List<JsonObject> rows = result.getRows();
                            for (JsonObject row : rows) { //循环每一行数据
                                //获取mysql表中具体name列得值
                                String name = row.getString("name");
                                //收集数据
                                resultFuture.complete(Collections.singleton(new CategoryInfo(input.getId(), name)));
                            }
                        }
                    });
                }
            });
        }
    }
}