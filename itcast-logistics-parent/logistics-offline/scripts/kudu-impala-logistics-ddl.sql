
/*
    SQL 脚本中多行注释
 */
-- Kudu表：tbl_transport_tool
CREATE EXTERNAL TABLE `tbl_transport_tool` STORED AS KUDU
TBLPROPERTIES(
    'kudu.table_name' = 'tbl_transport_tool',
    'kudu.master_addresses' = 'node2.itcast.cn:7051') ;

-- Kudu表：tbl_dot_transport_tool
CREATE EXTERNAL TABLE `tbl_dot_transport_tool` STORED AS KUDU
TBLPROPERTIES(
    'kudu.table_name' = 'tbl_dot_transport_tool',
    'kudu.master_addresses' = 'node2.itcast.cn:7051') ;

-- Kudu表：tbl_warehouse_transport_tool
CREATE EXTERNAL TABLE `tbl_warehouse_transport_tool` STORED AS KUDU
TBLPROPERTIES(
    'kudu.table_name' = 'tbl_warehouse_transport_tool',
    'kudu.master_addresses' = 'node2.itcast.cn:7051') ;
