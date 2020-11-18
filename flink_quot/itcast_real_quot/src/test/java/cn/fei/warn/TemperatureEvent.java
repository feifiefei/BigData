package cn.fei.warn;

/**
 * 封装监控温度数据实体类
 */
public class TemperatureEvent extends MonitoringEvent {
    private double temperature; //温度
    private Long timestamp;  //时间

    public TemperatureEvent(int rackID, double temperature, long timestamp) {
        super(rackID);

        this.temperature = temperature;
        this.timestamp = timestamp;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "TemperatureEvent(" + getRackID() + ", " + temperature + ", "+timestamp+")";
    }
}