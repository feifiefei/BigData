package cn.fei.inter;

import cn.fei.bean.CleanBean;
import org.apache.flink.streaming.api.datastream.DataStream;

/**
 * @Date 2020/11/1
 * 通用接口，数据解耦
 */
public interface ProcessDataInterface {

    //定义接口方法
    void process(DataStream<CleanBean> waterData);

}
