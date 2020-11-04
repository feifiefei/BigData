package com.fei.map;

import com.fei.avro.SzseAvro;
import com.fei.bean.CleanBean;
import org.apache.flink.api.common.functions.MapFunction;

import java.math.BigDecimal;

/**
 * @Date 2020/10/31
 * 对深市avro对象进行转换
 */
public class SzseMap implements MapFunction<SzseAvro, CleanBean> {
    @Override
    public CleanBean map(SzseAvro value) throws Exception {
        CleanBean cleanBean = new CleanBean();
        cleanBean.setMdStreamId(value.getMdStreamID().toString());
        cleanBean.setSecCode(value.getSecurityID().toString());
        cleanBean.setSecName(value.getSymbol().toString());
        cleanBean.setTradeVolumn(value.getTradeVolume());
        cleanBean.setTradeAmt(value.getTotalValueTraded());
        cleanBean.setPreClosePrice(BigDecimal.valueOf(value.getPreClosePx()));
        cleanBean.setOpenPrice(BigDecimal.valueOf(value.getOpenPrice()));
        cleanBean.setMaxPrice(BigDecimal.valueOf(value.getHighPrice()));
        cleanBean.setMinPrice(BigDecimal.valueOf(value.getLowPrice()));
        cleanBean.setTradePrice(BigDecimal.valueOf(value.getTradePrice()));
        cleanBean.setEventTime(value.getTimestamp()); //事件时间
        cleanBean.setSource("szse"); //表示数据来源于深市

        return cleanBean;
    }
}
