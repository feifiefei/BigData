package cn.itcast;

import com.alibaba.fastjson.JSON;
import jdk.nashorn.internal.runtime.JSONFunctions;
import lombok.Data;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.GroupReduceOperator;

/**
 * @description:
 * @author: 飞
 * @date: 2020/10/20 0020 19:25
 */
public class TrasformationDemo {
    public static void main(String[] args) throws Exception {
        //获取批处理执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //读取数据
        DataSource<String> source = env.readTextFile("F:\\桌面\\杂类\\大数据阶段四\\flink-day02\\资料\\测试数据\\data\\input\\click.log");
        //数据转换
        GroupReduceOperator<Click, Click> soursedata = source.map(new MapFunction<String, Click>() {
            @Override
            public Click map(String s) throws Exception {
                Click click = JSON.parseObject(s, Click.class);
                return click;
            }
        })
                .first(10);

    }

    //新建bean对象
    @Data
    public static class Click {
        private String browserType;
        private String categoryID;
        private String channelID;
        private String city;
        private String country;
        private String entryTime;
        private String leaveTime;
        private String network;
        private String produceID;
        private String province;
        private String source;
        private String userID;
    }
}
