package cn.fei.sink;

import cn.fei.util.HbaseUtil;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.hadoop.hbase.client.Put;

import java.util.List;

/**
 * @Date 2020/11/1
 */
public class SinkHbase implements SinkFunction<List<Put>> {

    //创建构造方法,获取hbase表名
    private String tableName;

    public SinkHbase(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void invoke(List<Put> value, Context context) throws Exception {
        //执行插入操作
        HbaseUtil.putList(tableName, value);
    }
}
