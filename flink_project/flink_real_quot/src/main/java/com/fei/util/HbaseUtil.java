package com.fei.util;

import com.fei.config.QuotConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:todo:数据的增删查改
 * @author: 飞
 * @date: 2020/10/31 0031 21:06
 */
public class HbaseUtil {
    /**
     * 开发步骤：
     * 1.静态代码块获取连接对象
     * 2.获取表
     * 3.插入单列数据
     * 4.插入多列数据
     * 5.根据rowkey查询数据
     * 6.根据rowkey删除数据
     * 7.批量数据插入
     */

    //1.静态代码块获取连接对象
    static Connection connection = null;

    static {

        //设置zookeeper
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", QuotConfig.config.getProperty("zookeeper.connect"));
        //获取连接对象
        try {
            connection = ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //2.获取表
    public static Table getTable(String tableName) {

        Table tblName = null;
        try {
            tblName = connection.getTable(TableName.valueOf(tableName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tblName;
    }

    //3.插入单列数据
    public static void putDataByRowkey(String tableName, String family, String colName, String colValue, String rowkey) {
        //获取表
        Table table = getTable(tableName);

        try {
            //新建put对象
            Put put = new Put(rowkey.getBytes());
            //封装数据
            put.addColumn(family.getBytes(), Bytes.toBytes(colName), colValue.getBytes());

            //数据插入
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //表关闭
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //4.插入多列数据
    public static void putMapDataByRowkey(String tableName, String family, Map<String, Object> map, String rowkey) {

        Table table = getTable(tableName);
        try {
            Put put = new Put(rowkey.getBytes());
            //map的key是列名，value是列值
            for (String key : map.keySet()) {

                put.addColumn(family.getBytes(), key.getBytes(), map.get(key).toString().getBytes());
            }
            //插入
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //5.根据rowkey查询数据
    public static String queryByRowkey(String tableName, String family, String colName, String rowkey) {

        Table table = getTable(tableName);
        String str = null;
        try {
            //查询
            Get get = new Get(rowkey.getBytes());
            Result result = table.get(get);
            byte[] value = result.getValue(family.getBytes(), colName.getBytes());
            str = new String(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    //6.根据rowkey删除数据
    public static void delByRowkey(String tableName, String family, String rowkey) {

        Table table = getTable(tableName);
        try {
            //删除对象
            Delete delete = new Delete(rowkey.getBytes());
            //添加列簇
            delete.addFamily(family.getBytes());
            table.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //7.批量数据插入
    public static void putList(String tableName, List<Put> list) {

        Table table = getTable(tableName);
        try {
            table.put(list);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                table.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        /**
         *  3.插入单列数据
         *  4.插入多列数据
         *  5.根据rowkey查询数据
         *  6.根据rowkey删除数据
         *  7.批量数据插入
         */

        /**
         * 创建表：create 'test','f1'
         */

        //3.插入单列数据
        putDataByRowkey("test", "f1", "name", "xiaoli", "1");


        //4.插入多列数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "xiaozhang");
        map.put("age", 20);
        putMapDataByRowkey("test", "f1", map, "2");

        //5.根据rowkey查询数据
        String str = queryByRowkey("test", "f1", "name", "1");
        System.out.println("<<<<:" + str);

        //6.根据rowkey删除数据
//        delByRowkey("test","f1","2");

        //7.批量数据插入
        ArrayList<Put> list = new ArrayList<>();
        Put put = new Put("3".getBytes());
        put.addColumn("f1".getBytes(), "name".getBytes(), "xiaohong".getBytes());
        Put put2 = new Put("4".getBytes());
        put2.addColumn("f1".getBytes(), "name".getBytes(), "xiaowang".getBytes());
        list.add(put);
        list.add(put2);
        putList("test", list);
    }

}
