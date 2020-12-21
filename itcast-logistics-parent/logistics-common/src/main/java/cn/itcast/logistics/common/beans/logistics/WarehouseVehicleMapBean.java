package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
 * tbl_warehouse_vehicle_map
 * @author mengyao 
 * 2020-04-27
 */
public class WarehouseVehicleMapBean extends AbstractBean implements Serializable {

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
	 * vehicle_id
	 */
	private long vehicleId;

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

	public WarehouseVehicleMapBean() {
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

	public long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
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