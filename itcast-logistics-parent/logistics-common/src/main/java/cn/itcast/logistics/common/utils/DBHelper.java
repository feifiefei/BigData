package cn.itcast.logistics.common.utils;

import cn.itcast.logistics.common.beans.logistics.CollectPackageBean;
import oracle.jdbc.pool.OracleDataSource;
import org.javatuples.Triplet;
import ru.yandex.clickhouse.ClickHouseDataSource;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class DBHelper {

	private final Builder builder;
	private OracleDataSource ods;
	private ClickHouseDataSource cds;
	private Connection connection;
	
	
	private DBHelper(Builder builder) {
		this.builder = builder;
		try {
			Dialect dialect = builder.getDialect();
			if (dialect== Dialect.Oracle) {
				ods = new OracleDataSource();
				ods.setURL(builder.getUrl());
				ods.setConnectionProperties(builder.getProp());
			} else if (dialect== Dialect.ClickHouse||dialect== Dialect.MySQL) {
				Class.forName(builder.getDriver());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	public Connection getConnection() {
		try {
			if (builder.getDialect()== Dialect.Oracle) {
				connection = ods.getConnection();
			} else if (builder.getDialect()== Dialect.ClickHouse) {
				if (null==connection||connection.isClosed()) {
					if (null != builder.getProp()) {
						cds = new ClickHouseDataSource(builder.getUrl(), builder.getProp());
					} else {
						cds = new ClickHouseDataSource(builder.getUrl());
					}
					connection = cds.getConnection();					
				}
			} else {
				connection = DriverManager.getConnection(builder.getUrl(), builder.getUser(), builder.getPassword());				
			}
		} catch (Exception e) {
			System.err.println(builder.dialect.name()+"异常！\n"+builder.toString()+"\n错误信息为"+e.getMessage()+ "\n程序强制退出！");
			System.exit(-1);
		}
		return connection;
	}
	
	/**
	 * 关闭rs
	 * @param rs
	 */
	public void close(ResultSet rs) {
		closeAll(rs, null, null, null);
	}
	
	/**
	 * 关闭st
	 * @param ps
	 */
	public void close(Statement st) {
		closeAll(null, st, null, null);
	}
	
	/**
	 * 关闭ps
	 * @param ps
	 */
	public void close(PreparedStatement ps) {
		closeAll(null, null, ps, null);
	}
	
	/**
	 * 关闭connection
	 * @param connection
	 */
	public void close(Connection connection) {
		closeAll(null, null, null, connection);
	}
	
	public void close(ResultSet rs, Statement st, Connection connection) {
		closeAll(rs, st, null, connection);
	}

	public void close(Statement st, Connection connection) {
		closeAll(null, st, null, connection);
	}

	public void close(ResultSet rs, PreparedStatement ps, Connection connection) {
		closeAll(rs, null, ps, connection);
	}
	
	public void close(PreparedStatement ps, Connection connection) {
		closeAll(null, null, ps, connection);
	}
	
	/**
	 * 关闭rs、ps、connection
	 * @param rs
	 * @param ps
	 * @param connection
	 */
	public void closeAll(ResultSet rs, Statement st, PreparedStatement ps, Connection connection) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void closeAll() {
		close(connection);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public enum Dialect {
		ClickHouse,Oracle,MySQL
	}
	
	public static class Builder {
		private String driver;
		private String url;
		private String user;
		private String password;
		private Properties prop = new Properties();
		private Dialect dialect;
		protected String getDriver() {
			return driver;
		}
		public Builder withUrl(String url) {
			this.url = url;
			return this;
		}
		protected String getUrl() {
			return url;
		}
		public Builder withUser(String user) {
			this.user = user;
			return this;
		}
		protected String getUser() {
			return user;
		}
		public Builder withPassword(String password) {
			this.password = password;
			return this;
		}
		protected String getPassword() {
			return password;
		}
		public Properties getProp() {
			return prop;
		}
		public void withProp(Properties prop) {
			this.prop = prop;
		}
		public Builder withDialect(Dialect dialect) {
			this.dialect = dialect;
			return this;
		}
		protected Dialect getDialect() {
			return dialect;
		}
		public Builder withDriver(String driver) {
			this.driver = driver;
			return this;
		}
		public DBHelper build() {
			if (dialect == Dialect.Oracle && "sys".equals(user.toLowerCase())) {
				prop.put("internal_logon", "sysdba");
			} else {
				prop.put("user", user);
				prop.put("password", password);
			}
			return new DBHelper(this);
		}

		@Override
		public String toString() {
			return "\tdriver=" + driver + "\n\t" +
					"url=" + url + "\n\t" +
					"user=" + user + "\n\t" +
					"password=" + password + "\n\t" +
					"prop=" + prop + "\n\t" +
					"dialect=" + dialect;
		}
	}
	
	public void createDB(String sql) {
		int status = 0;
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			ps = connection.prepareStatement(sql);
			status = ps.executeUpdate();
			System.out.println(status);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(ps);
		}
	}
	
	public void show(String sql) {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = getConnection();
			ps = connection.prepareStatement(sql);
			List<String> colNames = new ArrayList<String>();
			List<Object> colValues = new ArrayList<Object>();
			ResultSetMetaData metaData = ps.getMetaData();
			if (metaData!=null) {
				int columnCount = metaData.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					colNames.add(metaData.getColumnName(i));
				}
				rs = ps.executeQuery();
				while (rs.next()) {
					for (String colName : colNames) {
						colValues.add(rs.getObject(colName));
					}
				}
				System.out.println(colValues);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(ps);
		}
	}
	
	/**
	 * 
	 * @param string
	 * @param object
	 * @param string2
	 */
	public <T> void createTable(String table, T obj, String primary) {
		String createSql = buildSQL(table, obj, primary, null);
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			ps = connection.prepareStatement(createSql);
			int status = ps.executeUpdate();
			if (status > 0) {
				System.out.println("==== 执行成功！ ====");
			} else {
				if (builder.dialect== Dialect.Oracle || builder.dialect== Dialect.MySQL) {
					connection.rollback();					
				}
				System.out.println("==== 执行失败！ ====");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(ps);
		}
	}
	
	/**
	 * 根据JavaBean字段类型来获取数据库的数据类型
	 * @param <T>
	 * @param table
	 * @param obj
	 * @param primary
	 * @param type
	 * @return
	 */
	private <T> String buildSQL(String table, T obj, final String primary, String type) {
		List<Triplet<String, String, Object>> triplets = ReflectUtil.objToTriplet(obj, true);
		StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE ");
		if (builder.getDialect() == Dialect.MySQL) {
			sqlBuilder.append("IF NOT EXIST "+table+"(");
			triplets.forEach(trp -> {
				String fieldType = trp.getValue1().toLowerCase();
				String fieldName = trp.getValue0();
				sqlBuilder.append(fieldName+" "+buildSQLDataType(fieldType)+",");
				if (fieldName.equals(primary)) {
					// sqlBuilder.append("");
				}
			});
		}
		if (builder.getDialect() == Dialect.Oracle) {
		}
		// VersionedCollapsingMergeTree
		if (builder.getDialect() == Dialect.ClickHouse) {
			sqlBuilder.append("IF NOT EXISTS "+table+"(");
			triplets.forEach(trp -> {
				String fieldType = trp.getValue1().toLowerCase();
				String fieldName = trp.getValue0();
				sqlBuilder.append(fieldName+" "+buildSQLDataType(fieldType)+",");
				if (fieldName.equals(primary)) {
					// sqlBuilder.append("");
				}
			});
			sqlBuilder.append("sign Int8, version Int64) ENGINE = VersionedCollapsingMergeTree(sign, version) ORDER BY "+primary);
		}
		return sqlBuilder.toString();
	}
	
	private String buildSQLDataType(String javaDataType) {
		String sqlDataType = null;
		if (builder.getDialect() == Dialect.MySQL) {
			if (javaDataType.equals("byte")) {
				sqlDataType = "TINYINT";
			}
			if (javaDataType.equals("short")) {
				sqlDataType = "SMALLINT";
			}
			if (javaDataType.equals("int")) {
				sqlDataType = "INT";
			}
			if (javaDataType.equals("long")) {
				sqlDataType = "BIGINT";
			}
			if (javaDataType.equals("float")) {
				sqlDataType = "FLOAT";
			}
			if (javaDataType.equals("double")) {
				sqlDataType = "DOUBLE";
			}
			if (javaDataType.equals("date")) {
				sqlDataType = "Date";
			}
			if (javaDataType.equals("char")) {
				sqlDataType = "CHAR(2)";
			}
			if (javaDataType.equals("string")) {
				sqlDataType = "VARCHAR(100)";
			}
			if (javaDataType.equals("boolean")) {
				sqlDataType = "BOOLEAN";
			}
		}
		if (builder.getDialect() == Dialect.Oracle) {
			
		}
		if (builder.getDialect() == Dialect.ClickHouse) {
			if (javaDataType.equals("byte")) {
				sqlDataType = "Int8";
			}
			if (javaDataType.equals("short")) {
				sqlDataType = "Int16";
			}
			if (javaDataType.equals("int")) {
				sqlDataType = "Int32";
			}
			if (javaDataType.equals("long")) {
				sqlDataType = "Int64";
			}
			if (javaDataType.equals("float")) {
				sqlDataType = "Float32";
			}
			if (javaDataType.equals("double")) {
				sqlDataType = "Float64";
			}
			if (javaDataType.equals("date")) {
				sqlDataType = "DateTime";
			}
			if (javaDataType.equals("char")) {
				sqlDataType = "FixedString(2)";
			}
			if (javaDataType.equals("string")) {
				sqlDataType = "String";
			}
			if (javaDataType.equals("boolean")) {
				sqlDataType = "Int8";
			}
		}
		return sqlDataType;
	}
	
	/**
	 * 写入数据到ClickHouse
	 * @param obj						数据
	 * @param table						表名称
	 * @param isVersionedCollapsing		是否处理状态行和取消行
	 */
	public void save(Object obj, String table, boolean isVersionedCollapsing) {
		List<Triplet<String, String, Object>> triplets = ReflectUtil.objToTriplet(obj, false);
		StringBuilder querySqlBuilder = new StringBuilder("SELECT sign,version FROM "+table +" WHERE ");
		StringBuilder insertSqlBuilder = new StringBuilder("INSERT INTO "+table +"(");
		if (isVersionedCollapsing) {
			triplets.forEach(trp->{
				insertSqlBuilder.append(trp.getValue0()+",");
				querySqlBuilder.append(trp.getValue0()+"=? AND ");
			});
			querySqlBuilder.setLength(querySqlBuilder.toString().length()-5);
			insertSqlBuilder.append("sign,version) VALUES(");
			triplets.forEach(trp -> {
				insertSqlBuilder.append("?,");
			});
			insertSqlBuilder.append("?,?)");
		} else {
			triplets.forEach(trp -> {
				insertSqlBuilder.append(trp.getValue0()+",");
			});
			insertSqlBuilder.setLength(insertSqlBuilder.length()-1);
			insertSqlBuilder.append(") VALUES(");
			triplets.forEach(trp -> {
				insertSqlBuilder.append("?,");
			});
			insertSqlBuilder.setLength(insertSqlBuilder.length()-1);
			insertSqlBuilder.append(")");
		}
		int sign = 0;
		int version = 0;
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = getConnection();
			ps = connection.prepareStatement(insertSqlBuilder.toString());
			int paramSize = triplets.size();
			for (int i=0; i<paramSize; i++) {
				Triplet<String, String, Object> trp = triplets.get(i);
				String fieldType = trp.getValue1().toLowerCase();
				Object fieldValue = trp.getValue2();
				int paramIndex = i+1;
				if (fieldType.equals("short")) {
					ps.setShort(paramIndex, (short)fieldValue);
				}
				if (fieldType.equals("int")) {
					ps.setLong(paramIndex, (int)fieldValue);
				}
				if (fieldType.equals("float")) {
					ps.setFloat(paramIndex, (float)fieldValue);
				}
				if (fieldType.equals("double")) {
					ps.setDouble(paramIndex, (double)fieldValue);
				}
				if (fieldType.equals("long")) {
					ps.setLong(paramIndex, (long)fieldValue);
				}
				if (fieldType.equals("boolean")) {
					boolean boolValue = (boolean)fieldValue;
					ps.setInt(paramIndex, boolValue?1:0);
				}
				if (fieldType.equals("date")) {
					Date dateValue = (Date)fieldValue;
					ps.setDate(paramIndex, new java.sql.Date(dateValue.getTime()));
				}
				if (fieldType.equals("string")) {
					ps.setString(paramIndex, fieldValue.toString());
				}
				System.out.println(paramIndex+" = "+fieldValue);
			}
			if (isVersionedCollapsing) {
				ps.setInt(paramSize+1, sign==0?1:-1);
				ps.setInt(paramSize+2, version==0?1:version++);
			}
			int status = ps.executeUpdate();
			System.out.println(status);
			if (status>0) {
				System.out.println("==== 写入成功！ ====");
			} else {
				if (builder.dialect== Dialect.Oracle || builder.dialect== Dialect.MySQL) {
					connection.rollback();
				}
				// ClickHouse不支持事务，无需回滚
				System.out.println("==== 写入失败！ ====");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (builder.dialect== Dialect.Oracle || builder.dialect== Dialect.MySQL) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			close(ps);
		}
	}
	
	public static void main(String[] args) throws Exception {
		DBHelper helper = DBHelper.builder()
				.withDialect(Dialect.MySQL)
				.withDriver("com.mysql.jdbc.Driver")
				.withUrl("jdbc:mysql:///test")
				.withUser("root")
				.withPassword("123456")
				.build();
		CollectPackageBean cpb = new CollectPackageBean();
		cpb.setId(1);
		cpb.setCid(1);
		cpb.setEid(1);
		cpb.setPkgId(1);
		cpb.setExpressBillId(1);
		cpb.setExpressBillNumber("1");
		cpb.setState(1);
		cpb.setCollectPackageDt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		cpb.setCdt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		cpb.setUdt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		cpb.setRemark("-");
		helper.save(cpb, "collect_package", true);
		
//		// ==== Oracle Test
//    	String url = "jdbc:oracle:thin:@//192.168.10.10:1521/ORCL";
//		String user = "sys";
//		String password = "Abcd1234.";
//		DBHelper helper = DBHelper.builder()
//			.withDialect(Dialect.Oracle)
//			.withUrl(url)
//			.withUser(user)
//			.withPassword(password)
//			.build();
//		Connection connection = helper.getConnection();
//		PreparedStatement ps = connection.prepareStatement("select * from user_users");
//		ResultSet rs = ps.executeQuery();
//		while (rs.next()) {
//			System.out.println(rs.getString(1));
//			
//		}
//		helper.close(rs, ps, connection);
//		// ==== MySQL Test
//		String driver = "com.mysql.jdbc.Driver";
//		url = "jdbc:mysql:///test?useUnicode=true&characterEncoding=utf8";
//		user = "root";
//		password = "123456";
//		helper = DBHelper.builder()
//				.withDialect(Dialect.MySQL)
//				.withDriver(driver)
//				.withUrl(url)
//				.withUser(user)
//				.withPassword(password)
//				.build();
//		connection = helper.getConnection();
//		ps = connection.prepareStatement("show tables");
//		rs = ps.executeQuery();
//		while (rs.next()) {
//			System.out.println(rs.getString(1));
//			
//		}
//		helper.close(rs, ps, connection);
	}

}
