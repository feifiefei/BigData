package cn.fei.function;

import cn.fei.bean.SectorBean;
import com.alibaba.fastjson.JSON;
import org.apache.flink.streaming.api.functions.windowing.RichAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.hadoop.hbase.client.Put;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2020/11/3
 */
public class SectorHbaseListWindowFunction extends RichAllWindowFunction<SectorBean, List<Put>, TimeWindow> {
    @Override
    public void apply(TimeWindow window, Iterable<SectorBean> values, Collector<List<Put>> out) throws Exception {

        List<Put> list = new ArrayList<>();
        for (SectorBean value : values) {

            //拼接rowkey
            Put put = new Put((value.getSectorCode()+ value.getTradeTime()).getBytes());
            put.add("info".getBytes(),"data".getBytes(), JSON.toJSONString(value).getBytes());
            //添加入list
            list.add(put);
        }

        //数据收集
        out.collect(list);
    }
}
