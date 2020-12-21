package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_collect_package
*/
public class CollectPackageBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * cid
    */
    private long cid;

    /**
    * eid
    */
    private long eid;

    /**
    * pkg_id
    */
    private long pkgId;

    /**
    * express_bill_id
    */
    private long expressBillId;

    /**
    * express_bill_number
    */
    private String expressBillNumber;

    /**
    * state
    */
    private int state;

    /**
    * collect_package_dt
    */
    private String collectPackageDt;

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

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public long getEid() {
        return eid;
    }

    public void setEid(long eid) {
        this.eid = eid;
    }

    public long getPkgId() {
        return pkgId;
    }

    public void setPkgId(long pkgId) {
        this.pkgId = pkgId;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCollectPackageDt() {
        return collectPackageDt;
    }

    public void setCollectPackageDt(String collectPackageDt) {
        this.collectPackageDt = collectPackageDt;
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