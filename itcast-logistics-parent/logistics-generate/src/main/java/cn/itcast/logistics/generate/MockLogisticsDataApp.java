package cn.itcast.logistics.generate;

import cn.itcast.logistics.common.utils.DBHelper;
import cn.itcast.logistics.common.utils.RDBTool;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Oracle造数程序
 *     1. 将Kafka Topic删除，重新创建
 *     2. 先运行KuduStreamApp程序
 *     3. 再运行MockLogisticsDataApp
 */
public class MockLogisticsDataApp {

    private static String DAT_SUFFIX = ".csv";
    private static String ENCODING = "UTF-8";
    /** Oracle **/
    private static final String ORACLE_DDL_SQL_FILE = "/oracle-db.sql";
    private static List<String> oracleTableNames = Arrays.asList("tbl_transport_record","tbl_waybill","tbl_collect_package","tbl_consumer_sender_info","tbl_express_bill","tbl_express_package","tbl_warehouse_emp","tbl_out_warehouse","tbl_push_warehouse","tbl_route","tbl_address","tbl_dot_transport_tool","tbl_transport_tool","tbl_dot","tbl_warehouse_vehicle_map","tbl_warehouse_send_vehicle","tbl_warehouse_receipt","tbl_consumer_address_map","tbl_warehouse","tbl_codes","tbl_customer","tbl_deliver_package","tbl_areas","tbl_test","tbl_work_time","tbl_waybill_state_record","tbl_waybill_line","tbl_warehouse_receipt_detail","tbl_warehouse_rack_map","tbl_vehicle_monitor","tbl_store_grid","tbl_service_evaluation","tbl_push_warehouse_detail","tbl_postal_standard","tbl_pkg","tbl_out_warehouse_detail","tbl_job","tbl_goods_rack","tbl_fixed_area","tbl_department","tbl_delivery_record","tbl_deliver_region","tbl_courier","tbl_company_warehouse_map","tbl_company_transport_route_ma","tbl_company_dot_map","tbl_company","tbl_charge_standard","tbl_warehouse_transport_tool","tbl_emp","tbl_driver","tbl_emp_info_map");
    private static String ORACLE_USER_TABLES_SQL_KEY = "ORACLE_USER_TABLES_SQL";
    private static String ORACLE_TABLE_DDL_SQL_KEY = "ORACLE_TABLE_DDL_SQL";
    private static String ORACLE_TABLE_SCHEMA_SQL_KEY = "ORACLE_TABLE_SCHEMA_SQL";
    private static Map<String, String> oracleSqlHelps = new HashMap<String, String>() {{
        put(ORACLE_USER_TABLES_SQL_KEY, "SELECT TABLE_NAME,TABLESPACE_NAME FROM user_tables WHERE TABLESPACE_NAME='TBS_LOGISTICS'");
        put(ORACLE_TABLE_DDL_SQL_KEY, "SELECT dbms_metadata.get_ddl('TABLE',?) FROM DUAL");
        put(ORACLE_TABLE_SCHEMA_SQL_KEY, "SELECT COLUMN_NAME,DATA_TYPE FROM user_tab_columns WHERE TABLE_NAME=?");
    }};
    /** PUBLIC SQL **/
    private static String CLEAN_TABLE_SQL = "TRUNCATE TABLE ?";
    // Oracle JDBC
    private static final DBHelper oracleHelper = DBHelper.builder()
            .withDialect(DBHelper.Dialect.Oracle)
            .withUrl("jdbc:oracle:thin:@//192.168.88.10:1521/ORCL")
            .withUser("itcast")
            .withPassword("itcast")
            .build();

    public static void main( String[] args) {
        boolean isClean = true;
        if (args.length == 1 && args[0].matches("(true|false)")) {
            isClean = Boolean.valueOf(args[0]);
        }
        Map<String, List<String>> oracleSqls = buildOracleTableDML();
        /** ==== 初始化Oracle ==== **/
        Connection connection1 = oracleHelper.getConnection();
        // 清空表
        if(isClean) {
            // 清空Oracle表
            oracleTableNames.forEach(tableName -> {
                try {
                    System.out.println("==== 开始清空Oracle的：" + tableName + " 数据 ====");
                    RDBTool.update(CLEAN_TABLE_SQL, tableName, (sql, table) -> executeUpdate(connection1, sql.replaceAll("\\?", "\"" + table + "\""), 1));
                    System.out.println("==== 完成清空Oracle的：" + tableName + " 数据 ====");
                    Thread.sleep(200 * 2);
                } catch (Exception e) {}
            });
        }
        // 插入数据到Oracle表（每2秒插入一条记录）
        oracleSqls.forEach((table,sqlList) -> {
            try {
                System.out.println("==== 开始插入数据到Oracle的：" + table + " ====");
                sqlList.forEach(sql -> RDBTool.save(sql, sqlStr -> executeUpdate(connection1, sql, 1)));
                System.out.println("==== 完成插入数据到Oracle的：" + table + " ====");
                Thread.sleep(1000 * 2);
            } catch (Exception e) {}
        });
        oracleHelper.close(connection1);
        // 检查是否清空库的SQL
        checkSQL();
    }

    /**
     * 根据table读取csv，并从csv文件中拼接表的INSERT语句
     * @return
     */
    private static Map<String, List<String>> buildOracleTableDML() {
        Map<String, List<String>> sqls = new LinkedHashMap<>();
        // 从遍历表中获取表对应的数据文件
        oracleTableNames.forEach(table -> {
            String tableDatPath = null;
            try {
                // 根据表名获取类路径下的"表名.csv"绝对路径
                tableDatPath = MockLogisticsDataApp.class.getResource("/" + table + DAT_SUFFIX).getPath();
            } catch (Exception e) { }
            if(!StringUtils.isEmpty(tableDatPath)) {
                StringBuilder insertSQL = new StringBuilder();
                try {
                    // 读取"表名.csv"的数据
                    List<String> datas = IOUtils.readLines(new BOMInputStream(new FileInputStream(tableDatPath)), ENCODING);
                    // 取出首行的schema
                    String schemaStr = datas.get(0).replaceAll("\"","");
                    String[] fieldNames = schemaStr.split(",");
                    // 获取数据库中的schema定义
                    Map<String, String> schemaMap = getOracleTableSchema(table);
                    datas.remove(0);
                    List<String> tblSqls = new LinkedList<>();
                    datas.forEach(line->{
                        boolean chk = false;
                        insertSQL.append("INSERT INTO \"" + table + "\"(\"").append(schemaStr.replaceAll(",","\",\"")).append("\") VALUES(");
                        String[] vals = line.split(",");
                        for (int i = 0; i < vals.length; i++) {
                            String fieldName = fieldNames[i];
                            String type = schemaMap.get(fieldName);
                            String val = vals[i].trim();
                            if("NVARCHAR2".equals(type)) {insertSQL.append((StringUtils.isEmpty(val)?"NULL":"'"+val+"'")+",");}
                            else if("DATE".equals(type)) {insertSQL.append("to_date('"+val+"','yyyy-mm-dd hh24:mi:ss'),");}
                            else if("NUMBER".equals(type)) {insertSQL.append(""+(StringUtils.isEmpty(val)?"0":val)+",");}
                            else {insertSQL.append(val+",");}
                            int diff = 0;
                            if (i==vals.length-1&&fieldNames.length>vals.length) {
                                diff = fieldNames.length-vals.length;
                                for (int j = 0; j < diff; j++) {insertSQL.append("NULL,");}
                            }
                            chk = vals.length+diff == fieldNames.length;
                        }
                        insertSQL.setLength(insertSQL.length()-1);
                        insertSQL.append(")");
                        if(chk) {
                            tblSqls.add(insertSQL.toString());
                        }
                        insertSQL.setLength(0);
                    });
                    sqls.put(table, tblSqls);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return sqls;
    }

    /**
     * 根据table和fields获取schema
     * @param table     表名
     * @return
     */
    private static Map<String, String> getOracleTableSchema(String table) {
        Map<String, LinkedHashMap<String,String>> tableSchema = getOracleAllTableSchema(null);
        return tableSchema.get(table);
    }

    /**
     * 从项目的DDL文件中获取每一张表的schema信息<table,[<fieldName><fieldType>]>
     * @param path
     * @return
     */
    private static Map<String, LinkedHashMap<String,String>> getOracleAllTableSchema(String path) {
        if(StringUtils.isEmpty(path)) {
            path = MockLogisticsDataApp.class.getResource(ORACLE_DDL_SQL_FILE).getPath();
        }
        Map<String, LinkedHashMap<String,String>> tableSchema = new LinkedHashMap<>();
        try {
            List<String> ddlSQL = FileUtils.readLines(new File(path), ENCODING);
            String table = null;
            String curLine = null;
            Map<String, String> schema = new LinkedHashMap<>();
            for (int i=0; i<ddlSQL.size(); i++) {
                curLine = ddlSQL.get(i);
                if(StringUtils.isEmpty(curLine)) {
                    continue;
                }
                if (curLine.contains("CREATE TABLE ")) {
                    table = curLine.substring(13, curLine.lastIndexOf(" ")).replaceAll("\"","");
                    continue;
                }
                if (curLine.contains("PRIMARY KEY")) {
                    LinkedHashMap<String, String> _schema = new LinkedHashMap<>();
                    _schema.putAll(schema);
                    tableSchema.put(table, _schema);
                    table = null;
                    schema.clear();
                }
                if (!StringUtils.isEmpty(table)) {
                    int offset = curLine.indexOf("(");
                    if (offset==-1) {offset = curLine.indexOf(",");}
                    String fieldInfo = curLine.substring(0, offset);
                    if(!StringUtils.isEmpty(fieldInfo)) {
                        String[] arr = fieldInfo.replaceAll("\"","").split(" ",2);
                        String fieldName = arr[0].trim();
                        String fieldType = arr[1].trim();
                        schema.put(fieldName, fieldType);
                    }
                }
            }
        } catch (IOException e) {
        }
        return tableSchema;
    }



    /**
     * 执行增删改的SQL
     * @param sql
     */
    private static void executeUpdate(Connection connection, String sql, int dbType) {
        Statement st = null;
        ResultSet rs = null;
        int state = 0;
        try {
            if (null==connection||connection.isClosed()) {
                if(dbType==1){
                    connection = oracleHelper.getConnection();
                }
            }
            connection.setAutoCommit(false);
            st = connection.createStatement();
            state = st.executeUpdate(sql);
            if(sql.startsWith("INSERT")) {
                if (state > 0) {
                    connection.commit();
                    System.out.println("==== SQL执行成功："+sql+" ====");
                } else {
                    System.out.println("==== SQL执行失败："+sql+" ====");
                }
            } else {
                System.out.println("==== SQL执行成功："+sql+" ====");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(dbType==1){
                oracleHelper.close(rs, st, null);
            }
        }
    }

    /**
     * ## 检查是否删除表或清空数据成功 ##
     * SQL1：在Hue中检查kudu表是否被全部删除
     *          使用方式：
     *              A 在浏览器中打开：http://node2.itcast.cn:8889/hue
     *              B 输入命令show tables或者复制SQL1粘贴到hue编辑框中执行，结果为0表示正确
     * SQL2: 在Shell中检查clickhouse表数据是否被清空
     *          使用方式：
     *              A 通过SecureCRT连接node2.itcast.cn
     *              B 在root用户下输入命令：clickhouse-client -m --host node2.itcast.cn --user root --password 123456
     *              C 复制SQL2并粘贴到命令行中执行，结果为0表示正确
     */
    private static void checkSQL() {
        String useHueCheckSQL = "SELECT SUM(t.cnt) FROM (\n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_address) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_areas) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_charge_standard) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_codes) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_collect_package) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_company) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_company_dot_map) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_company_transport_route_ma) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_company_warehouse_map) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_consumer_address_map) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_consumer_sender_info) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_courier) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_customer) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_deliver_package) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_deliver_region) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_delivery_record) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_department) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_dot) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_dot_transport_tool) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_driver) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_emp) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_emp_info_map) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_express_bill) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_express_package) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_fixed_area) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_goods_rack) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_job) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_out_warehouse) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_pkg) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_postal_standard) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_push_warehouse) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_route) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_service_evaluation) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_store_grid) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_transport_tool) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_vehicle_monitor) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_emp) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_rack_map) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_receipt) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_send_vehicle) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_transport_tool) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_vehicle_map) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_waybill) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_waybill_line) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_waybill_state_record) UNION \n" +
                "\t(SELECT COUNT(1) AS cnt FROM logistics.tbl_work_time)\n" +
                ") AS t";
        String useClickhouseCheckSQL = "SELECT SUM(t.cnt) FROM (\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_address UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_areas UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_codes UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_collect_package UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_consumer_address_map UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_consumer_sender_info UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_courier UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_customer UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_deliver_package UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_dot UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_dot_transport_tool UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_express_bill UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_express_package UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_out_warehouse UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_pkg UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_push_warehouse UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_route UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_transport_record UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_transport_tool UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_emp UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_receipt UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_transport_tool UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_vehicle_map UNION ALL\n" +
                "\tSELECT COUNT(1) AS cnt FROM logistics.tbl_waybill\n" +
                ") AS t;";
        System.out.println("==== SQL1(Using Hue Query): \n"+useHueCheckSQL);
        System.out.println("==== SQL2(Using Clickhouse Query): \n"+useClickhouseCheckSQL);
    }


}
