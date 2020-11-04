package com.fei.szse;


import com.fei.avro.SzseAvro;
import com.fei.kafka.KafkaPro;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @description:todo: 深市客户端代码：主要是接收服务端对外广播的行情数据
 * @author: 飞
 * @date: 2020/10/28 0028 16:08
 */
public class SocketClient {
    //随机浮动成交价格系数
    private static Double[] price = new Double[]{0.1, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.2, -0.1, -0.11, -0.12, -0.13, -0.14, -0.15, -0.16, -0.17, -0.18, -0.19, -0.2};
    //随机浮动成交量
    private static int[] volumn = new int[]{50, 80, 110, 140, 170, 200, 230, 260, 290, 320, 350, 380, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300};

    public static Map<String, Map<String, Long>> map = new HashMap<>();

    //创建main方法
    public static void main(String[] args) throws IOException {
        /**
         * 开发步骤:
         * 1.创建main方法
         * 2.建立socket连接，获取流数据
         * 3.读文件缓存成交量和成交金额
         * 4.解析行数据，数据转换
         * 5.发送kafka
         */
        //建立socket连接，获取数据流
        Socket socket = new Socket("localhost", 4444);
        //读取数据
        InputStream inputStream = socket.getInputStream();
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        //新建kafka生产者对象
        KafkaPro kafkaPro = new KafkaPro();
        while (true) {
            //读取每一条数据
            String str = dataInputStream.readUTF();
//            System.out.println(str);
            //解析行数据，数据转换
            SzseAvro szseAvro = parseStr(str);
            //发送kafka
            kafkaPro.sendData("szse", szseAvro);

        }
    }

    private static SzseAvro parseStr(String str) {

        /**
         * 开发步骤：
         * 1.拷贝成交量和成交价数组
         * 2.获取随机浮动成交量和价格
         * 3.字符串切割、计算最新价
         * 4.获取缓存的成交量/金额
         * 5.计算总成交量/金额
         * 6.缓存总成交量/金额
         * 7.获取最高价和最低价(和最新价比较)
         * 8.封装结果数据
         */
        //后去随机浮动成交量和价格
        Random random = new Random();
        int pIndex = random.nextInt(price.length);
        //随机浮动价格系数
        Double randomPrice = price[pIndex];
        int vIndex = random.nextInt(volumn.length);
        //随机浮动成交量
        int randomVolumn = volumn[vIndex];
        //字符串切割、计算最新价
        String[] arr = str.split("\\|");
        String code = arr[1].trim();//产品代码
        //精度计算：金额字段加减乘除都用bigdecimal
        BigDecimal tradePrice = new BigDecimal(arr[9].trim());
        //26.73 *(1+0.1)
        //保存两位小数，四舍五入
        //浮动最新价
        tradePrice = tradePrice.multiply(new BigDecimal(1 + randomPrice)).setScale(2, RoundingMode.HALF_UP);
        //解析文件获取得成交量
        Long tradeVol = Long.valueOf(arr[3].trim());
        //解析文件获取的成交金额
        long tradeAmt = Double.valueOf(arr[4].trim()).longValue();
        //计算总成交量/金额
        Long totalVol = 0l;
        Long totalAmt = 0l;
        Map<String, Long> amtVolMap = map.get(code);
        if (amtVolMap == null) {
            totalVol = tradeVol;
            totalAmt = tradeAmt;
            //缓存总成交量和总成交金额
            HashMap<String, Long> mapCache = new HashMap<>();
            mapCache.put("tradeAmt", totalAmt);
            mapCache.put("tradeVol", totalVol);
            map.put(code, mapCache);
        } else {

            //从缓存里获取的
            Long tradeAmtTmp = amtVolMap.get("tradeAmt");
            Long tradeVolTmp = amtVolMap.get("tradeVol");
            totalVol = tradeVolTmp + randomVolumn; //总成交量
            //总成交金额
            //总成交金额 ！= tradePrice * tradeVol
            //增量的成交金额 = 浮动成交量* 最新价
            //总成交金额 = 增量的成交金额+ 当前总金额
            BigDecimal tmpAmt = tradePrice.multiply(new BigDecimal(randomVolumn));//增量的成交金额
            totalAmt = tradeAmtTmp + tmpAmt.longValue();

            //缓存总成交量和总成交金额
            HashMap<String, Long> mapCache = new HashMap<>();
            mapCache.put("tradeAmt", totalAmt);
            mapCache.put("tradeVol", totalVol);
            map.put(code, mapCache);
        }


        //7.获取最高价和最低价(和最新价比较)
        //获取最高价
        BigDecimal highPrice = new BigDecimal(arr[7].trim());
        if (tradePrice.compareTo(highPrice) == 1) {
            highPrice = tradePrice;
        }

        //获取最低价
        BigDecimal lowPrice = new BigDecimal(arr[8].trim());
        if (tradePrice.compareTo(lowPrice) == -1) {

            lowPrice = tradePrice;
        }

        //8.封装结果数据
        SzseAvro szseAvro = new SzseAvro();
        szseAvro.setMdStreamID(arr[0].trim());
        szseAvro.setSecurityID(code);
        szseAvro.setSymbol(arr[2].trim());
        szseAvro.setTradeVolume(totalVol); //成交总量
        szseAvro.setTotalValueTraded(totalAmt);//成交总额
        szseAvro.setPreClosePx(new Double(arr[5].trim()));
        szseAvro.setOpenPrice(new Double(arr[6].trim()));
        szseAvro.setHighPrice(highPrice.doubleValue());
        szseAvro.setLowPrice(lowPrice.doubleValue());
        szseAvro.setTradePrice(tradePrice.doubleValue());
        szseAvro.setClosePx(tradePrice.doubleValue()); //在15点之前，收盘价和最新家数据一样
        szseAvro.setTradingPhaseCode("T11"); //处于连续竞价期间，市场交易未结束
        szseAvro.setTimestamp(new Date().getTime()); //事件时间
        return szseAvro;

    }
}


