package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  _dot
*/
public class DotBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * dot_number
    */
    private String dotNumber;

    /**
    * dot_name
    */
    private String dotName;

    /**
    * dot_addr
    */
    private String dotAddr;

    /**
    * dot_gis_addr
    */
    private String dotGisAddr;

    /**
    * dot_tel
    */
    private String dotTel;

    /**
    * company_id
    */
    private long companyId;

    /**
    * manage_area_id
    */
    private long manageAreaId;

    /**
    * manage_area_gis
    */
    private String manageAreaGis;

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


    public DotBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDotNumber() {
        return dotNumber;
    }

    public void setDotNumber(String dotNumber) {
        this.dotNumber = dotNumber;
    }

    public String getDotName() {
        return dotName;
    }

    public void setDotName(String dotName) {
        this.dotName = dotName;
    }

    public String getDotAddr() {
        return dotAddr;
    }

    public void setDotAddr(String dotAddr) {
        this.dotAddr = dotAddr;
    }

    public String getDotGisAddr() {
        return dotGisAddr;
    }

    public void setDotGisAddr(String dotGisAddr) {
        this.dotGisAddr = dotGisAddr;
    }

    public String getDotTel() {
        return dotTel;
    }

    public void setDotTel(String dotTel) {
        this.dotTel = dotTel;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getManageAreaId() {
        return manageAreaId;
    }

    public void setManageAreaId(long manageAreaId) {
        this.manageAreaId = manageAreaId;
    }

    public String getManageAreaGis() {
        return manageAreaGis;
    }

    public void setManageAreaGis(String manageAreaGis) {
        this.manageAreaGis = manageAreaGis;
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