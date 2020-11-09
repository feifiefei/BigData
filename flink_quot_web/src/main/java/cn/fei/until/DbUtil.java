package cn.fei.until;

import java.sql.*;

/**
 * @Date 2020/11/9
 * 通过数据库JDBC连接对象，主要供apache druid使用
 */
public class DbUtil {


    //获取连接
    public static Connection getConn(String driverName, String url) {
        Connection connection = null;
        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    //关流
    public static void close(ResultSet rs, Statement st,Connection conn){

        try {
            if(rs != null){
                rs.close();
            }
            if(st != null){
                st.close();
            }
            if(conn != null){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
