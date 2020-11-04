package com.fei.inter;

import com.fei.bean.CleanBean;
import org.apache.flink.streaming.api.datastream.DataStream;

/**
 * @description:通用接口数据解析
 * @author: 飞
 * @date: 2020/11/1 0001 9:29
 */
public interface ProcessDataInterface {
    void process(DataStream<CleanBean> waterData);
}
