package cn.itcast.logistics.common.beans.logistics;

import cn.itcast.logistics.common.beans.AbstractBean;

import java.io.Serializable;

/**
*  tbl_codes
*/
public class CodesBean extends AbstractBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    private long id;

    /**
    * name
    */
    private String name;

    /**
    * type
    */
    private int type;

    /**
    * code
    */
    private String code;

    /**
    * code_desc
    */
    private String codeDesc;

    /**
    * code_type
    */
    private String codeType;

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
    public CodesBean(){}
    public CodesBean(long id, String name, int type, String code, String codeDesc, String codeType, long state, String cdt, String udt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.code = code;
        this.codeDesc = codeDesc;
        this.codeType = codeType;
        this.state = state;
        this.cdt = cdt;
        this.udt = udt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
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

    @Override
    public String toString() {
        return "CodesBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", code='" + code + '\'' +
                ", codeDesc='" + codeDesc + '\'' +
                ", codeType='" + codeType + '\'' +
                ", state=" + state +
                ", cdt='" + cdt + '\'' +
                ", udt='" + udt + '\'' +
                '}';
    }
}