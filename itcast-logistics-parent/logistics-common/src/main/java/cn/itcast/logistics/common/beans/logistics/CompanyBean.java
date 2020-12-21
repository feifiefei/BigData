package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;
import java.util.List;

/**
 * tbl_company
 */
public class CompanyBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private long id;

	/**
	 * company_name
	 */
	private String companyName;

	/**
	 * city_id
	 */
	private long cityId;

	/**
	 * company_number
	 */
	private String companyNumber;

	/**
	 * company_addr
	 */
	private String companyAddr;

	/**
	 * company_addr_gis
	 */
	private String companyAddrGis;

	/**
	 * company_tel
	 */
	private String companyTel;

	/**
	 * is_sub_company
	 */
	private long isSubCompany;

	/**
	 * state
	 */
	private long state;

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

	public CompanyBean() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getCompanyNumber() {
		return companyNumber;
	}

	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}

	public String getCompanyAddr() {
		return companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	public String getCompanyAddrGis() {
		return companyAddrGis;
	}

	public void setCompanyAddrGis(String companyAddrGis) {
		this.companyAddrGis = companyAddrGis;
	}

	public String getCompanyTel() {
		return companyTel;
	}

	public void setCompanyTel(String companyTel) {
		this.companyTel = companyTel;
	}

	public long getIsSubCompany() {
		return isSubCompany;
	}

	public void setIsSubCompany(long isSubCompany) {
		this.isSubCompany = isSubCompany;
	}

	public long getState() {
		return state;
	}

	public void setState(long state) {
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