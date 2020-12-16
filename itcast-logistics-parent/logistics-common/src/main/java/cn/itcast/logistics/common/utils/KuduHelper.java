package cn.itcast.logistics.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;
import org.apache.kudu.client.KuduScanner.KuduScannerBuilder;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.*;

public class KuduHelper {

	private Builder builder;
	private KuduClient client;
	public static final Map<String, Type> TYPE_MAP = new HashMap<String, Type>() {{
		put("byte", Type.INT8);
		put("short", Type.INT16);
		put("char", Type.STRING);
		put("int", Type.INT32);
		put("integer", Type.INT32);
		put("float", Type.FLOAT);
		put("long", Type.INT64);
		put("double", Type.DOUBLE);
		put("boolean", Type.BOOL);
		put("string", Type.STRING);
		put("date", Type.STRING);
	}};

	private KuduHelper() {
	}

	private KuduHelper(Builder builder) {
		this.builder = builder;
		client = new KuduClient.KuduClientBuilder(builder.getMaster()).build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private String master;
		private String table;
		private Properties prop = new Properties();
		protected String getMaster() {
			return master;
		}
		public Builder withMaster(String master) {
			this.master = master;
			return this;
		}
		protected String getTable() {
			return table;
		}
		public Builder withTable(String table) {
			this.table = table;
			return this;
		}
		public Properties getProp() {
			return prop;
		}
		public void withProp(Properties prop) {
			this.prop = prop;
		}
		public KuduHelper build() {
			return new KuduHelper(this);
		}
	}
	
	/**
	 * 将obj转换为Kudu表的Schema
	 * @param obj
	 * @param primary
	 * @return
	 */
	private Schema getSchema(Object obj, String primary) {
		List<Pair<String, String>> pairs = ReflectUtil.objToPair(obj);
		List<ColumnSchema> columns = new LinkedList<ColumnSchema>();
		for (Pair<String,String> pair : pairs) {
			String name = pair.getValue0();
			Type type = TYPE_MAP.get(pair.getValue1().toLowerCase());
			ColumnSchema.ColumnSchemaBuilder csb = new ColumnSchema.ColumnSchemaBuilder(name, type);
			if (name.equals(primary)) {
				columns.add(csb.key(true).build());
			} else {
				columns.add(csb.nullable(true).build());
			}
		}
		return new Schema(columns);
	}
	
	/**
	 * 创建表
	 * @param table
	 * @param schema
	 * @param options
	 */
	public <T> void createTable(String table, T obj, String primary) {
		try {
			if (!client.tableExists(table)) {
				Schema schema = getSchema(obj, primary);
				CreateTableOptions options = new CreateTableOptions();
				options.setRangePartitionColumns(Arrays.asList(primary));
				options.setNumReplicas(1);
				client.createTable(table, schema, options);
			} else {
				System.err.println("==== "+table+"已存在 ====");
			}
		} catch (KuduException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询所有的表
	 * @return
	 */
	public List<String> listTable() {
		try {
			ListTablesResponse tablesList = client.getTablesList();
			return tablesList.getTablesList();
		} catch (KuduException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 删除表
	 * @param table
	 * @param schema
	 * @param options
	 */
	public void deleteTable(String table) {
		try {
			if (client.tableExists(table)) {
				client.deleteTable(table);
			} else {
				System.err.println("==== "+table+"不存在 ====");
			}
		} catch (KuduException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 保存数据到Kudu表中
	 * @param table
	 * @param obj
	 * @return
	 */
	public boolean save(String table, Object obj) {
		boolean status = false;
		KuduSession session = null;
		try {
			if (client.tableExists(table)) {
				session = client.newSession();
				KuduTable kuduTable = client.openTable(table);
				Insert insert = kuduTable.newInsert();
				OperationResponse result = session.apply(buildInsertAndUpdate(insert, obj));
				if (!result.hasRowError()) {
					status = true;
				}
			} else {
				System.err.println("==== "+table+"不存在 ====");
			}
		} catch (KuduException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null!=session&&!session.isClosed()) {
					session.close();					
				}
			} catch (KuduException e) {
				e.printStackTrace();
			}
		}
		return status;
	}
	
	/**
	 * 查询表数据
	 * @param <T>
	 * @param table
	 * @param obj
	 * @param limit
	 * @return
	 */
	public <T> List<T> query(String table, T obj, boolean condition, int limit) {
		List<T> result = new ArrayList<T>();
		KuduScanner scanner = null;
        try {
        	KuduTable kuduTable = client.openTable(table);
			KuduScannerBuilder newScannerBuilder = client.newScannerBuilder(kuduTable);
        	// 设置查询条件
        	List<Triplet<String, String, Object>> triplets = ReflectUtil.objToTriplet(obj, true);
        	KuduPredicate predicate = null;
        	Map<String, Triplet<String, String, Object>> columnNames = new LinkedHashMap<String, Triplet<String, String, Object>>();
        	for (Triplet<String, String, Object> triplet : triplets) {
        		String fieldName = triplet.getValue0();
        		String fieldType = triplet.getValue1().toLowerCase();
        		Object fieldValue = triplet.getValue2();
        		columnNames.put(fieldName, triplet);
        		Type kuduType = TYPE_MAP.get(fieldType);
        		if (condition) {
        			if (fieldType.equals("byte")) {
        				byte byteVal = (byte)fieldValue;
        				if (byteVal>0) {
        					predicate = KuduPredicate.newComparisonPredicate(
        							new ColumnSchema.ColumnSchemaBuilder(fieldName, kuduType).build(), KuduPredicate.ComparisonOp.EQUAL, byteVal);
						}
        			}
        			if (fieldType.equals("short")) {
        				short shortVal = (short)fieldValue;
        				if (shortVal>0) {
        					predicate = KuduPredicate.newComparisonPredicate(
        							new ColumnSchema.ColumnSchemaBuilder(fieldName, kuduType).build(), KuduPredicate.ComparisonOp.EQUAL, shortVal);
						}
        			}
					if (fieldType.equals("int")) {
						int intVal = (int)fieldValue;
						if (intVal>0) {
							predicate = KuduPredicate.newComparisonPredicate(
									new ColumnSchema.ColumnSchemaBuilder(fieldName, kuduType).build(), KuduPredicate.ComparisonOp.EQUAL, intVal);
						}
        			}
        			if (fieldType.equals("integer")) {
        				int intVal = (int)fieldValue;
						if (intVal>0) {
							predicate = KuduPredicate.newComparisonPredicate(
									new ColumnSchema.ColumnSchemaBuilder(fieldName, kuduType).build(), KuduPredicate.ComparisonOp.EQUAL, intVal);
						}
        			}
        			if (fieldType.equals("float")) {
        				float floatVal = (float)fieldValue;
        				if (floatVal>0.0F) {
        					predicate = KuduPredicate.newComparisonPredicate(
        							new ColumnSchema.ColumnSchemaBuilder(fieldName, kuduType).build(), KuduPredicate.ComparisonOp.EQUAL, floatVal);							
						}
        			}
        			if (fieldType.equals("long")) {
        				long longVal = (long)fieldValue;
        				if (longVal>0L) {
        					predicate = KuduPredicate.newComparisonPredicate(
        							new ColumnSchema.ColumnSchemaBuilder(fieldName, kuduType).build(), KuduPredicate.ComparisonOp.EQUAL, (long)fieldValue);
        				}
        			}
        			if (fieldType.equals("double")) {
        				double doubleVal = (double)fieldValue;
        				if (doubleVal>0.0D) {
        					predicate = KuduPredicate.newComparisonPredicate(
        							new ColumnSchema.ColumnSchemaBuilder(fieldName, kuduType).build(), KuduPredicate.ComparisonOp.EQUAL, doubleVal);						
        				}
        			}
        			if (fieldType.equals("boolean")) {
        				predicate = KuduPredicate.newComparisonPredicate(
        						new ColumnSchema.ColumnSchemaBuilder(fieldName, kuduType).build(), KuduPredicate.ComparisonOp.EQUAL, (boolean)fieldValue);
        			}
					if (fieldType.equals("string")) {
						String stringVal = fieldValue.toString();
						if (!StringUtils.isEmpty(stringVal)) {
							predicate = KuduPredicate.newComparisonPredicate(
									new ColumnSchema.ColumnSchemaBuilder(fieldName, kuduType).build(), KuduPredicate.ComparisonOp.EQUAL, stringVal);							
						}
        			}
        			if (fieldType.equals("date")) {
						if (null!=fieldValue) {
							String stringDateValue = DateUtil.dateToStr((Date)fieldValue, DateUtil.FMT_YMDHMSS.get());
							predicate = KuduPredicate.newComparisonPredicate(
									new ColumnSchema.ColumnSchemaBuilder(fieldName, kuduType).build(), KuduPredicate.ComparisonOp.EQUAL, stringDateValue);							
						}
        			}
        			if (null!=predicate) {
        				newScannerBuilder.addPredicate(predicate);
        			}
        		}
			}
        	newScannerBuilder.setProjectedColumnNames(new ArrayList<String>(columnNames.keySet()));					
        	if (limit>0) {
        		newScannerBuilder.limit(limit);
			}
			scanner = newScannerBuilder.build();
			// 接收返回值
			while (scanner.hasMoreRows()) {
			    RowResultIterator rowIterator = scanner.nextRows();
				while(rowIterator.hasNext()) {
					RowResult row = rowIterator.next();
					List<ColumnSchema> columns = row.getSchema().getColumns();
					List<Triplet<String, String, Object>> newTriplets = new LinkedList<Triplet<String,String,Object>>();
					Triplet<String, String, Object> newTriplet = null;
					for (ColumnSchema columnSchema : columns) {
						Triplet<String, String, Object> triplet = columnNames.get(columnSchema.getName());
						//for (Entry<String, Triplet<String, String, Object>> entry : columnNames.entrySet()) {
						//Triplet<String, String, Object> triplet = entry.getValue();
						String fieldName = triplet.getValue0();
						String fieldType = triplet.getValue1().toLowerCase();
						if (fieldType.equals("byte")) {
							newTriplet = new Triplet<>(fieldName, fieldType, row.getByte(fieldName));
						}
						if (fieldType.equals("short")) {
							newTriplet = new Triplet<>(fieldName, fieldType, row.getShort(fieldName));
						}
						if (fieldType.equals("int")) {
							newTriplet = new Triplet<>(fieldName, fieldType, row.getInt(fieldName));
						}
						if (fieldType.equals("integer")) {
							newTriplet = new Triplet<>(fieldName, fieldType, row.getInt(fieldName));
						}
						if (fieldType.equals("float")) {
							newTriplet = new Triplet<>(fieldName, fieldType, row.getFloat(fieldName));
						}
						if (fieldType.equals("long")) {
							newTriplet = new Triplet<>(fieldName, fieldType, row.getLong(fieldName));
						}
						if (fieldType.equals("double")) {
							newTriplet = new Triplet<>(fieldName, fieldType, row.getDouble(fieldName));
						}
						if (fieldType.equals("boolean")) {
							newTriplet = new Triplet<>(fieldName, fieldType, row.getBoolean(fieldName));
						}
						if (fieldType.equals("string")) {
							String value = row.getString(fieldName);
							if (!StringUtils.isEmpty(value)) {
								newTriplet = new Triplet<>(fieldName, fieldType, value);
							}
						}
						if (fieldType.equals("date")) {
							String value = row.getString(fieldName);
							if (!StringUtils.isEmpty(value)) {
								newTriplet = new Triplet<>(fieldName, fieldType, value);
							}
						}
						newTriplets.add(newTriplet);
					}
					result.add(ReflectUtil.genObj(newTriplets, obj));
				}
			}
		} catch (KuduException e) {
			e.printStackTrace();
		} finally {
			try {
				if (scanner!=null&&!scanner.isClosed()) {
					scanner.close();
				}
			} catch (KuduException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 修改表数据
	 * @param <T>
	 * @param table
	 * @param obj
	 * @return
	 */
	public <T> boolean update(String table, T obj) {
		boolean status = false;
		KuduSession session = null;
		try {
			if (client.tableExists(table)) {
				session = client.newSession();
				KuduTable kuduTable = client.openTable(table);
				Update update = kuduTable.newUpdate();
				OperationResponse deleteResult = session.apply(buildInsertAndUpdate(update, obj));
				if (!deleteResult.hasRowError()) {
					status = true;
				}				
			} else {
				System.err.println("==== "+table+"不存在 ====");
			}
		} catch (KuduException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null!=session&&!session.isClosed()) {
					session.close();					
				}
			} catch (KuduException e) {
				e.printStackTrace();
			}
		}
		return status;
	}
	
	/**
	 * 删除表数据
	 * @param <T>
	 * @param table
	 * @param obj
	 * @return
	 */
	public <T> boolean delete(String table, T obj) {
		boolean status = false;
		KuduSession session = null;
		try {
			if (client.tableExists(table)) {
				session = client.newSession();
				KuduTable kuduTable = client.openTable(table);
				Delete delete = kuduTable.newDelete();
				OperationResponse deleteResult = session.apply(delete);
				if (!deleteResult.hasRowError()) {
					status = true;
				}
			} else {
				System.err.println("==== "+table+"不存在 ====");
			}
		} catch (KuduException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null!=session&&!session.isClosed()) {
					session.close();					
				}
			} catch (KuduException e) {
				e.printStackTrace();
			}
		}
		return status;
	}
	
	/**
	 * 构建kudu的Insert或Update中的row
	 * @param insertAndUpdate
	 * @param data
	 * @return
	 */
	private Operation buildInsertAndUpdate(Operation insertAndUpdate, Object data) {
		List<Triplet<String, String, Object>> results = ReflectUtil.objToTriplet(data, false);
		if (results.size()>0) {
			for (Triplet<String, String, Object> triplet : results) {
				String col = triplet.getValue0();
				String type = triplet.getValue1().toLowerCase();
				Object value = triplet.getValue2();
				if (type.equals("int")) {
					insertAndUpdate.getRow().addInt(col, (int)value);
				}
				if (type.equals("double")) {
					insertAndUpdate.getRow().addDouble(col, (double)value);
				}
				if (type.equals("float")) {
					insertAndUpdate.getRow().addFloat(col, (float)value);
				}
				if (type.equals("long")) {
					insertAndUpdate.getRow().addLong(col, (long)value);
				}
				if (type.equals("string")) {
					insertAndUpdate.getRow().addString(col, value.toString());
				}
				if (type.equals("date")) {
					insertAndUpdate.getRow().addString(col, DateUtil.dateToStr((Date)value, DateUtil.FMT_YMDHMSS.get()));
				}
				if (type.equals("byte")) {
					insertAndUpdate.getRow().addByte(col, (byte)value);
				}
				if (type.equals("boolean")) {
					insertAndUpdate.getRow().addBoolean(col, (boolean)value);
				}
			}
		}
		return insertAndUpdate;
	}
	
	/**
	 * 释放kudu连接
	 */
	public void close() {
		try {
			if (null!=client) {
				client.close();				
			}
		} catch (KuduException e) {
			e.printStackTrace();
		}
	}
	
	
}


