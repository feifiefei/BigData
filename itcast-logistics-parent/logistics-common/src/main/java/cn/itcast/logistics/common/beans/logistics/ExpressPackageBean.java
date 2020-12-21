package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_express_package
*/
public class ExpressPackageBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * scan_gun_id
    */
    private String scanGunId;

    /**
    * name
    */
    private String name;

    /**
    * cid
    */
    private long cid;

    /**
    * weight
    */
    private double weight;

    /**
    * amount
    */
    private double amount;

    /**
    * coupon_id
    */
    private long couponId;

    /**
    * coupon_amount
    */
    private double couponAmount;

    /**
    * actual_amount
    */
    private double actualAmount;

    /**
    * insured_price
    */
    private double insuredPrice;

    /**
    * is_fragile
    */
    private String isFragile;

    /**
    * send_address_id
    */
    private long sendAddressId;

    /**
    * recv_address_id
    */
    private long recvAddressId;

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


    public ExpressPackageBean() {
    }

    public ExpressPackageBean(long aLong, String string, String string1, long aLong1, double aDouble, double aDouble1, long aLong2, double aDouble2, double aDouble3, double aDouble4, String string2, long aLong3, long aLong4, String String, String String1, String string3) {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScanGunId() {
        return scanGunId;
    }

    public void setScanGunId(String scanGunId) {
        this.scanGunId = scanGunId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public double getInsuredPrice() {
        return insuredPrice;
    }

    public void setInsuredPrice(double insuredPrice) {
        this.insuredPrice = insuredPrice;
    }

    public String getIsFragile() {
        return isFragile;
    }

    public void setIsFragile(String isFragile) {
        this.isFragile = isFragile;
    }

    public long getSendAddressId() {
        return sendAddressId;
    }

    public void setSendAddressId(long sendAddressId) {
        this.sendAddressId = sendAddressId;
    }

    public long getRecvAddressId() {
        return recvAddressId;
    }

    public void setRecvAddressId(long recvAddressId) {
        this.recvAddressId = recvAddressId;
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