package cn.itcast.logistics.common.beans.crm;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
 * CRM地址信息
 */
public class AddressBean extends AbstractBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8216215778785426469L;

    private long id;
    private String name;
    private String tel;
    private String mobile;
    private String detailAddr;
    private long areaId;
    private String gisAddr;
    private String cdt;
    private String udt;
    private String remark;

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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public String getGisAddr() {
        return gisAddr;
    }

    public void setGisAddr(String gisAddr) {
        this.gisAddr = gisAddr;
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

    @Override
    public String toString() {
        return id + "\t" + name + "\t" + tel + "\t" + mobile + "\t" + detailAddr + "\t" + areaId + "\t" + gisAddr + "\t"
                + cdt + "\t" + udt + "\t" + remark;
    }

}
