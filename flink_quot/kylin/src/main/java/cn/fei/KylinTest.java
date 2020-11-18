package cn.fei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @Date 2020/11/7
 * 使用JDBC操作数据库
 */
public class KylinTest {

    public static void main(String[] args) throws Exception {

        /**
         * 开发步骤：
         * 1.加载驱动
         * 2.建立连接
         * 3.创建statement对象
         * 4.写sql，执行sql查询
         * 5.获取查询结果
         * 6.关流
         */
        //1.加载驱动
        Class.forName("org.apache.kylin.jdbc.Driver");
        //2.建立连接
        Connection conn = DriverManager.getConnection("jdbc:kylin://node01:7070/itcast_quot_dm", "ADMIN", "KYLIN");
        //3.创建statement对象
        Statement st = conn.createStatement();
        //4.写sql，执行sql查询
        String sql = "select \n" +
                "sec_code,\n" +
                "trade_date,\n" +
                "max(high_price) as high_price,\n" +
                "min(low_price) as low_price,\n" +
                "min(trade_vol_day) as min_vol_day,\n" +
                "max(trade_vol_day) as max_vol_day,\n" +
                "min(trade_amt_day) as min_amt_day,\n" +
                "max(trade_amt_day) as max_amt_day\n" +
                "from app_sec_quot_stock\n" +
                "where trade_date between '2020-11-01' and '2020-11-07'\n" +
                "group by 1,2";

        ResultSet rs = st.executeQuery(sql);

        //5.获取查询结果
        while (rs.next()){

            System.out.println("secCode:"+rs.getString(1)+",trade_date:"+rs.getString(2));
        }

        //6.关流
        if(rs != null){
            rs.close();
        }
        if(st != null){
            st.close();
        }
        if(conn != null){
            conn.close();
        }
    }

}
