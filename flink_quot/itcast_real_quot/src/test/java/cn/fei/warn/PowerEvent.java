package cn.fei.warn;

public class PowerEvent extends MonitoringEvent {
    private double voltage; //电压，温度
    private long timestamp; //时间戳

    public PowerEvent(int rackID, double voltage, long timestamp) {
        super(rackID);

        this.voltage = voltage;
        this.timestamp = timestamp;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public double getVoltage() {
        return voltage;
    }

    @Override
    public String toString() {
        return "PowerEvent(" + getRackID() + ", " + voltage + ", " + timestamp + ")";
    }
}