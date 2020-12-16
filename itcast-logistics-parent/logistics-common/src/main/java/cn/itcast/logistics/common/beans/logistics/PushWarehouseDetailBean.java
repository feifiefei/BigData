package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_push_warehouse_detail
*/
public class PushWarehouseDetailBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * push_warehouse_id
    */
    private long pushWarehouseId;

    /**
    * push_warehouse_bill
    */
    private String pushWarehouseBill;

    /**
    * warehouse_id
    */
    private long warehouseId;

    /**
    * pw_start_dt
    */
    private String pwStartDt;

    /**
    * pw_end_dt
    */
    private String pwEndDt;

    /**
    * pack_id
    */
    private long packId;

    /**
    * pack_desc
    */
    private String packDesc;

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


    public PushWarehouseDetailBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPushWarehouseId() {
        return pushWarehouseId;
    }

    public void setPushWarehouseId(long pushWarehouseId) {
        this.pushWarehouseId = pushWarehouseId;
    }

    public String getPushWarehouseBill() {
        return pushWarehouseBill;
    }

    public void setPushWarehouseBill(String pushWarehouseBill) {
        this.pushWarehouseBill = pushWarehouseBill;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getPwStartDt() {
        return pwStartDt;
    }

    public void setPwStartDt(String pwStartDt) {
        this.pwStartDt = pwStartDt;
    }

    public String getPwEndDt() {
        return pwEndDt;
    }

    public void setPwEndDt(String pwEndDt) {
        this.pwEndDt = pwEndDt;
    }

    public long getPackId() {
        return packId;
    }

    public void setPackId(long packId) {
        this.packId = packId;
    }

    public String getPackDesc() {
        return packDesc;
    }

    public void setPackDesc(String packDesc) {
        this.packDesc = packDesc;
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