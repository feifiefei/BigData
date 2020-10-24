package com.itheima;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Author: 飞
 * Date: 2020/9/21 0021 20:25
 * FileName: HbaseDMLTest
 * Description: 对hbaseDML的测试
 */
public class HbaseDMLTest {
    public static void main(String[] args) throws IOException {
        HbaseDMLTest dmlTest = new HbaseDMLTest();
        //构建连接
        Connection conn = dmlTest.getConnect();
        //构建表的对象
        Table table = dmlTest.getTable(conn);
        //todo 1:put
        //dmlTest.putData(table);
        //todo 2:get
        // dmlTest.getData(table);
        //todo 3:delete
        // dmlTest.delData(table);
        //todo 4：scan
        dmlTest.scanData(table);
    }
    //遍历
    private void scanData(Table table) throws IOException {
        Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            for (Cell cell : result.rawCells()) {
                System.out.println(
                        Bytes.toString(CellUtil.cloneRow(cell)) + "\t" +
                                Bytes.toString(CellUtil.cloneFamily(cell)) + "\t" +
                                Bytes.toString(CellUtil.cloneQualifier(cell)) + "\t" +
                                Bytes.toString(CellUtil.cloneValue(cell)) + "\t" +
                                cell.getTimestamp()
                );
            }
        }
    }

    //删除
    private void delData(Table table) throws IOException {
        Delete delete = new Delete(Bytes.toBytes("10080_001"));
        table.delete(delete);
    }
    //获取
    private void getData(Table table) throws IOException {
        Get get = new Get(Bytes.toBytes("10080_001"));
        get.addFamily(Bytes.toBytes("info"));
        Result result = table.get(get);
        for (Cell cell : result.rawCells()) {
            System.out.println(
                    Bytes.toString(CellUtil.cloneRow(cell)) + "\t" +
                            Bytes.toString(CellUtil.cloneFamily(cell)) + "\t" +
                            Bytes.toString(CellUtil.cloneQualifier(cell)) + "\t" +
                            Bytes.toString(CellUtil.cloneValue(cell)) + "\t" +
                            cell.getTimestamp()
            );
        }


    }

    //添加数据
    private void putData(Table table) throws IOException {
        Put put = new Put(Bytes.toBytes("10080_001"));
        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("fage"));
        table.put(put);
    }

    //获取表
    private Table getTable(Connection conn) throws IOException {
        Table table = conn.getTable(TableName.valueOf("nbtest001:heshui"));
        return table;
    }

    //构建连接
    private Connection getConnect() throws IOException {
        //构建Configuration对象
        Configuration conf = HBaseConfiguration.create();
        //配置服务器：所有Hbase客户端都连接zookeeper
        conf.set("hbase.zookeeper.quorum", "node1:2181,node2:2181,node3:2181");
        //构建连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        return conn;
    }
}
