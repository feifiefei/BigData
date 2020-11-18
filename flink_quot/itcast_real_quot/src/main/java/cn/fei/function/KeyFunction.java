package cn.fei.function;

import cn.fei.bean.CleanBean;
import org.apache.flink.api.java.functions.KeySelector;

/**
 * @Date 2020/11/1
 * 根据证券代码进行分组
 */
public class KeyFunction implements KeySelector<CleanBean,String> {
    @Override
    public String getKey(CleanBean value) throws Exception {
        return value.getSecCode();
    }
}
