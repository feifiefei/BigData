package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_out_warehouse
*/
public class OutWarehouseBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * pw_waybill_id
    */
    private long pwWaybillId;

    /**
    * pw_waybill_number
    */
    private String pwWaybillNumber;

    /**
    * ow_dot_id
    */
    private long owDotId;

    /**
    * warehouse_id
    */
    private long warehouseId;

    /**
    * ow_vehicle_id
    */
    private long owVehicleId;

    /**
    * ow_driver_emp_id
    */
    private long owDriverEmpId;

    /**
    * ow_follow1_emp_id
    */
    private long owFollow1EmpId;

    /**
    * ow_follow2_emp_id
    */
    private long owFollow2EmpId;

    /**
    * ow_start_dt
    */
    private String owStartDt;

    /**
    * ow_end_dt
    */
    private String owEndDt;

    /**
    * ow_position
    */
    private String owPosition;

    /**
    * ow_reg_emp_id
    */
    private long owRegEmpId;

    /**
    * ow_reg_scan_gun_id
    */
    private long owRegScanGunId;

    /**
    * ow_confirm_emp_id
    */
    private long owConfirmEmpId;

    /**
    * ow_confirm_scan_gun_id
    */
    private long owConfirmScanGunId;

    /**
    * ow_pre_seal_img
    */
    private String owPreSealImg;

    /**
    * ow_receipt_number
    */
    private String owReceiptNumber;

    /**
    * ow_receipt_dt
    */
    private String owReceiptDt;

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


    public OutWarehouseBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getOwDotId() {
        return owDotId;
    }

    public void setOwDotId(long owDotId) {
        this.owDotId = owDotId;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public long getOwVehicleId() {
        return owVehicleId;
    }

    public void setOwVehicleId(long owVehicleId) {
        this.owVehicleId = owVehicleId;
    }

    public long getOwDriverEmpId() {
        return owDriverEmpId;
    }

    public void setOwDriverEmpId(long owDriverEmpId) {
        this.owDriverEmpId = owDriverEmpId;
    }

    public long getOwFollow1EmpId() {
        return owFollow1EmpId;
    }

    public void setOwFollow1EmpId(long owFollow1EmpId) {
        this.owFollow1EmpId = owFollow1EmpId;
    }

    public long getOwFollow2EmpId() {
        return owFollow2EmpId;
    }

    public void setOwFollow2EmpId(long owFollow2EmpId) {
        this.owFollow2EmpId = owFollow2EmpId;
    }

    public String getOwStartDt() {
        return owStartDt;
    }

    public void setOwStartDt(String owStartDt) {
        this.owStartDt = owStartDt;
    }

    public String getOwEndDt() {
        return owEndDt;
    }

    public void setOwEndDt(String owEndDt) {
        this.owEndDt = owEndDt;
    }

    public String getOwPosition() {
        return owPosition;
    }

    public void setOwPosition(String owPosition) {
        this.owPosition = owPosition;
    }

    public long getOwRegEmpId() {
        return owRegEmpId;
    }

    public void setOwRegEmpId(long owRegEmpId) {
        this.owRegEmpId = owRegEmpId;
    }

    public long getOwRegScanGunId() {
        return owRegScanGunId;
    }

    public void setOwRegScanGunId(long owRegScanGunId) {
        this.owRegScanGunId = owRegScanGunId;
    }

    public long getOwConfirmEmpId() {
        return owConfirmEmpId;
    }

    public void setOwConfirmEmpId(long owConfirmEmpId) {
        this.owConfirmEmpId = owConfirmEmpId;
    }

    public long getOwConfirmScanGunId() {
        return owConfirmScanGunId;
    }

    public void setOwConfirmScanGunId(long owConfirmScanGunId) {
        this.owConfirmScanGunId = owConfirmScanGunId;
    }

    public String getOwPreSealImg() {
        return owPreSealImg;
    }

    public void setOwPreSealImg(String owPreSealImg) {
        this.owPreSealImg = owPreSealImg;
    }

    public String getOwReceiptNumber() {
        return owReceiptNumber;
    }

    public void setOwReceiptNumber(String owReceiptNumber) {
        this.owReceiptNumber = owReceiptNumber;
    }

    public String getOwReceiptDt() {
        return owReceiptDt;
    }

    public void setOwReceiptDt(String owReceiptDt) {
        this.owReceiptDt = owReceiptDt;
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