package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_work_time
*/
public class WorkTimeBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * name
    */
    private String name;

    /**
    * start_dt
    */
    private String startDt;

    /**
    * stop_dt
    */
    private String stopDt;

    /**
    * saturday_start_dt
    */
    private String saturdayStartDt;

    /**
    * saturday_stop_dt
    */
    private String saturdayStopDt;

    /**
    * sunday_start_dt
    */
    private String sundayStartDt;

    /**
    * sunday_stop_dt
    */
    private String sundayStopDt;

    /**
    * state
    */
    private int state;

    /**
    * company_id
    */
    private long companyId;

    /**
    * operator_id
    */
    private long operatorId;

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


    public WorkTimeBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getStopDt() {
        return stopDt;
    }

    public void setStopDt(String stopDt) {
        this.stopDt = stopDt;
    }

    public String getSaturdayStartDt() {
        return saturdayStartDt;
    }

    public void setSaturdayStartDt(String saturdayStartDt) {
        this.saturdayStartDt = saturdayStartDt;
    }

    public String getSaturdayStopDt() {
        return saturdayStopDt;
    }

    public void setSaturdayStopDt(String saturdayStopDt) {
        this.saturdayStopDt = saturdayStopDt;
    }

    public String getSundayStartDt() {
        return sundayStartDt;
    }

    public void setSundayStartDt(String sundayStartDt) {
        this.sundayStartDt = sundayStartDt;
    }

    public String getSundayStopDt() {
        return sundayStopDt;
    }

    public void setSundayStopDt(String sundayStopDt) {
        this.sundayStopDt = sundayStopDt;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
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