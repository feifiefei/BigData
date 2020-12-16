package cn.itcast.logistics.common.beans.parser;

import java.util.List;
import java.util.Map;

/**
 * 定义消费canal数据对应的JavaBean对象
 * {
 *     "data": [{
 *        "id": "1",
 *        "name": "北京",
 *        "tel": "222",
 *        "mobile": "1111",
 *        "detail_addr": "北京",
 *        "area_id": "1",
 *        "gis_addr": "1",
 *        "cdt": "2020-10-08 17:20:12",
 *        "udt": "2020-11-05 17:20:16",
 *        "remark": null
 *        }],
 *     "database": "crm",
 *     "es": 1602148867000,
 *     "id": 15,
 *     "isDdl": false,
 *     "mysqlType": {
 *        "id": "bigint(20)",
 *        "name": "varchar(50)",
 *        "tel": "varchar(20)",
 *        "mobile": "varchar(20)",
 *        "detail_addr": "varchar(100)",
 *        "area_id": "bigint(20)",
 *        "gis_addr": "varchar(20)",
 *        "cdt": "datetime",
 *        "udt": "datetime",
 *        "remark": "varchar(100)"
 *    },
 *     "old": [{
 *        "tel": "111"
 *    }],
 *     "sql": "",
 *     "sqlType": {
 *        "id": -5,
 *        "name": 12,
 *        "tel": 12,
 *        "mobile": 12,
 *        "detail_addr": 12,
 *        "area_id": -5,
 *        "gis_addr": 12,
 *        "cdt": 93,
 *        "udt": 93,
 *        "remark": 12
 *    },
 *     "table": "crm_address",
 *     "ts": 1602148867311,
 *     "type": "UPDATE"               //修改数据
 * }
 */
public class CanalMessageBean extends MessageBean {

	private static final long serialVersionUID = 4977535609865399200L;

	//数据库名称
	private String database;
	private Long es;
	private Long id;
	private boolean isDdl;
	private Map<String, Object> mysqlType;
	private String old;
	private String sql;
	private Map<String, Object> sqlType;
	private Long ts;
	private String type;

	//操作的数据集合
	private List<Map<String, Object>> data;

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public Long getEs() {
		return es;
	}

	public void setEs(Long es) {
		this.es = es;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isDdl() {
		return isDdl;
	}

	public void setDdl(boolean ddl) {
		isDdl = ddl;
	}

	public Map<String, Object> getMysqlType() {
		return mysqlType;
	}

	public void setMysqlType(Map<String, Object> mysqlType) {
		this.mysqlType = mysqlType;
	}

	public String getOld() {
		return old;
	}

	public void setOld(String old) {
		this.old = old;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Map<String, Object> getSqlType() {
		return sqlType;
	}

	public void setSqlType(Map<String, Object> sqlType) {
		this.sqlType = sqlType;
	}

	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 重写父类的settable方法，将表名修改成统一的前缀
	 * @param table
	 */
	@Override
	public void setTable(String table) {
		if(table != null && ! table.equals("")){
			if(table.startsWith("crm_")) {
				table = table.replace("crm_", "tbl_");
			}
		}
		super.setTable(table);
	}
}
