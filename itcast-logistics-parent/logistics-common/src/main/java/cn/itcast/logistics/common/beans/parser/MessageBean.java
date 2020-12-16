package cn.itcast.logistics.common.beans.parser;

import java.io.Serializable;

/**
 * 根据数据源定义抽象类，数据源：ogg 和 canal， 两者有共同的table属性
 */
public abstract class MessageBean implements Serializable {

	private static final long serialVersionUID = 373363837132138843L;
	
	private String table;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	@Override
	public String toString() {
		return table;
	}

}
