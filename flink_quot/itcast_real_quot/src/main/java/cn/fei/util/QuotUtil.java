package cn.fei.util;

import cn.fei.avro.SseAvro;
import cn.fei.avro.SzseAvro;
import cn.fei.bean.CleanBean;

/**
 * @Date 2020/10/31
 */
public class QuotUtil {

    //时间过滤,保证数据接收时间在开闭市时间区间之间
    //只接收数据时间在开市和闭市之间得数据流
    public static boolean checkTime(Object obj) {

        //判断对象
        if (obj instanceof SseAvro) {
            SseAvro sseAvro = (SseAvro) obj;
            return sseAvro.getTimestamp() < SpecialTimeUtil.closeTime && sseAvro.getTimestamp() > SpecialTimeUtil.openTime;
        } else {
            SzseAvro szseAvro = (SzseAvro) obj;
            return szseAvro.getTimestamp() < SpecialTimeUtil.closeTime && szseAvro.getTimestamp() > SpecialTimeUtil.openTime;
        }
    }

    //数据过滤
    //过滤数据中最高价、最低价、开盘价和收盘价为0的数据,保证数据都不为0
    public static boolean checkData(Object obj) {
        //判断对象
        if (obj instanceof SseAvro) {
            SseAvro sseAvro = (SseAvro) obj;
            return sseAvro.getHighPrice() != 0 && sseAvro.getLowPrice() != 0 && sseAvro.getOpenPrice() != 0 && sseAvro.getTradePrice() != 0;
        } else {
            SzseAvro szseAvro = (SzseAvro) obj;
            return szseAvro.getHighPrice() != 0 && szseAvro.getLowPrice() != 0 && szseAvro.getOpenPrice() != 0 && szseAvro.getTradePrice() != 0;
        }
    }

    /**
     * 过滤个股数据
     */
    //注意：沪市个股代码类型标识：MD002
    //      深市个股代码类型标识：010
    public static boolean isStock(CleanBean cleanBean){

        return cleanBean.getMdStreamId().equals("MD002") || cleanBean.getMdStreamId().equals("010");
    }

    /**
     * 过滤指数数据
     */
    public static boolean isIndex(CleanBean value) {
        return value.getMdStreamId().equals("MD001") || value.getMdStreamId().equals("900");
    }
}
