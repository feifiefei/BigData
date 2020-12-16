package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_courier
*/
public class CourierBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * job_num
    */
    private String jobNum;

    /**
    * name
    */
    private String name;

    /**
    * birathday
    */
    private String birathday;

    /**
    * tel
    */
    private String tel;

    /**
    * pda_num
    */
    private String pdaNum;

    /**
    * car_id
    */
    private long carId;

    /**
    * postal_standard_id
    */
    private long postalStandardId;

    /**
    * work_time_id
    */
    private String workTimeId;

    /**
    * dot_id
    */
    private long dotId;

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


    public CourierBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirathday() {
        return birathday;
    }

    public void setBirathday(String birathday) {
        this.birathday = birathday;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPdaNum() {
        return pdaNum;
    }

    public void setPdaNum(String pdaNum) {
        this.pdaNum = pdaNum;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public long getPostalStandardId() {
        return postalStandardId;
    }

    public void setPostalStandardId(long postalStandardId) {
        this.postalStandardId = postalStandardId;
    }

    public String getWorkTimeId() {
        return workTimeId;
    }

    public void setWorkTimeId(String workTimeId) {
        this.workTimeId = workTimeId;
    }

    public long getDotId() {
        return dotId;
    }

    public void setDotId(long dotId) {
        this.dotId = dotId;
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