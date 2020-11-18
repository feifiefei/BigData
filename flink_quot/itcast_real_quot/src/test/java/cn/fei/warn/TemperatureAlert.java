package cn.fei.warn;

/**
 * 告警实体类
 */
public class TemperatureAlert {
    private int rackID; //机架id

    public TemperatureAlert(int rackID) {
        this.rackID = rackID;
    }

    public TemperatureAlert() {
        this(-1);
    }

    public void setRackID(int rackID) {
        this.rackID = rackID;
    }

    public int getRackID() {
        return rackID;
    }

    @Override
    public String toString() {
        return "TemperatureAlert(" + getRackID() + ")";
    }
}
