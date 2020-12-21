package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_route
*/
public class RouteBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * start_station
    */
    private String startStation;

    /**
    * start_station_area_id
    */
    private long startStationAreaId;

    /**
    * start_warehouse_id
    */
    private long startWarehouseId;

    /**
    * end_station
    */
    private String endStation;

    /**
    * end_station_area_id
    */
    private long endStationAreaId;

    /**
    * end_warehouse_id
    */
    private long endWarehouseId;

    /**
    * mileage_m
    */
    private int mileageM;

    /**
    * time_consumer_minute
    */
    private int timeConsumerMinute;

    /**
    * state
    */
    private long state;

    /**
    * cdt
    */
    private String cdt;

    /**
    * udt
    */
    private String udt;

    /**
    * remark
    */
    private String remark;


    public RouteBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public long getStartStationAreaId() {
        return startStationAreaId;
    }

    public void setStartStationAreaId(long startStationAreaId) {
        this.startStationAreaId = startStationAreaId;
    }

    public long getStartWarehouseId() {
        return startWarehouseId;
    }

    public void setStartWarehouseId(long startWarehouseId) {
        this.startWarehouseId = startWarehouseId;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public long getEndStationAreaId() {
        return endStationAreaId;
    }

    public void setEndStationAreaId(long endStationAreaId) {
        this.endStationAreaId = endStationAreaId;
    }

    public long getEndWarehouseId() {
        return endWarehouseId;
    }

    public void setEndWarehouseId(long endWarehouseId) {
        this.endWarehouseId = endWarehouseId;
    }

    public int getMileageM() {
        return mileageM;
    }

    public void setMileageM(int mileageM) {
        this.mileageM = mileageM;
    }

    public int getTimeConsumerMinute() {
        return timeConsumerMinute;
    }

    public void setTimeConsumerMinute(int timeConsumerMinute) {
        this.timeConsumerMinute = timeConsumerMinute;
    }

    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }

    public String getCdt() {
        return cdt;
    }

    public void setCdt(String cdt) {
        this.cdt = cdt;
    }

    public String getUdt() {
        return udt;
    }

    public void setUdt(String udt) {
        this.udt = udt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}