package com.fei.sink;

import com.fei.util.HbaseUtil;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.hadoop.hbase.client.Put;

import java.util.*;

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/1 0001 21:28
 */
public class SinkHbase implements SinkFunction<List<Put>> {
    //创建构造方法,获取hbase表名
    private String tableName;

    public SinkHbase(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void invoke(List<Put> value) throws Exception {
        HbaseUtil.putList(tableName,value);
    }

}
