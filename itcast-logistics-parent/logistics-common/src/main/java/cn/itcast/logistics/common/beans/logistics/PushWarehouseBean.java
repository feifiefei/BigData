package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_push_warehouse
*/
public class PushWarehouseBean extends AbstractBean implements Serializable {

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
    * pw_dot_id
    */
    private long pwDotId;

    /**
    * warehouse_id
    */
    private long warehouseId;

    /**
    * emp_id
    */
    private long empId;

    /**
    * pw_start_dt
    */
    private String pwStartDt;

    /**
    * pw_end_dt
    */
    private String pwEndDt;

    /**
    * pw_position
    */
    private String pwPosition;

    /**
    * pw_reg_emp_id
    */
    private long pwRegEmpId;

    /**
    * ow_reg_emp_scan_gun_id
    */
    private long owRegEmpScanGunId;

    /**
    * pw_confirm_emp_id
    */
    private long pwConfirmEmpId;

    /**
    * ow_confirm_emp_scan_gun_id
    */
    private long owConfirmEmpScanGunId;

    /**
    * pw_box_emp_id
    */
    private long pwBoxEmpId;

    /**
    * pw_box_scan_gun_id
    */
    private long pwBoxScanGunId;

    /**
    * pw_after_seal_img
    */
    private String pwAfterSealImg;

    /**
    * pw_receipt_number
    */
    private String pwReceiptNumber;

    /**
    * pw_receipt_dt
    */
    private String pwReceiptDt;

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


    public PushWarehouseBean() {
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

    public long getPwDotId() {
        return pwDotId;
    }

    public void setPwDotId(long pwDotId) {
        this.pwDotId = pwDotId;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public long getEmpId() {
        return empId;
    }

    public void setEmpId(long empId) {
        this.empId = empId;
    }

    public String getPwStartDt() {
        return pwStartDt;
    }

    public void setPwStartDt(String pwStartDt) {
        this.pwStartDt = pwStartDt;
    }

    public String getPwEndDt() {
        return pwEndDt;
    }

    public void setPwEndDt(String pwEndDt) {
        this.pwEndDt = pwEndDt;
    }

    public String getPwPosition() {
        return pwPosition;
    }

    public void setPwPosition(String pwPosition) {
        this.pwPosition = pwPosition;
    }

    public long getPwRegEmpId() {
        return pwRegEmpId;
    }

    public void setPwRegEmpId(long pwRegEmpId) {
        this.pwRegEmpId = pwRegEmpId;
    }

    public long getOwRegEmpScanGunId() {
        return owRegEmpScanGunId;
    }

    public void setOwRegEmpScanGunId(long owRegEmpScanGunId) {
        this.owRegEmpScanGunId = owRegEmpScanGunId;
    }

    public long getPwConfirmEmpId() {
        return pwConfirmEmpId;
    }

    public void setPwConfirmEmpId(long pwConfirmEmpId) {
        this.pwConfirmEmpId = pwConfirmEmpId;
    }

    public long getOwConfirmEmpScanGunId() {
        return owConfirmEmpScanGunId;
    }

    public void setOwConfirmEmpScanGunId(long owConfirmEmpScanGunId) {
        this.owConfirmEmpScanGunId = owConfirmEmpScanGunId;
    }

    public long getPwBoxEmpId() {
        return pwBoxEmpId;
    }

    public void setPwBoxEmpId(long pwBoxEmpId) {
        this.pwBoxEmpId = pwBoxEmpId;
    }

    public long getPwBoxScanGunId() {
        return pwBoxScanGunId;
    }

    public void setPwBoxScanGunId(long pwBoxScanGunId) {
        this.pwBoxScanGunId = pwBoxScanGunId;
    }

    public String getPwAfterSealImg() {
        return pwAfterSealImg;
    }

    public void setPwAfterSealImg(String pwAfterSealImg) {
        this.pwAfterSealImg = pwAfterSealImg;
    }

    public String getPwReceiptNumber() {
        return pwReceiptNumber;
    }

    public void setPwReceiptNumber(String pwReceiptNumber) {
        this.pwReceiptNumber = pwReceiptNumber;
    }

    public String getPwReceiptDt() {
        return pwReceiptDt;
    }

    public void setPwReceiptDt(String pwReceiptDt) {
        this.pwReceiptDt = pwReceiptDt;
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