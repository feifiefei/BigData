package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_warehouse_send_vehicle
*/
public class WarehouseSendVehicleBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * out_warehouse_id
    */
    private long outWarehouseId;

    /**
    * out_warehouse_waybill_id
    */
    private long outWarehouseWaybillId;

    /**
    * out_warehouse_waybill_number
    */
    private String outWarehouseWaybillNumber;

    /**
    * vehicle_id
    */
    private long vehicleId;

    /**
    * driver1_id
    */
    private long driver1Id;

    /**
    * driver2_id
    */
    private long driver2Id;

    /**
    * start_vehicle_dt
    */
    private String startVehicleDt;

    /**
    * next_warehouse_id
    */
    private long nextWarehouseId;

    /**
    * predict_arrivals_dt
    */
    private String predictArrivalsDt;

    /**
    * actual_arrivals_dt
    */
    private String actualArrivalsDt;

    /**
    * state
    */
    private int state;

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


    public WarehouseSendVehicleBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOutWarehouseId() {
        return outWarehouseId;
    }

    public void setOutWarehouseId(long outWarehouseId) {
        this.outWarehouseId = outWarehouseId;
    }

    public long getOutWarehouseWaybillId() {
        return outWarehouseWaybillId;
    }

    public void setOutWarehouseWaybillId(long outWarehouseWaybillId) {
        this.outWarehouseWaybillId = outWarehouseWaybillId;
    }

    public String getOutWarehouseWaybillNumber() {
        return outWarehouseWaybillNumber;
    }

    public void setOutWarehouseWaybillNumber(String outWarehouseWaybillNumber) {
        this.outWarehouseWaybillNumber = outWarehouseWaybillNumber;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public long getDriver1Id() {
        return driver1Id;
    }

    public void setDriver1Id(long driver1Id) {
        this.driver1Id = driver1Id;
    }

    public long getDriver2Id() {
        return driver2Id;
    }

    public void setDriver2Id(long driver2Id) {
        this.driver2Id = driver2Id;
    }

    public String getStartVehicleDt() {
        return startVehicleDt;
    }

    public void setStartVehicleDt(String startVehicleDt) {
        this.startVehicleDt = startVehicleDt;
    }

    public long getNextWarehouseId() {
        return nextWarehouseId;
    }

    public void setNextWarehouseId(Integer nextWarehouseId) {
        this.nextWarehouseId = nextWarehouseId;
    }

    public String getPredictArrivalsDt() {
        return predictArrivalsDt;
    }

    public void setPredictArrivalsDt(String predictArrivalsDt) {
        this.predictArrivalsDt = predictArrivalsDt;
    }

    public String getActualArrivalsDt() {
        return actualArrivalsDt;
    }

    public void setActualArrivalsDt(String actualArrivalsDt) {
        this.actualArrivalsDt = actualArrivalsDt;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
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

    @Override
    public String toString() {
        return "WarehouseSendVehicleBean{" +
                "id=" + id +
                ", outWarehouseId=" + outWarehouseId +
                ", outWarehouseWaybillId=" + outWarehouseWaybillId +
                ", outWarehouseWaybillNumber='" + outWarehouseWaybillNumber + '\'' +
                ", vehicleId=" + vehicleId +
                ", driver1Id=" + driver1Id +
                ", driver2Id=" + driver2Id +
                ", startVehicleDt='" + startVehicleDt + '\'' +
                ", nextWarehouseId=" + nextWarehouseId +
                ", predictArrivalsDt='" + predictArrivalsDt + '\'' +
                ", actualArrivalsDt='" + actualArrivalsDt + '\'' +
                ", state=" + state +
                ", cdt='" + cdt + '\'' +
                ", udt='" + udt + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}