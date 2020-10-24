package cn.itcast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @description：todo:flink连接mysql
 * @author: 飞
 * @date: 2020/10/22 0022 19:46
 */
public class SinkMysqlDemo {
    public static void main(String[] args) throws Exception {
        /**
         * 开发步骤：
         * 1.初始化环境
         * 2.加载数据源（bean对象）
         * 3.数据写入
         *   （1）初始化mysql连接对象
         *   （2）执行写入
         *   （3）关流
         * 4.触发执行
         */

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Student2> source = env.fromElements(new Student2(13, "唐三", 88));
        source.addSink(new MysqlSinkFunction());
        env.execute();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Student2 {
        private Integer id;
        private String name;
        private Integer age;
    }

    private static class MysqlSinkFunction extends RichSinkFunction<Student2> {
        Connection conn = null;
        PreparedStatement pst = null;

        @Override
        public void open(Configuration parameters) throws Exception {
            //构建mysql连接
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=Asia/Shanghai", "root", "123456");
            pst = conn.prepareStatement("insert into t_student values(?,?,?)");

        }

        @Override
        public void close() throws Exception {
            if (pst != null) {
                pst.close();
            }
            ;
            if (conn != null) {
                conn.close();
            }
        }

        @Override
        public void invoke(Student2 value, Context context) throws Exception {
            pst.setInt(1, value.getId());
            pst.setString(2, value.getName());
            pst.setInt(3, value.getAge());
            pst.executeLargeUpdate();

        }
    }
}
