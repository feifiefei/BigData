package cn.fei.function;

import cn.fei.bean.StockBean;
import com.alibaba.fastjson.JSON;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.hadoop.hbase.client.Put;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date 2020/11/1
 * 封装hbase批量写入对象List<Put></>
 */
public class SecStockHbaseFunction implements AllWindowFunction<StockBean, List<Put>, TimeWindow> {

    @Override
    public void apply(TimeWindow window, Iterable<StockBean> values, Collector<List<Put>> out) throws Exception {
        /**
         * 开发步骤：
         * 1.新建List<Put>
         * 2.循环数据
         * 3.设置rowkey
         * 4.json数据转换
         * 5.封装put
         * 6.收集数据
         */
        //1.新建List<Put>
        List<Put> list = new ArrayList<>();
        for (StockBean stockBean : values) {
            //3.设置rowkey
            String rowkey = stockBean.getSecCode() + stockBean.getTradeTime(); //00000120201101101850
            //把对象转换成json字符串，放在hbase的一列里面
            String str = JSON.toJSONString(stockBean);
            //5.封装put
            Put put = new Put(rowkey.getBytes());
            put.add("info".getBytes(),"data".getBytes(),str.getBytes());
            list.add(put);
        }

        //6.收集数据
        out.collect(list);
    }
}
