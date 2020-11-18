package cn.fei.function;


import cn.fei.bean.IndexBean;
import com.alibaba.fastjson.JSON;
import org.apache.flink.streaming.api.functions.windowing.RichAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.hadoop.hbase.client.Put;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2020/11/1
 */
public class SecIndexHbaseFunction extends RichAllWindowFunction<IndexBean, List<Put>, TimeWindow> {
    @Override
    public void apply(TimeWindow window, Iterable<IndexBean> values, Collector<List<Put>> out) throws Exception {

        //新建集合对象
        List<Put> list = new ArrayList<>();
        for (IndexBean value : values) {

            //获取rowkey
            String rowkey = value.getIndexCode() +value.getTradeTime();
            //对象转换json
            String jsonStr = JSON.toJSONString(value);
            //封装put
            Put put = new Put(rowkey.getBytes());
            put.add("info".getBytes(),"data".getBytes(),jsonStr.getBytes());
            list.add(put);
        }

        //数据收集
        out.collect(list);
    }
}
