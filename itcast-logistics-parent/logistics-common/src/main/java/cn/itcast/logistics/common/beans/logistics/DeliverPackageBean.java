package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_deliver_package
*/
public class DeliverPackageBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * emp_id
    */
    private long empId;

    /**
    * waybill_id
    */
    private long waybillId;

    /**
    * waybill_number
    */
    private String waybillNumber;

    /**
    * express_bill_id
    */
    private long expressBillId;

    /**
    * express_bill_number
    */
    private String expressBillNumber;

    /**
    * package_id
    */
    private long packageId;

    /**
    * collect_package_dt
    */
    private String collectPackageDt;

    /**
    * rece_type
    */
    private long receType;

    /**
    * rece_dt
    */
    private String receDt;

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


    public DeliverPackageBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEmpId() {
        return empId;
    }

    public void setEmpId(long empId) {
        this.empId = empId;
    }

    public long getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(long waybillId) {
        this.waybillId = waybillId;
    }

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public void setWaybillNumber(String waybillNumber) {
        this.waybillNumber = waybillNumber;
    }

    public long getExpressBillId() {
        return expressBillId;
    }

    public void setExpressBillId(long expressBillId) {
        this.expressBillId = expressBillId;
    }

    public String getExpressBillNumber() {
        return expressBillNumber;
    }

    public void setExpressBillNumber(String expressBillNumber) {
        this.expressBillNumber = expressBillNumber;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }

    public String getCollectPackageDt() {
        return collectPackageDt;
    }

    public void setCollectPackageDt(String collectPackageDt) {
        this.collectPackageDt = collectPackageDt;
    }

    public long getReceType() {
        return receType;
    }

    public void setReceType(long receType) {
        this.receType = receType;
    }

    public String getReceDt() {
        return receDt;
    }

    public void setReceDt(String receDt) {
        this.receDt = receDt;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
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