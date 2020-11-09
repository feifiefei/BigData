package cn.fei.until;

public class FlowidUtil {

    /**
     * @description 生成流水号，组成：时间+5位随机数
     */
    public static String getFlowid(){
        return System.currentTimeMillis()+""+(int) ((Math.random() * 9 + 1) * 10000);
    }

}
