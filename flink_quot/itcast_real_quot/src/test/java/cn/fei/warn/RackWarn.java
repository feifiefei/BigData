package cn.fei.warn;

import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternFlatSelectFunction;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.IngestionTimeExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.util.List;
import java.util.Map;

/**
 * @Date 2020/11/6
 * 机架温控预警
 */
public class RackWarn {

    /**
     * 需要定义两次模式匹配
     * 警告：某机架在10秒内连续两次上报的温度超过阈值
     * 报警：某机架在20秒内连续两次匹配警告，并且第二次的温度超过了第一次的温度就告警
     */
    /**
     * 1.获取流处理执行环境
     * 2.设置事件时间
     * 3.加载数据源，接收监视数据,设置提取时间
     * 4.定义匹配模式，设置预警匹配规则，警告：10s内连续两次超过阀值
     * 5.生成匹配模式流（分组）
     * 6.数据处理,生成警告数据
     * 7.二次定义匹配模式，告警：20s内连续两次匹配警告
     * 8.二次生成匹配模式流（分组）
     * 9.数据处理生成告警信息flatSelect，返回类型
     * 10.数据打印(警告和告警)
     * 11.触发执行
     */
    public static void main(String[] args) throws Exception {
        //1.获取流处理执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //2.设置事件处理时间
        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
        //3.加载数据源，接收监视数据,设置提取处理时间
        SingleOutputStreamOperator<MonitoringEvent> source = env.addSource(new MonitoringEventSource()).assignTimestampsAndWatermarks(new IngestionTimeExtractor<>());

        //4.定义匹配模式，设置预警匹配规则，警告：10s内连续两次超过阀值
        //警告：某机架在10秒内连续两次上报的温度超过阈值
        Pattern<MonitoringEvent, TemperatureEvent> pattern = Pattern.<MonitoringEvent>begin("begin").subtype(TemperatureEvent.class) //设置子类类型
                .where(new SimpleCondition<TemperatureEvent>() {
                    @Override
                    public boolean filter(TemperatureEvent value) throws Exception {
                        return value.getTemperature() > 100;
                    }
                }).next("next").subtype(TemperatureEvent.class)
                .where(new SimpleCondition<TemperatureEvent>() {
                    @Override
                    public boolean filter(TemperatureEvent value) throws Exception {
                        return value.getTemperature() > 100;
                    }
                }).within(Time.seconds(10));
        //5.生成匹配模式流（分组）
        PatternStream<MonitoringEvent> cep = CEP.pattern(source.keyBy(MonitoringEvent::getRackID), pattern);
        //6.数据处理,生成警告数据
        SingleOutputStreamOperator<TemperatureWarning> warnData = cep.select(new PatternSelectFunction<MonitoringEvent, TemperatureWarning>() {
            @Override
            public TemperatureWarning select(Map<String, List<MonitoringEvent>> pattern) throws Exception {
                //获取模式匹配到得两条数据
                TemperatureEvent begin = (TemperatureEvent) pattern.get("begin").get(0);
                TemperatureEvent next = (TemperatureEvent) pattern.get("next").get(0);

//                TemperatureWarning temperatureWarning = new TemperatureWarning();
//                temperatureWarning.setRackID(next.getRackID());
//                temperatureWarning.setAverageTemperature((begin.getTemperature()+next.getTemperature())/2);
                return new TemperatureWarning(begin.getRackID(), (begin.getTemperature() + next.getTemperature()) / 2);
            }
        });

        //报警：某机架在20秒内连续两次匹配警告，并且第二次的温度超过了第一次的温度就告警
        //7.二次定义匹配模式，告警：20s内连续两次匹配警告
        Pattern<TemperatureWarning, TemperatureWarning> patternAlert = Pattern.<TemperatureWarning>begin("begin").next("next").within(Time.seconds(20));
        //8.二次生成匹配模式流（分组）
        PatternStream<TemperatureWarning> cepAlert = CEP.pattern(warnData.keyBy(TemperatureWarning::getRackID), patternAlert);
        //9.数据处理生成告警信息flatSelect，返回类型
        SingleOutputStreamOperator<Object> alertData = cepAlert.flatSelect(new PatternFlatSelectFunction<TemperatureWarning, Object>() {
            @Override
            public void flatSelect(Map<String, List<TemperatureWarning>> pattern, Collector<Object> out) throws Exception {

                //第二次的温度超过了第一次的温度就告警
                TemperatureWarning begin = pattern.get("begin").get(0);
                TemperatureWarning next = pattern.get("next").get(0);
                if (next.getAverageTemperature() > begin.getAverageTemperature()) {
                    out.collect(new TemperatureAlert(begin.getRackID()));
                }
            }
        });

        //10.数据打印(警告和告警)
        warnData.print("警告：");
        alertData.print("告警：");

        //11.触发执行
        env.execute();
    }
}
