package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
 * 废弃仓库发车记录，变更为转运记录表 
 * Created by mengyao 
 * 2020年3月23日
 */
public class TransportRecordBean extends AbstractBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6607939787630933338L;

	private long id;
	private long pwId;
	private long pwWaybillId;
	private String pwWaybillNumber;
	private long owId;
	private long owWaybillId;
	private String owWaybillNumber;
	private long swId;
	private long ewId;
	private long transportToolId;
	private long pwDriver1Id;
	private long pwDriver2Id;
	private long pwDriver3Id;
	private long owDriver1Id;
	private long owDriver2Id;
	private long owDriver3Id;
	private long routeId;
	private long distance;
	private long duration;
	private int state;
	private String startVehicleDt;
	private String predictArrivalsDt;
	private String actualArrivalsDt;
	private String cdt;
	private String udt;
	private String remark;

	public TransportRecordBean() {
		super();
	}

	public TransportRecordBean(long id, long pwId, long pwWaybillId, String pwWaybillNumber, long owId,
			long owWaybillId, String owWaybillNumber, long swId, long ewId, long transportToolId, long pwDriver1Id,
			long pwDriver2Id, long pwDriver3Id, long owDriver1Id, long owDriver2Id, long owDriver3Id, long routeId,
			long distance, long duration, int state, String startVehicleDt, String predictArrivalsDt, String actualArrivalsDt,
			String cdt, String udt, String remark) {
		super();
		this.id = id;
		this.pwId = pwId;
		this.pwWaybillId = pwWaybillId;
		this.pwWaybillNumber = pwWaybillNumber;
		this.owId = owId;
		this.owWaybillId = owWaybillId;
		this.owWaybillNumber = owWaybillNumber;
		this.swId = swId;
		this.ewId = ewId;
		this.transportToolId = transportToolId;
		this.pwDriver1Id = pwDriver1Id;
		this.pwDriver2Id = pwDriver2Id;
		this.pwDriver3Id = pwDriver3Id;
		this.owDriver1Id = owDriver1Id;
		this.owDriver2Id = owDriver2Id;
		this.owDriver3Id = owDriver3Id;
		this.routeId = routeId;
		this.distance = distance;
		this.duration = duration;
		this.state = state;
		this.startVehicleDt = startVehicleDt;
		this.predictArrivalsDt = predictArrivalsDt;
		this.actualArrivalsDt = actualArrivalsDt;
		this.cdt = cdt;
		this.udt = udt;
		this.remark = remark;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPwId() {
		return pwId;
	}

	public void setPwId(long pwId) {
		this.pwId = pwId;
	}

	public long getPwWaybillId() {
		return pwWaybillId;
	}

	public void setPwWaybillId(long pwWaybillId) {
		this.pwWaybillId = pwWaybillId;
	}

	public String getPwWaybillNumber() {
		return pwWaybillNumber;
	}

	public void setPwWaybillNumber(String pwWaybillNumber) {
		this.pwWaybillNumber = pwWaybillNumber;
	}

	public long getOwId() {
		return owId;
	}

	public void setOwId(long owId) {
		this.owId = owId;
	}

	public long getOwWaybillId() {
		return owWaybillId;
	}

	public void setOwWaybillId(long owWaybillId) {
		this.owWaybillId = owWaybillId;
	}

	public String getOwWaybillNumber() {
		return owWaybillNumber;
	}

	public void setOwWaybillNumber(String owWaybillNumber) {
		this.owWaybillNumber = owWaybillNumber;
	}

	public long getSwId() {
		return swId;
	}

	public void setSwId(long swId) {
		this.swId = swId;
	}

	public long getEwId() {
		return ewId;
	}

	public void setEwId(long ewId) {
		this.ewId = ewId;
	}

	public long getTransportToolId() {
		return transportToolId;
	}

	public void setTransportToolId(long transportToolId) {
		this.transportToolId = transportToolId;
	}

	public long getPwDriver1Id() {
		return pwDriver1Id;
	}

	public void setPwDriver1Id(long pwDriver1Id) {
		this.pwDriver1Id = pwDriver1Id;
	}

	public long getPwDriver2Id() {
		return pwDriver2Id;
	}

	public void setPwDriver2Id(long pwDriver2Id) {
		this.pwDriver2Id = pwDriver2Id;
	}

	public long getPwDriver3Id() {
		return pwDriver3Id;
	}

	public void setPwDriver3Id(long pwDriver3Id) {
		this.pwDriver3Id = pwDriver3Id;
	}

	public long getOwDriver1Id() {
		return owDriver1Id;
	}

	public void setOwDriver1Id(long owDriver1Id) {
		this.owDriver1Id = owDriver1Id;
	}

	public long getOwDriver2Id() {
		return owDriver2Id;
	}

	public void setOwDriver2Id(long owDriver2Id) {
		this.owDriver2Id = owDriver2Id;
	}

	public long getOwDriver3Id() {
		return owDriver3Id;
	}

	public void setOwDriver3Id(long owDriver3Id) {
		this.owDriver3Id = owDriver3Id;
	}

	public long getRouteId() {
		return routeId;
	}

	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int getState() {
		return state;
	}

	/**
	 * 0准备发车、1发往、2到达
	 * @param state
	 */
	public void setState(int state) {
		this.state = state;
	}

	public String getStartVehicleDt() {
		return startVehicleDt;
	}

	public void setStartVehicleDt(String startVehicleDt) {
		this.startVehicleDt = startVehicleDt;
	}

	public String getPredictArrivalsDt() {
		return predictArrivalsDt;
	}

	public void setPredictArrivalsDt(String predictArrivalsDt) {
		this.predictArrivalsDt = predictArrivalsDt;
	}

	public String getActualArrivalsDt() {
		return actualArrivalsDt;
	}

	public void setActualArrivalsDt(String actualArrivalsDt) {
		this.actualArrivalsDt = actualArrivalsDt;
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
		return id + "\t" + pwId + "\t" + pwWaybillId + "\t" + pwWaybillNumber + "\t" + owId + "\t" + owWaybillId
				+ "\t" + owWaybillNumber + "\t" + swId + "\t" + ewId + "\t" + transportToolId + "\t" + pwDriver1Id
				+ "\t" + pwDriver2Id + "\t" + pwDriver3Id + "\t" + owDriver1Id + "\t" + owDriver2Id + "\t"
				+ owDriver3Id + "\t" + routeId + "\t" + distance + "\t" + duration + "\t" + state + "\t"
				+ startVehicleDt + "\t" + predictArrivalsDt + "\t" + actualArrivalsDt + "\t" + cdt + "\t" + udt
				+ "\t" + remark;
	}

}