package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
 * tbl_warehouse_transport_tool
 *
 */
public class WarehouseTransportToolBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private long id;

	/**
	 * warehouse_id
	 */
	private long warehouseId;

	/**
	 * transport_tool_id
	 */
	private long transportToolId;

	/**
	 * allocate_dt
	 */
	private String allocateDt;

	/**
	 * state
	 */
	private int state;

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

	public WarehouseTransportToolBean() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public long getTransportToolId() {
		return transportToolId;
	}

	public void setTransportToolId(long transportToolId) {
		this.transportToolId = transportToolId;
	}

	public String getAllocateDt() {
		return allocateDt;
	}

	public void setAllocateDt(String allocateDt) {
		this.allocateDt = allocateDt;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
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