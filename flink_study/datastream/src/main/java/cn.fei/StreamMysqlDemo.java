package cn.itcast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/22 0022 14:20
 */
public class StreamMysqlDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.addSource(new ReadMysql()).print();
        env.execute();

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Student {
        private Integer id;
        private String name;
        private Integer age;
    }

    //构架mysql的JDBC
    private static class ReadMysql extends RichSourceFunction<Student> {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        @Override
        public void open(Configuration parameters) throws Exception {
            //注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //构建连接
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?serverTimezone=Asia/Shanghai", "root", "123456");
            pst = conn.prepareStatement("select * from t_student");
            rs = pst.executeQuery();

        }

        @Override
        public void run(SourceContext ctx) throws Exception {
            while (rs.next()) {
                ctx.collect(new Student(
                        //读取mysql数据，下标从1开始
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3)
                ));
            }
        }

        @Override
        public void close() throws Exception {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        @Override
        public void cancel() {

        }
    }
}
