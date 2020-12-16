package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  _delivery_record
*/
public class DeliveryRecordBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * cur_warehouse_id
    */
    private String curWarehouseId;

    /**
    * vehicle_id
    */
    private long vehicleId;

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
    * actua_arrivals_dt
    */
    private long actuaArrivalsDt;

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


    public DeliveryRecordBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurWarehouseId() {
        return curWarehouseId;
    }

    public void setCurWarehouseId(String curWarehouseId) {
        this.curWarehouseId = curWarehouseId;
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
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

    public void setNextWarehouseId(long nextWarehouseId) {
        this.nextWarehouseId = nextWarehouseId;
    }

    public String getPredictArrivalsDt() {
        return predictArrivalsDt;
    }

    public void setPredictArrivalsDt(String predictArrivalsDt) {
        this.predictArrivalsDt = predictArrivalsDt;
    }

    public long getActuaArrivalsDt() {
        return actuaArrivalsDt;
    }

    public void setActuaArrivalsDt(long actuaArrivalsDt) {
        this.actuaArrivalsDt = actuaArrivalsDt;
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