package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_pkg
*/
public class PkgBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * pw_bill
    */
    private String pwBill;

    /**
    * pw_dot_id
    */
    private long pwDotId;

    /**
    * warehouse_id
    */
    private long warehouseId;

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


    public PkgBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPwBill() {
        return pwBill;
    }

    public void setPwBill(String pwBill) {
        this.pwBill = pwBill;
    }

    public long getPwDotId() {
        return pwDotId;
    }

    public void setPwDotId(long pwDotId) {
        this.pwDotId = pwDotId;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
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