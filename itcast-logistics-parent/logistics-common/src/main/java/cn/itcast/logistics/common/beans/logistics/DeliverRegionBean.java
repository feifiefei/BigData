package cn.itcast.logistics.common.beans.logistics;
import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_deliver_region
*/
public class DeliverRegionBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * search_keyword
    */
    private String searchKeyword;

    /**
    * search_assist_keyword
    */
    private String searchAssistKeyword;

    /**
    * area_id
    */
    private long areaId;

    /**
    * fixed_area_id
    */
    private long fixedAreaId;

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


    public DeliverRegionBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public String getSearchAssistKeyword() {
        return searchAssistKeyword;
    }

    public void setSearchAssistKeyword(String searchAssistKeyword) {
        this.searchAssistKeyword = searchAssistKeyword;
    }

    public long getAreaId() {
        return areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public long getFixedAreaId() {
        return fixedAreaId;
    }

    public void setFixedAreaId(long fixedAreaId) {
        this.fixedAreaId = fixedAreaId;
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