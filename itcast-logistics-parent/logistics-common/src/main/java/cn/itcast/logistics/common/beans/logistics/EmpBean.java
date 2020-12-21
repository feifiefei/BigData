package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  _emp
*/
public class EmpBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * emp_number
    */
    private String empNumber;

    /**
    * emp_name
    */
    private String empName;

    /**
    * emp_gender
    */
    private long empGender;

    /**
    * emp_birathday
    */
    private String empBirathday;

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


    public EmpBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public long getEmpGender() {
        return empGender;
    }

    public void setEmpGender(long empGender) {
        this.empGender = empGender;
    }

    public String getEmpBirathday() {
        return empBirathday;
    }

    public void setEmpBirathday(String empBirathday) {
        this.empBirathday = empBirathday;
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