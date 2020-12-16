package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_out_warehouse_detail
*/
public class OutWarehouseDetailBean extends AbstractBean implements Serializable {

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
    * waybill_id
    */
    private long waybillId;

    /**
    * pkg_id
    */
    private long pkgId;

    /**
    * pkg_desc
    */
    private String pkgDesc;

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


    public OutWarehouseDetailBean() {
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

    public long getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(long waybillId) {
        this.waybillId = waybillId;
    }

    public long getPkgId() {
        return pkgId;
    }

    public void setPkgId(long pkgId) {
        this.pkgId = pkgId;
    }

    public String getPkgDesc() {
        return pkgDesc;
    }

    public void setPkgDesc(String pkgDesc) {
        this.pkgDesc = pkgDesc;
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