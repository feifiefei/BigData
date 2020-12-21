package cn.itcast.logistics.common.beans.parser;

import java.util.Map;

/**
 * 定义消费OGG数据的JavaBean对象
 * {
 *     "table": "ITCAST.tbl_route",            //表名：库名.表名
 *     "op_type": "U",                         //操作类型：U表示修改
 *     "op_ts": "2020-10-08 09:10:54.000774",
 *     "current_ts": "2020-10-08T09:11:01.925000",
 *     "pos": "00000000200006645758",
 *     "before": {                            //操作前的字段集合
 *        "id": 104,
 *        "start_station": "东莞中心",
 *        "start_station_area_id": 441900,
 *        "start_warehouse_id": 1,
 *        "end_station": "蚌埠中转部",
 *        "end_station_area_id": 340300,
 *        "end_warehouse_id": 107,
 *        "mileage_m": 1369046,
 *        "time_consumer_minute": 56172,
 *        "state": 1,
 *        "cdt": "2020-02-02 18:51:39",
 *        "udt": "2020-02-02 18:51:39",
 *        "remark": null
 *        },
 *     "after": {                         //操作后的字段集合
 *        "id": 104,
 *        "start_station": "东莞中心",
 *        "start_station_area_id": 441900,
 *        "start_warehouse_id": 1,
 *        "end_station": "TBD",
 *        "end_station_area_id": 340300,
 *        "end_warehouse_id": 107,
 *        "mileage_m": 1369046,
 *        "time_consumer_minute": 56172,
 *        "state": 1,
 *        "cdt": "2020-02-02 18:51:39",
 *        "udt": "2020-02-02 18:51:39",
 *        "remark": null
 *    }
 * }
 */
public class OggMessageBean extends MessageBean {

	private static final long serialVersionUID = -4763944161833712521L;

	//定义操作类型
	private String op_type;
	//操作时间
	private String op_ts;
	//同步时间
	private String current_ts;
	//偏移量
	private String pos;
	//操作之前的数据
	private Map<String, Object> before;
	//操作之后的数据
	private Map<String, Object> after;

	@Override
	public void setTable(String table) {
		//如果表名不为空
		if (table != null && !table.equals("")) {
			table = table.replaceAll("[A-Z]+\\.", "");
		}
		super.setTable(table);
	}

	public String getOp_type() {
		return op_type;
	}

	public void setOp_type(String op_type) {
		this.op_type = op_type;
	}

	public String getOp_ts() {
		return op_ts;
	}

	public void setOp_ts(String op_ts) {
		this.op_ts = op_ts;
	}

	public String getCurrent_ts() {
		return current_ts;
	}

	public void setCurrent_ts(String current_ts) {
		this.current_ts = current_ts;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public Map<String, Object> getBefore() {
		return before;
	}

	public void setBefore(Map<String, Object> before) {
		this.before = before;
	}

	public Map<String, Object> getAfter() {
		return after;
	}

	public void setAfter(Map<String, Object> after) {
		this.after = after;
	}

	@Override
	public String toString() {
		return "OggMessageBean{" +
			"table='" + super.getTable() + '\'' +
			", op_type='" + op_type + '\'' +
			", op_ts='" + op_ts + '\'' +
			", current_ts='" + current_ts + '\'' +
			", pos='" + pos + '\'' +
			", before=" + before +
			", after=" + after +
			'}';
	}

	/**
	 * 返回需要处理的列的集合
	 */
	public Map<String, Object> getValue() {
		//如果执行的是删除操作，则返回before节点的列的集合，如果执行的是插入和更新操作，则返回after节点的列的集合
		if (after == null) {
			return before;
		} else {
			return after;
		}
	}

}
