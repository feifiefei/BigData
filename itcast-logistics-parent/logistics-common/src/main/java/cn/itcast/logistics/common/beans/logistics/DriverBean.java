package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  _driver
*/
public class DriverBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * job_number
    */
    private String jobNumber;

    /**
    * name
    */
    private String name;

    /**
    * gender
    */
    private String gender;

    /**
    * birathday
    */
    private String birathday;

    /**
    * state
    */
    private long state;

    /**
    * driver_license_number
    */
    private String driverLicenseNumber;

    /**
    * driver_license_type
    */
    private long driverLicenseType;

    /**
    * get_driver_license_dt
    */
    private String getDriverLicenseDt;

    /**
    * car_id
    */
    private long carId;

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


    public DriverBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirathday() {
        return birathday;
    }

    public void setBirathday(String birathday) {
        this.birathday = birathday;
    }

    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public void setDriverLicenseNumber(String driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public long getDriverLicenseType() {
        return driverLicenseType;
    }

    public void setDriverLicenseType(long driverLicenseType) {
        this.driverLicenseType = driverLicenseType;
    }

    public String getGetDriverLicenseDt() {
        return getDriverLicenseDt;
    }

    public void setGetDriverLicenseDt(String getDriverLicenseDt) {
        this.getDriverLicenseDt = getDriverLicenseDt;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
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