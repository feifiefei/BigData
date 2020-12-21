package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
 * tbl_emp_info_map
 */
public class EmpInfoMapBean extends AbstractBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private long id;

	/**
	 * company_id
	 */
	private long companyId;

	/**
	 * dot_id
	 */
	private long dotId;

	/**
	 * emp_id
	 */
	private long empId;

	/**
	 * job_id
	 */
	private long jobId;

	/**
	 * dep_id
	 */
	private long depId;

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

	public EmpInfoMapBean() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getDotId() {
		return dotId;
	}

	public void setDotId(long dotId) {
		this.dotId = dotId;
	}

	public long getEmpId() {
		return empId;
	}

	public void setEmpId(long empId) {
		this.empId = empId;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public long getDepId() {
		return depId;
	}

	public void setDepId(long depId) {
		this.depId = depId;
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