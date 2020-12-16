package cn.itcast.logistics.common.beans.crm;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
 * CRM客户地址信息
 * 2020年4月25日
 */
public class CustomerAddressBean extends AbstractBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8322665140642618700L;

	private long id;
	private long consumerId;
	private long addressId;
	private String cdt;
	private String udt;
	private String remark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(long consumerId) {
		this.consumerId = consumerId;
	}

	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
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
		return id + "\t" + consumerId + "\t" + addressId + "\t" + cdt + "\t" + udt + "\t" + remark;
	}

}
