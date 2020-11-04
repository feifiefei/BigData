package com.fei.function;

import com.fei.bean.CleanBean;
import org.apache.flink.api.java.functions.KeySelector;

/**
 * @description:
 * @author: 飞
 * @date: 2020/11/1 0001 20:46
 */
public class KeyFunction implements KeySelector<CleanBean, String> {
    @Override
    public String getKey(CleanBean cleanBean) throws Exception {
        return cleanBean.getSecCode();
    }
}
