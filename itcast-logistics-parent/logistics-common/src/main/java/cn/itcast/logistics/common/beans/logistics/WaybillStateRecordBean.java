package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
 * tbl_waybill_state_record
 * @author mengyao 
 * 2020-04-27
 */
public class WaybillStateRecordBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private long id;

	/**
	 * waybill_id
	 */
	private long waybillId;

	/**
	 * waybill_number
	 */
	private String waybillNumber;

	/**
	 * employee_id
	 */
	private String employeeId;

	/**
	 * consignee_id
	 */
	private long consigneeId;

	/**
	 * cur_warehouse_id
	 */
	private long curWarehouseId;

	/**
	 * next_warehouse_id
	 */
	private long nextWarehouseId;

	/**
	 * deliverer_id
	 */
	private long delivererId;

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

	public WaybillStateRecordBean() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getWaybillId() {
		return waybillId;
	}

	public void setWaybillId(long waybillId) {
		this.waybillId = waybillId;
	}

	public String getWaybillNumber() {
		return waybillNumber;
	}

	public void setWaybillNumber(String waybillNumber) {
		this.waybillNumber = waybillNumber;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public long getConsigneeId() {
		return consigneeId;
	}

	public void setConsigneeId(long consigneeId) {
		this.consigneeId = consigneeId;
	}

	public long getCurWarehouseId() {
		return curWarehouseId;
	}

	public void setCurWarehouseId(long curWarehouseId) {
		this.curWarehouseId = curWarehouseId;
	}

	public long getNextWarehouseId() {
		return nextWarehouseId;
	}

	public void setNextWarehouseId(Integer nextWarehouseId) {
		this.nextWarehouseId = nextWarehouseId;
	}

	public long getDelivererId() {
		return delivererId;
	}

	public void setDelivererId(Integer delivererId) {
		this.delivererId = delivererId;
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