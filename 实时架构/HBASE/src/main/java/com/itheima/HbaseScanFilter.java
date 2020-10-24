package com.itheima;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Author: 飞
 * Date: 2020/9/22 0022 15:12
 * FileName: HbaseScanFilter
 * Description: scan过滤器
 */
public class HbaseScanFilter {
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

    //获取表
    private Table getTable(Connection conn) throws IOException {
        Table table = conn.getTable(TableName.valueOf("nbtest001:heshui"));
        return table;
    }

    public static void main(String[] args) throws IOException {
        //实例化本类
        HbaseScanFilter dmlTest = new HbaseScanFilter();
        //构建连接
        Connection connect = dmlTest.getConnect();
        //构建表对象
        Table table = dmlTest.getTable(connect);
        dmlTest.scanData(table);

    }

    private void scanData(Table table) throws IOException {
        Scan scan = new Scan();
        //起始范围
        scan.withStartRow(Bytes.toBytes("20200920_001"));
        scan.withStopRow(Bytes.toBytes("20200920_001"));
        //todo 1:基本比较过滤器
        //rowkey过滤器
        RowFilter rowFilter = new RowFilter(CompareOperator.NOT_EQUAL, new SubstringComparator("20200920_001"));
        //列族过滤器
        FamilyFilter name = new FamilyFilter(CompareOperator.EQUAL, new SubstringComparator("name"));
        //列值过滤器
        QualifierFilter qualifierFilter = new QualifierFilter(CompareOperator.NOT_EQUAL, new SubstringComparator("name"));
        //值过滤器
        new ValueFilter(CompareOperator.EQUAL, new SubstringComparator("e"));
        //todo 2:条件过滤器PrefixFilter，SingleColumnValueFilter,MutipleColumnPrefixFilter
        //rowkey前缀过滤器：PrefixFilter:查询所有2020年9月20号的数据
        PrefixFilter prefixFilter = new PrefixFilter(Bytes.toBytes("20200920"));
        //单列列值过滤器：SingleColumnValueFilter：查询姓名叫laoda的数据的信息
        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(
                Bytes.toBytes("basic"), //指定列族
                Bytes.toBytes("name"),  //指定列名
                CompareOperator.EQUAL,     //比较器
                Bytes.toBytes("lasan")  //值
        );
        //多列值过滤器：MultipleColumnPreFilter:查询所有数据的age和sex这两列
        byte[][] prefixes = {
                Bytes.toBytes("age"),
                Bytes.toBytes("sex")
        };
        MultipleColumnPrefixFilter multipleColumnPrefixFilter = new MultipleColumnPrefixFilter(prefixes);
        //todo 3:scan加载过滤器
        //组合过滤：获取name=laoda的数据中的age和sex
        FilterList filterList = new FilterList();
        filterList.addFilter(singleColumnValueFilter);
        filterList.addFilter(multipleColumnPrefixFilter);
        scan.setFilter(filterList);
        //执行scan
        ResultScanner scanner = table.getScanner(scan);
        //迭代取出数据
        for (Result result : scanner) {
            for (Cell cell : result.rawCells()) {
                System.out.println(Bytes.toString(result.getRow()));
                //遍历打印每列的数据
                System.out.println(
                        Bytes.toString(CellUtil.cloneRow(cell)) + "\t" +
                                Bytes.toString(CellUtil.cloneFamily(cell)) + "\t" +
                                Bytes.toString(CellUtil.cloneQualifier(cell)) + "\t" +
                                Bytes.toString(CellUtil.cloneValue(cell)) + "\t" +
                                cell.getTimestamp());
            }
        }
    }

}
