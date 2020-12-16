package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
 * tbl_waybill
 * @author mengyao 
 * 2020-04-27
 */
public class WaybillBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private long id;

	/**
	 * express_bill_number
	 */
	private String expressBillNumber;

	/**
	 * waybill_number
	 */
	private String waybillNumber;

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
	 * pkg_id
	 */
	private long pkgId;

	/**
	 * pkg_number
	 */
	private String pkgNumber;

	/**
	 * timeout_dt
	 */
	private String timeoutDt;

	/**
	 * transform_type
	 */
	private int transformType;

	/**
	 * delivery_customer_name
	 */
	private String deliveryCustomerName;

	/**
	 * delivery_addr
	 */
	private String deliveryAddr;

	/**
	 * delivery_mobile
	 */
	private String deliveryMobile;

	/**
	 * delivery_tel
	 */
	private String deliveryTel;

	/**
	 * receive_customer_name
	 */
	private String receiveCustomerName;

	/**
	 * receive_addr
	 */
	private String receiveAddr;

	/**
	 * receive_mobile
	 */
	private String receiveMobile;

	/**
	 * receive_tel
	 */
	private String receiveTel;

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

	public WaybillBean() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getExpressBillNumber() {
		return expressBillNumber;
	}

	public void setExpressBillNumber(String expressBillNumber) {
		this.expressBillNumber = expressBillNumber;
	}

	public String getWaybillNumber() {
		return waybillNumber;
	}

	public void setWaybillNumber(String waybillNumber) {
		this.waybillNumber = waybillNumber;
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

	public long getPkgId() {
		return pkgId;
	}

	public void setPkgId(long pkgId) {
		this.pkgId = pkgId;
	}

	public String getPkgNumber() {
		return pkgNumber;
	}

	public void setPkgNumber(String pkgNumber) {
		this.pkgNumber = pkgNumber;
	}

	public String getTimeoutDt() {
		return timeoutDt;
	}

	public void setTimeoutDt(String timeoutDt) {
		this.timeoutDt = timeoutDt;
	}

	public int getTransformType() {
		return transformType;
	}

	public void setTransformType(int transformType) {
		this.transformType = transformType;
	}

	public String getDeliveryCustomerName() {
		return deliveryCustomerName;
	}

	public void setDeliveryCustomerName(String deliveryCustomerName) {
		this.deliveryCustomerName = deliveryCustomerName;
	}

	public String getDeliveryAddr() {
		return deliveryAddr;
	}

	public void setDeliveryAddr(String deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}

	public String getDeliveryMobile() {
		return deliveryMobile;
	}

	public void setDeliveryMobile(String deliveryMobile) {
		this.deliveryMobile = deliveryMobile;
	}

	public String getDeliveryTel() {
		return deliveryTel;
	}

	public void setDeliveryTel(String deliveryTel) {
		this.deliveryTel = deliveryTel;
	}

	public String getReceiveCustomerName() {
		return receiveCustomerName;
	}

	public void setReceiveCustomerName(String receiveCustomerName) {
		this.receiveCustomerName = receiveCustomerName;
	}

	public String getReceiveAddr() {
		return receiveAddr;
	}

	public void setReceiveAddr(String receiveAddr) {
		this.receiveAddr = receiveAddr;
	}

	public String getReceiveMobile() {
		return receiveMobile;
	}

	public void setReceiveMobile(String receiveMobile) {
		this.receiveMobile = receiveMobile;
	}

	public String getReceiveTel() {
		return receiveTel;
	}

	public void setReceiveTel(String receiveTel) {
		this.receiveTel = receiveTel;
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