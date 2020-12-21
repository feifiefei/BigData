package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_warehouse_receipt
* @author mengyao 
* 2020-04-27
*/
public class WarehouseReceiptBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * bill
    */
    private String bill;

    /**
    * type
    */
    private int type;

    /**
    * warehouse_id
    */
    private long warehouseId;

    /**
    * operator_id
    */
    private long operatorId;

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


    public WarehouseReceiptBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public Integer getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
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

}