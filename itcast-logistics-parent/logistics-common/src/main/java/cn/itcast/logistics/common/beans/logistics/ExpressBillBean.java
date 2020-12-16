package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_express_bill
*/
public class ExpressBillBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * express_number
    */
    private String expressNumber;

    /**
    * cid
    */
    private long cid;

    /**
    * eid
    */
    private long eid;

    /**
    * order_channel_id
    */
    private long orderChannelId;

    /**
    * order_dt
    */
    private String orderDt;

    /**
    * order_terminal_type
    */
    private int orderTerminalType;

    /**
    * order_terminal_os_type
    */
    private int orderTerminalOsType;

    /**
    * reserve_dt
    */
    private String reserveDt;

    /**
    * is_collect_package_timeout
    */
    private int isCollectPackageTimeout;

    /**
    * timeout_dt
    */
    private String timeoutDt;

    /**
    * type
    */
    private int type;

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


    public ExpressBillBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
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

    public long getOrderChannelId() {
        return orderChannelId;
    }

    public void setOrderChannelId(long orderChannelId) {
        this.orderChannelId = orderChannelId;
    }

    public String getOrderDt() {
        return orderDt;
    }

    public void setOrderDt(String orderDt) {
        this.orderDt = orderDt;
    }

    public int getOrderTerminalType() {
        return orderTerminalType;
    }

    public void setOrderTerminalType(int orderTerminalType) {
        this.orderTerminalType = orderTerminalType;
    }

    public int getOrderTerminalOsType() {
        return orderTerminalOsType;
    }

    public void setOrderTerminalOsType(int orderTerminalOsType) {
        this.orderTerminalOsType = orderTerminalOsType;
    }

    public String getReserveDt() {
        return reserveDt;
    }

    public void setReserveDt(String reserveDt) {
        this.reserveDt = reserveDt;
    }

    public int getIsCollectPackageTimeout() {
        return isCollectPackageTimeout;
    }

    public void setIsCollectPackageTimeout(int isCollectPackageTimeout) {
        this.isCollectPackageTimeout = isCollectPackageTimeout;
    }

    public String getTimeoutDt() {
        return timeoutDt;
    }

    public void setTimeoutDt(String timeoutDt) {
        this.timeoutDt = timeoutDt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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