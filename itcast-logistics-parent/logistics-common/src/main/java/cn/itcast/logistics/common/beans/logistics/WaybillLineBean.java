package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_waybill_line
*/
public class WaybillLineBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * waybill_number
    */
    private String waybillNumber;

    /**
    * route_id
    */
    private long routeId;

    /**
    * serial_number
    */
    private String serialNumber;

    /**
    * transport_tool
    */
    private Integer transportTool;

    /**
    * delivery_record_id
    */
    private long deliveryRecordId;

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


    public WaybillLineBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public void setWaybillNumber(String waybillNumber) {
        this.waybillNumber = waybillNumber;
    }

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getTransportTool() {
        return transportTool;
    }

    public void setTransportTool(Integer transportTool) {
        this.transportTool = transportTool;
    }

    public long getDeliveryRecordId() {
        return deliveryRecordId;
    }

    public void setDeliveryRecordId(Integer deliveryRecordId) {
        this.deliveryRecordId = deliveryRecordId;
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
        return "WaybillLineBean{" +
                "id=" + id +
                ", waybillNumber='" + waybillNumber + '\'' +
                ", routeId=" + routeId +
                ", serialNumber='" + serialNumber + '\'' +
                ", transportTool=" + transportTool +
                ", deliveryRecordId=" + deliveryRecordId +
                ", cdt='" + cdt + '\'' +
                ", udt='" + udt + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}