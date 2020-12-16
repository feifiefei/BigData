package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
 * tbl_warehouse_receipt_detail
 *
 */
public class WarehouseReceiptDetailBean extends AbstractBean implements Serializable {

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
	 * pkg_id
	 */
	private long pkgId;

	/**
	 * receipt_bill_id
	 */
	private long receiptBillId;

	/**
	 * receipt_bill
	 */
	private String receiptBill;

	/**
	 * operator_id
	 */
	private long operatorId;

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

	public WarehouseReceiptDetailBean() {
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

	public long getPkgId() {
		return pkgId;
	}

	public void setPkgId(long pkgId) {
		this.pkgId = pkgId;
	}

	public long getReceiptBillId() {
		return receiptBillId;
	}

	public void setReceiptBillId(long receiptBillId) {
		this.receiptBillId = receiptBillId;
	}

	public String getReceiptBill() {
		return receiptBill;
	}

	public void setReceiptBill(String receiptBill) {
		this.receiptBill = receiptBill;
	}

	public long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(long operatorId) {
		this.operatorId = operatorId;
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