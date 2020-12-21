package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
 * tbl_transport_tool
 * @author mengyao 
 * 2020-04-27
 */
public class TransportToolBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private long id;

	/**
	 * brand
	 */
	private String brand;

	/**
	 * model
	 */
	private String model;

	/**
	 * type
	 */
	private int type;

	/**
	 * given_load
	 */
	private String givenLoad;

	/**
	 * load_cn_unit
	 */
	private String loadCnUnit;

	/**
	 * load_en_unit
	 */
	private String loadEnUnit;

	/**
	 * buy_dt
	 */
	private String buyDt;

	/**
	 * license_plate
	 */
	private String licensePlate;

	/**
	 * state
	 */
	private String state;

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

	public TransportToolBean() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getGivenLoad() {
		return givenLoad;
	}

	public void setGivenLoad(String givenLoad) {
		this.givenLoad = givenLoad;
	}

	public String getLoadCnUnit() {
		return loadCnUnit;
	}

	public void setLoadCnUnit(String loadCnUnit) {
		this.loadCnUnit = loadCnUnit;
	}

	public String getLoadEnUnit() {
		return loadEnUnit;
	}

	public void setLoadEnUnit(String loadEnUnit) {
		this.loadEnUnit = loadEnUnit;
	}

	public String getBuyDt() {
		return buyDt;
	}

	public void setBuyDt(String buyDt) {
		this.buyDt = buyDt;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
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