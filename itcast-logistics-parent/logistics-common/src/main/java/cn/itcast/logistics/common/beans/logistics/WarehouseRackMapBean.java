package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_warehouse_rack_map
* @author mengyao 
* 2020-04-27
*/
public class WarehouseRackMapBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * warehouse_name
    */
    private String warehouseName;

    /**
    * warehouse_addr
    */
    private String warehouseAddr;

    /**
    * warehouse_addr_gis
    */
    private String warehouseAddrGis;

    /**
    * company_id
    */
    private long companyId;

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


    public WarehouseRackMapBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseAddr() {
        return warehouseAddr;
    }

    public void setWarehouseAddr(String warehouseAddr) {
        this.warehouseAddr = warehouseAddr;
    }

    public String getWarehouseAddrGis() {
        return warehouseAddrGis;
    }

    public void setWarehouseAddrGis(String warehouseAddrGis) {
        this.warehouseAddrGis = warehouseAddrGis;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
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