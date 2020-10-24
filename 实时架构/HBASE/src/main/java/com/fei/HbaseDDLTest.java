package com.itheima;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

/**
 * Author: 飞
 * Date: 2020/9/21 0021 17:24
 * FileName: testDDL
 * Description: hbase的API之DDL
 */
public class HbaseDDLTest {
    public static void main(String[] args) throws IOException {
        //构建当前类的对象
        HbaseDDLTest ddlTest = new HbaseDDLTest();
        //构建连接
        Connection conn = ddlTest.getConnect();
        //构建管理员的对象
        HBaseAdmin admin = ddlTest.getAdmin(conn);
        //todo 1：列举当前所有的namespace。
        // ddlTest.listNS(admin);
//        //todo 2:创建namespace。
//       ddlTest.createNs(admin);
//        //todo 3:删除namespace。
//        ddlTest.delNS(admin);
//        //todo 4:列举表
//        ddlTest.listTables(admin);
//        //todo 5:创建删除表
        ddlTest.cteatTable(admin);
    }

    //创建表
    private void cteatTable(HBaseAdmin admin) throws IOException {
        //如果表存在就删除
        TableName tableName = TableName.valueOf("nbtest001:heshui");
        if (admin.tableExists(tableName)) {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        }
        //创建表
        //构建表的描述器
        HTableDescriptor desc = new HTableDescriptor(tableName);
        HColumnDescriptor family = new HColumnDescriptor(Bytes.toBytes("info"));
        //设置version的个数
        family.setMaxVersions(3);
        //添加列族
        desc.addFamily(family);
        admin.createTable(desc);

    }

    //列举表
    private void listTables(HBaseAdmin admin) throws IOException {
        List<TableDescriptor> tableDescriptors = admin.listTableDescriptors();
        for (TableDescriptor tableDescriptor : tableDescriptors) {
            System.out.println(tableDescriptor.getTableName().getNameAsString());

        }
    }

    //删除namespace
    private void delNS(HBaseAdmin admin) throws IOException {
        admin.deleteNamespace("test2");
    }

    //创建namespace。
    private void createNs(HBaseAdmin admin) throws IOException {
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create("nbtest001").build();
        admin.createNamespace(namespaceDescriptor);
    }

    //列举namespace
    private void listNS(HBaseAdmin admin) throws IOException {
        NamespaceDescriptor[] namespaceDescriptors = admin.listNamespaceDescriptors();
        for (NamespaceDescriptor namespaceDescriptor : namespaceDescriptors) {
            System.out.println(namespaceDescriptor.getName());

        }
    }

    //构建管理员连接
    private HBaseAdmin getAdmin(Connection conn) throws IOException {
        //从连接对象中构建管理员对象
        HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
        return admin;
    }

    //构建hbase连接
    private Connection getConnect() throws IOException {
        //构建Configuration对象
        Configuration conf = HBaseConfiguration.create();
        //配置服务端：所有Hbase客户端都连接Zookeeper
        conf.set("hbase.zookeeper.quorum", "node1:2181,node2:2181,node3:2181");
        //构建连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        //返回连接
        return conn;
    }


}
