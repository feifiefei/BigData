package cn.itcast.logistics.common.beans.crm;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
 * CRM客户信息
 * 2020年4月25日
 */
public class CustomerBean extends AbstractBean implements Serializable {
    private static final long serialVersionUID = 4373186682507605383L;

    private long id;
    private String name;
    private String tel;
    private String mobile;
    private String email;
    private int type;
    private int isOwnReg;
    private String regDt;
    private String regChannelId;
    private int state;
    private String cdt;
    private String udt;
    private String lastLoginDt;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsOwnReg() {
        return isOwnReg;
    }

    public void setIsOwnReg(int isOwnReg) {
        this.isOwnReg = isOwnReg;
    }

    public String getRegDt() {
        return regDt;
    }

    public void setRegDt(String regDt) {
        this.regDt = regDt;
    }

    public String getRegChannelId() {
        return regChannelId;
    }

    public void setRegChannelId(String regChannelId) {
        this.regChannelId = regChannelId;
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

    public String getLastLoginDt() {
        return lastLoginDt;
    }

    public void setLastLoginDt(String lastLoginDt) {
        this.lastLoginDt = lastLoginDt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return id + "\t" + name + "\t" + tel + "\t" + mobile + "\t" + email + "\t" + type + "\t" + isOwnReg + "\t"
                + regDt + "\t" + regChannelId + "\t" + state + "\t" + cdt + "\t" + udt + "\t" + lastLoginDt + "\t"
                + remark;
    }
}
