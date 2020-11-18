package cn.fei.inter;

import cn.fei.bean.CleanBean;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @Date 2020/11/4
 * 通用接口，数据解耦
 */
public interface ProcessDataCepInterface {

    //定义接口方法
    void process(DataStream<CleanBean> waterData, StreamExecutionEnvironment env);

}
