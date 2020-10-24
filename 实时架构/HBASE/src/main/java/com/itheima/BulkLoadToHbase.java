package com.itheima;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles;

/**
 * Author: 飞
 * Date: 2020/9/23 0023 20:54
 * FileName: BulkLoadToHbase
 * Description: 将已转换好的 HFILE文件导入Hbase的表中
 */
public class BulkLoadToHbase {
    public static void main(String[] args) throws Exception {
        //构建Hbase的连接
        Configuration conf = HBaseConfiguration.create();
        conf.set("fs.defaultFS", "hdfs://node1:8020");
        conf.set("hbase.zookeeper.quorum", "node1:2181,node2:2181,node3:2181");
        Connection conn = ConnectionFactory.createConnection(conf);
        //指定输入的HFILE文件的地址
        Path hfile = new Path(args[0]);
        //获取管理员对象
        HBaseAdmin admin = (HBaseAdmin) conn.getAdmin();
        //获取表的对象
        Table tbName = conn.getTable(TableName.valueOf("mrhbase"));
        //获取该表的region
        RegionLocator regionLocator = conn.getRegionLocator(TableName.valueOf("mrhbase"));
        //加载实现的对象
        LoadIncrementalHFiles loadIncrementalHFiles = new LoadIncrementalHFiles(conf);
        //导入到hbase表的 方法
        loadIncrementalHFiles.doBulkLoad(
                hfile,//需要导入的HFILE的地址
                admin,//管理员对象
                tbName,//目标表
                regionLocator//表的region地址
        );

    }
}
