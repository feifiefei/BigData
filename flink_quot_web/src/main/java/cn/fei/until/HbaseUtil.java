package cn.fei.until;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2020/11/9
 * 区间查询
 */
public class HbaseUtil {


    //获取连接对象，全局，不会关闭连接
    static Connection connection = null;
    static {

        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "node01:2181");
        try {
            connection = ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 区间查询
     */
    public static List<String> scanQuery(String tableName,String family,String colName,String startKey,String endKey){

        List<String> list = new ArrayList<>();
        Table table = null;
        try {
             table = connection.getTable(TableName.valueOf(tableName));
            //区间查询
            Scan scan = new Scan();
            scan.setStartRow(startKey.getBytes());
            scan.setStopRow(endKey.getBytes());

            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                //获取指定列下面的数据
                byte[] value = result.getValue(family.getBytes(), colName.getBytes());
                list.add(new String(value));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    //测试
    public static void main(String[] args) {

        List<String> list = scanQuery("quot_stock", "info", "data", "00071920201101103819", "00071920201101103859");
        System.out.println("=======:"+list);
    }


}
