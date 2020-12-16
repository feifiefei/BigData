package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_charge_standard

*/
public class ChargeStandardBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * start_area_id
    */
    private long startAreaId;

    /**
    * stop_area_id
    */
    private long stopAreaId;

    /**
    * first_weight_charge
    */
    private String firstWeightCharge;

    /**
    * follow_up_weight_charge
    */
    private String followUpWeightCharge;

    /**
    * prescription
    */
    private long prescription;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStartAreaId() {
        return startAreaId;
    }

    public void setStartAreaId(long startAreaId) {
        this.startAreaId = startAreaId;
    }

    public long getStopAreaId() {
        return stopAreaId;
    }

    public void setStopAreaId(long stopAreaId) {
        this.stopAreaId = stopAreaId;
    }

    public String getFirstWeightCharge() {
        return firstWeightCharge;
    }

    public void setFirstWeightCharge(String firstWeightCharge) {
        this.firstWeightCharge = firstWeightCharge;
    }

    public String getFollowUpWeightCharge() {
        return followUpWeightCharge;
    }

    public void setFollowUpWeightCharge(String followUpWeightCharge) {
        this.followUpWeightCharge = followUpWeightCharge;
    }

    public long getPrescription() {
        return prescription;
    }

    public void setPrescription(long prescription) {
        this.prescription = prescription;
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