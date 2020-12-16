package cn.itcast.logistics.common.beans;

import java.io.Serializable;

/**
 * @ClassName AbstractBean
 */
public class AbstractBean implements Serializable {
    private String opType = "insert";
    /**
     * 1:insert、2:update、3:delete
     * @return
     */
    public String getOpType() {return opType;}
    public void setOpType(String opType) {this.opType = opType;}
}
