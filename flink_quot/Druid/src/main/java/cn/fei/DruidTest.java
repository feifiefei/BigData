package cn.fei;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @Date 2020/10/31
 */
public class DruidTest {

    public static void main(String[] args) throws Exception {

        //加载驱动
        Class.forName("org.apache.calcite.avatica.remote.Driver");
        //建立连接
        Connection connection = DriverManager.getConnection("jdbc:avatica:remote:url=http://node01:8888/druid/v2/sql/avatica/");
        Statement st = connection.createStatement();
        //查询sql
        String sql = "SELECT * FROM \"wikiticker\"";
        ResultSet rs = st.executeQuery(sql);
        //遍历数据集
        while (rs.next()){
            //下标从1开始
            String added = rs.getString(2);
            String channel = rs.getString("channel");
            System.out.println("added:"+added +",channel："+channel);
        }

        //关流
        rs.close();
        st.close();
        connection.close();
    }
}
