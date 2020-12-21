package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_service_evaluation
*/
public class ServiceEvaluationBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * express_bill_id
    */
    private String expressBillId;

    /**
    * express_bill
    */
    private long expressBill;

    /**
    * pack_score
    */
    private long packScore;

    /**
    * delivery_time_score
    */
    private String deliveryTimeScore;

    /**
    * courier_score
    */
    private long courierScore;

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


    public ServiceEvaluationBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExpressBillId() {
        return expressBillId;
    }

    public void setExpressBillId(String expressBillId) {
        this.expressBillId = expressBillId;
    }

    public long getExpressBill() {
        return expressBill;
    }

    public void setExpressBill(long expressBill) {
        this.expressBill = expressBill;
    }

    public long getPackScore() {
        return packScore;
    }

    public void setPackScore(long packScore) {
        this.packScore = packScore;
    }

    public String getDeliveryTimeScore() {
        return deliveryTimeScore;
    }

    public void setDeliveryTimeScore(String deliveryTimeScore) {
        this.deliveryTimeScore = deliveryTimeScore;
    }

    public long getCourierScore() {
        return courierScore;
    }

    public void setCourierScore(long courierScore) {
        this.courierScore = courierScore;
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