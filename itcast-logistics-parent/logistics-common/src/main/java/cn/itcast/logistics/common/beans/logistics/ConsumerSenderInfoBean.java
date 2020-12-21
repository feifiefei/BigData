package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;
import java.util.List;

/**
*  tbl_consumer_sender_info
*/
public class ConsumerSenderInfoBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * ciid
    */
    private long ciid;

    /**
    * pkg_id
    */
    private long pkgId;

    /**
    * express_bill_id
    */
    private long expressBillId;

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


    public ConsumerSenderInfoBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCiid() {
        return ciid;
    }

    public void setCiid(long ciid) {
        this.ciid = ciid;
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