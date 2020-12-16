-- ## 在sys用户下执行 ## --
-- 查询所有表空间
SELECT * FROM dba_data_files;
-- 创建表空间
CREATE TABLESPACE "TBS_LOGISTICS" DATAFILE '/u01/app/oracle/oradata/orcl/tbs_logistics.dat' SIZE 500M AUTOEXTEND ON NEXT 32M MAXSIZE UNLIMITED EXTENT MANAGEMENT LOCAL;
-- 创建itcast用户
CREATE USER itcast IDENTIFIED BY itcast DEFAULT TABLESPACE TBS_LOGISTICS;
-- 为itcast授予dba等权限
GRANT connect,resource,dba to itcast;

-- ## 在itcast用户下执行 ## --
-- 使用itcast用户登录oracle
conn itcast/itcast;
-- 创建所有的序列
CREATE SEQUENCE tbl_emp_info_map_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_driver_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_emp_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_warehouse_tt_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_charge_standard_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_company_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_company_dot_map_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_company_route_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_company_warehouse_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_courier_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_deliver_region_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_delivery_record_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_department_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_fixed_area_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_goods_rack_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_job_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_out_warehouse_dtl_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_pkg_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_postal_standard_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_push_warehouse_dtl_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_service_evaluation_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_store_grid_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_vehicle_monitor_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_warehouse_rack_map_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_warehouse_receipt_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_waybill_line_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_waybill_record_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_work_time_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_test_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_areas_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_deliver_package_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_customer_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_codes_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_warehouse_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_consumer_address_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_warehouse_receipt_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_warehouse_send_vehicle_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_warehouse_vehicle_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_dot_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_transport_tool_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_dot_transport_tool_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_address_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_route_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_push_warehouse_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_out_warehouse_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_warehouse_emp_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_express_package_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_express_bill_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_consumer_sender_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_collect_package_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;
CREATE SEQUENCE tbl_waybill_id_seq INCREMENT BY 1 START WITH 1 MINVALUE 1 MAXVALUE 999999999999999;

-- 创建所有的业务表
CREATE TABLE "tbl_emp_info_map" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"company_id" NUMBER(19,0), 
"dot_id" NUMBER(19,0), 
"emp_id" NUMBER(19,0), 
"job_id" NUMBER(19,0), 
"dep_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
CONSTRAINT "PK_TBL_EMP_INFO_MAP" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS";

CREATE TABLE "tbl_driver" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"job_number" NVARCHAR2(50), 
"name" NVARCHAR2(50), 
"gender" NVARCHAR2(100),
"birathday" DATE, 
"state" NUMBER(19,0), 
"driver_license_number" NVARCHAR2(100), 
"driver_license_type" NUMBER(19,0), 
"get_driver_license_dt" DATE, 
"car_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_DRIVER" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_emp" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"emp_number" NVARCHAR2(50), 
"emp_name" NVARCHAR2(50), 
"emp_gender" NUMBER(10,0), 
"emp_birathday" DATE, 
"state" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_EMP" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_warehouse_transport_tool" (	
"id" NUMBER(19,0) NOT NULL ENABLE, 
"warehouse_id" NUMBER(19,0), 
"transport_tool_id" NUMBER(19,0), 
"allocate_dt" DATE, 
"state" NUMBER(10,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_WAREHOUSE_TRANSPORT_TOOL" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_charge_standard" (	
"id" NUMBER(19,0) NOT NULL ENABLE, 
"start_area_id" NUMBER(19,0), 
"stop_area_id" NUMBER(19,0), 
"first_weight_charge" NUMBER(19,0), 
"follow_up_weight_charge" NUMBER(19,0), 
"prescription" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_CHARGE_STANDARD" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS";

CREATE TABLE "tbl_company" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"company_name" NVARCHAR2(50), 
"city_id" NUMBER(19,0), 
"company_number" NVARCHAR2(50), 
"company_addr" NVARCHAR2(100), 
"company_addr_gis" NVARCHAR2(100), 
"company_tel" NVARCHAR2(20), 
"is_sub_company" NUMBER(19,0), 
"state" NUMBER(10,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_COMPANY" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS";

CREATE TABLE "tbl_company_dot_map" (	
"id" NUMBER(19,0) NOT NULL ENABLE, 
"company_id" NUMBER(19,0), 
"dot_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_COMPANY_DOT_MAP" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_company_transport_route_ma" 
(
"id" NUMBER(19,0) NOT NULL ENABLE, 
"company_id" NUMBER(19,0), 
"transport_route_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_COMPANY_TRANSPORT_ROUTE" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_company_warehouse_map" (	
"id" NUMBER(19,0) NOT NULL ENABLE, 
"company_id" NUMBER(19,0), 
"warehouse_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_COMPANY_WAREHOUSE_MAP" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_courier" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"job_num" NVARCHAR2(50), 
"name" NVARCHAR2(50), 
"birathday" DATE, 
"tel" NVARCHAR2(20), 
"pda_num" NVARCHAR2(50), 
"car_id" NUMBER(19,0), 
"postal_standard_id" NUMBER(19,0), 
"work_time_id" NUMBER(19,0), 
"dot_id" NUMBER(19,0), 
"state" NUMBER(10,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_COURIER" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_deliver_region" 
(
"id" NUMBER(19,0) NOT NULL ENABLE, 
"search_keyword" NVARCHAR2(100), 
"search_assist_keyword" NVARCHAR2(100), 
"area_id" NUMBER(19,0), 
"fixed_area_id" NUMBER(19,0), 
"state" NUMBER(10,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_DELIVER_REGION" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_delivery_record" (	
"id" NUMBER(19,0) NOT NULL ENABLE, 
"cur_warehouse_id" NVARCHAR2(50), 
"vehicle_id" NUMBER(19,0), 
"start_vehicle_dt" DATE,
"next_warehouse_id" NUMBER(19,0), 
"predict_arrivals_dt" DATE,
"actua_arrivals_dt" DATE,
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_DELIVERY_RECORD" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_department" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"dep_name" NVARCHAR2(50), 
"dep_level" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_DEPARTMENT" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_fixed_area" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"name" NVARCHAR2(50), 
"emp_id" NUMBER(19,0), 
"operator_dt" DATE, 
"operator_id" NUMBER(19,0), 
"gis_fence" NVARCHAR2(200), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_FIXED_AREA" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_goods_rack" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"warehouse_name" NVARCHAR2(50), 
"warehouse_addr" NVARCHAR2(100),
"warehouse_addr_gis" NVARCHAR2(50), 
"company_id" NUMBER(19,0), 
"employee_id" NVARCHAR2(200), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_GOODS_RACK" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_job" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"job_name" NVARCHAR2(50), 
"job_level" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_JOB" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_out_warehouse_detail" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"push_warehouse_id" NUMBER(19,0), 
"push_warehouse_bill" NVARCHAR2(100), 
"warehouse_id" NUMBER(19,0), 
"waybill_id" NUMBER(19,0), 
"pkg_id" NUMBER(19,0), 
"pkg_desc" NVARCHAR2(100), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_OUT_WAREHOUSE_DETAIL" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_pkg" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"pw_bill" NVARCHAR2(50), 
"pw_dot_id" NUMBER(19,0), 
"warehouse_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_PKG" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_postal_standard" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"name" NVARCHAR2(50), 
"min_weight" NVARCHAR2(50), 
"min_length" NVARCHAR2(50), 
"max_length" NVARCHAR2(50), 
"trajectory" NVARCHAR2(50), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_POSTAL_STANDARD" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_push_warehouse_detail" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"push_warehouse_id" NUMBER(19,0), 
"push_warehouse_bill" NVARCHAR2(50), 
"warehouse_id" NUMBER(19,0), 
"pw_start_dt" NVARCHAR2(50), 
"pw_end_dt" NVARCHAR2(50), 
"pack_id" NUMBER(19,0), 
"pack_desc" NVARCHAR2(50), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_PUSH_WAREHOUSE_DETAIL" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_service_evaluation" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"express_bill_id" NVARCHAR2(100), 
"express_bill" NUMBER(19,0), 
"pack_score" NUMBER(10,0), 
"delivery_time_score" NUMBER(10,0), 
"courier_score" NUMBER(10,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_SERVICE_EVALUATION" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_store_grid" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"warehouse_name" NVARCHAR2(50), 
"warehouse_addr" NVARCHAR2(100),
"warehouse_addr_gis" NVARCHAR2(50), 
"company_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_STORE_GRID" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_vehicle_monitor" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"delivery_record" NUMBER(19,0), 
"empId" NUMBER(19,0), 
"express_bill__id" NVARCHAR2(200), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_VEHICLE_MONITOR" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_warehouse_rack_map" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"warehouse_name" NVARCHAR2(50), 
"warehouse_addr" NVARCHAR2(100),
"warehouse_addr_gis" NVARCHAR2(50), 
"company_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_WAREHOUSE_RACK_MAP" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_warehouse_receipt_detail" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"waybill_id" NUMBER(19,0), 
"pkg_id" NUMBER(19,0), 
"receipt_bill_id" NUMBER(19,0), 
"receipt_bill" NVARCHAR2(100), 
"operator_id" NUMBER(19,0), 
"state" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_WAREHOUSE_RECEIPT_DETAI" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_waybill_line" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"waybill_number" NVARCHAR2(100), 
"route_id" NUMBER(19,0), 
"serial_number" NVARCHAR2(100), 
"transport_tool" NUMBER(19,0), 
"delivery_record_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_WAYBILL_LINE" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_waybill_state_record" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"waybill_id" NUMBER(19,0), 
"waybill_number" NVARCHAR2(100), 
"employee_id" NVARCHAR2(100), 
"consignee_id" NUMBER(19,0), 
"cur_warehouse_id" NUMBER(10,0), 
"next_warehouse_id" NUMBER(10,0), 
"deliverer_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_WAYBILL_STATE_RECORD" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_work_time" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"name" NVARCHAR2(50), 
"start_dt" NVARCHAR2(100),
"stop_dt" NVARCHAR2(100),
"saturday_start_dt" NVARCHAR2(100),
"saturday_stop_dt" NVARCHAR2(100),
"sunday_start_dt" NVARCHAR2(100),
"sunday_stop_dt" NVARCHAR2(100),
"state" NUMBER(10,0), 
"company_id" NUMBER(10,0), 
"operator_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_WORK_TIME" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_test" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"name" NVARCHAR2(50), 
 CONSTRAINT "PK_TBL_TEST" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_areas" (
"id" NUMBER(11,0) NOT NULL ENABLE, 
"name" NVARCHAR2(40), 
"pid" NUMBER(11,0), 
"sname" NVARCHAR2(40), 
"level" NVARCHAR2(11), 
"citycode" NVARCHAR2(20), 
"yzcode" NVARCHAR2(20), 
"mername" NVARCHAR2(100), 
"lng" NUMBER(11,4), 
"lat" NUMBER(11,4), 
"pinyin" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_AREAS" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_deliver_package" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"emp_id" NUMBER(19,0), 
"waybill_id" NUMBER(19,0), 
"waybill_number" NVARCHAR2(100), 
"express_bill_id" NUMBER(19,0), 
"express_bill_number" NVARCHAR2(100), 
"package_id" NUMBER(19,0), 
"collect_package_dt" DATE, 
"rece_type" NUMBER(19,0), 
"rece_dt" DATE, 
"state" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_DELIVER_PACKAGE" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_customer" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"name" NVARCHAR2(50), 
"tel" NVARCHAR2(20), 
"mobile" NVARCHAR2(20), 
"email" NVARCHAR2(50), 
"type" NUMBER(10,0), 
"is_own_reg" NUMBER(10,0), 
"reg_dt" DATE, 
"reg_channel_id" NUMBER(10,0), 
"state" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"last_login_dt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_CUSTOMER" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_codes" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"name" NVARCHAR2(50), 
"type" NUMBER(19,0), 
"code" NVARCHAR2(50), 
"code_desc" NVARCHAR2(100), 
"code_type" NVARCHAR2(50), 
"state" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
 CONSTRAINT "PK_TBL_CODES" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_warehouse" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"name" NVARCHAR2(50), 
"addr" NVARCHAR2(19), 
"addr_gis" NVARCHAR2(50), 
"company_id" NUMBER(19,0), 
"employee_id" NUMBER(19,0), 
"type" NUMBER(10,0), 
"area" NVARCHAR2(50), 
"is_lease" NUMBER(10,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_WAREHOUSE" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_consumer_address_map" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"consumer_id" NUMBER(19,0), 
"address_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_CUSTOMER_SENDER_MAP" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_warehouse_receipt" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"bill" NVARCHAR2(100), 
"type" NUMBER(19,0), 
"warehouse_id" NUMBER(19,0), 
"operator_id" NUMBER(19,0), 
"state" NUMBER(10,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_WAREHOUSE_RECEIPT" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_warehouse_send_vehicle" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"out_warehouse_id" NUMBER(19,0), 
"out_warehouse_waybill_id" NUMBER(19,0), 
"out_warehouse_waybill_number" NVARCHAR2(100), 
"vehicle_id" NUMBER(19,0), 
"driver1_id" NUMBER(19,0), 
"driver2_id" NUMBER(19,0), 
"start_vehicle_dt" DATE, 
"next_warehouse_id" NUMBER(19,0), 
"predict_arrivals_dt" DATE, 
"actual_arrivals_dt" DATE, 
"state" NUMBER(10,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_WAREHOUSE_SEND_VEHICLE" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_warehouse_vehicle_map" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"warehouse_id" NUMBER(19,0), 
"vehicle_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_COMPANY_VEHICLE_MAP" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_dot" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"dot_number" NVARCHAR2(50), 
"dot_name" NVARCHAR2(50), 
"dot_addr" NVARCHAR2(100), 
"dot_gis_addr" NVARCHAR2(100), 
"dot_tel" NVARCHAR2(20), 
"company_id" NUMBER(19,0), 
"manage_area_id" NUMBER(19,0), 
"manage_area_gis" NVARCHAR2(100), 
"state" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_DOT" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_transport_tool" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"brand" NVARCHAR2(100), 
"model" NVARCHAR2(100), 
"type" NUMBER(19,0), 
"given_load" NVARCHAR2(100), 
"load_cn_unit" NVARCHAR2(100), 
"load_en_unit" NVARCHAR2(100), 
"buy_dt" DATE, 
"license_plate" NVARCHAR2(100), 
"state" NVARCHAR2(100), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_TRANSPORT_TOOL" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_dot_transport_tool" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"dot_id" NUMBER(19,0), 
"transport_tool_id" NUMBER(19,0), 
"allocate_dt" DATE, 
"state" NUMBER(10,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_DOT_TRANSPORT_TOOL" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_address" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"name" NVARCHAR2(50), 
"tel" NVARCHAR2(20), 
"mobile" NVARCHAR2(20), 
"detail_addr" NVARCHAR2(100), 
"area_id" NUMBER(19,0), 
"gis_addr" NVARCHAR2(20), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_CUSTOMER_ADDRESS" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_route" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"start_station" NVARCHAR2(50), 
"start_station_area_id" NUMBER(19,0), 
"start_warehouse_id" NUMBER(19,0), 
"end_station" NVARCHAR2(50), 
"end_station_area_id" NUMBER(19,0), 
"end_warehouse_id" NUMBER(19,0), 
"mileage_m" NUMBER(10,0), 
"time_consumer_minute" NUMBER(10,0), 
"state" NUMBER(10,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_ROUTE" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_push_warehouse" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"pw_waybill_id" NUMBER(19,0), 
"pw_waybill_number" NVARCHAR2(50), 
"pw_dot_id" NUMBER(19,0), 
"warehouse_id" NUMBER(19,0), 
"emp_id" NUMBER(19,0), 
"pw_start_dt" DATE, 
"pw_end_dt" DATE, 
"pw_position" NVARCHAR2(50), 
"pw_reg_emp_id" NUMBER(19,0), 
"ow_reg_emp_scan_gun_id" NUMBER(19,0), 
"pw_confirm_emp_id" NUMBER(19,0), 
"ow_confirm_emp_scan_gun_id" NUMBER(19,0), 
"pw_box_emp_id" NUMBER(19,0), 
"pw_box_scan_gun_id" NUMBER(19,0), 
"pw_after_seal_img" NVARCHAR2(100), 
"pw_receipt_number" NVARCHAR2(100), 
"pw_receipt_dt" DATE, 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_PUSH_WAREHOUSE" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_out_warehouse" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"pw_waybill_id" NUMBER(19,0), 
"pw_waybill_number" NVARCHAR2(100), 
"ow_dot_id" NUMBER(19,0), 
"warehouse_id" NUMBER(19,0), 
"ow_vehicle_id" NUMBER(19,0), 
"ow_driver_emp_id" NUMBER(19,0), 
"ow_follow1_emp_id" NUMBER(19,0), 
"ow_follow2_emp_id" NUMBER(19,0), 
"ow_start_dt" DATE, 
"ow_end_dt" DATE, 
"ow_position" NVARCHAR2(50), 
"ow_reg_emp_id" NUMBER(19,0), 
"ow_reg_scan_gun_id" NUMBER(19,0), 
"ow_confirm_emp_id" NUMBER(19,0), 
"ow_confirm_scan_gun_id" NUMBER(19,0), 
"ow_pre_seal_img" NVARCHAR2(100), 
"ow_receipt_number" NVARCHAR2(100), 
"ow_receipt_dt" DATE, 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_OUT_WAREHOUSE" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_warehouse_emp" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"job_num" NVARCHAR2(50), 
"name" NVARCHAR2(50), 
"birathday" DATE, 
"tel" NVARCHAR2(20), 
"type" NUMBER(10,0), 
"warehouse_id" NUMBER(19,0), 
"state" NUMBER(10,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_WAREHOUSE_EMP" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_express_package" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"scan_gun_id" NVARCHAR2(19), 
"name" NVARCHAR2(50), 
"cid" NUMBER(10,2), 
"weight" NUMBER(10,2), 
"amount" NUMBER(10,2), 
"coupon_id" NUMBER(19,0), 
"coupon_amount" NUMBER(10,2), 
"actual_amount" NUMBER(10,2), 
"insured_price" NUMBER(10,2), 
"is_fragile" NVARCHAR2(20), 
"send_address_id" NUMBER(19,0), 
"recv_address_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_EXPRESS_PACKAGE" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_express_bill" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"express_number" NVARCHAR2(50), 
"cid" NUMBER(19,0), 
"eid" NUMBER(19,0), 
"order_channel_id" NUMBER(19,0), 
"order_dt" DATE, 
"order_terminal_type" NUMBER(10,0), 
"order_terminal_os_type" NUMBER(10,0), 
"reserve_dt" DATE, 
"is_collect_package_timeout" NUMBER(10,0), 
"timeout_dt" DATE, 
"type" NUMBER(10,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_EXPRESS_BILL" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_consumer_sender_info" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"ciid" NUMBER(19,0), 
"pkg_id" NUMBER(19,0), 
"express_bill_id" NUMBER(19,0), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_CUSTOMER_SENDER_INFO" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_collect_package" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"cid" NUMBER(19,0), 
"eid" NUMBER(19,0), 
"pkg_id" NUMBER(19,0), 
"express_bill_id" NUMBER(19,0), 
"express_bill_number" NVARCHAR2(100), 
"state" NUMBER(10,0), 
"collect_package_dt" DATE, 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_COLLECT_PACKAGE" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS"; 

CREATE TABLE "tbl_waybill" (
"id" NUMBER(19,0) NOT NULL ENABLE, 
"express_bill_number" NVARCHAR2(100), 
"waybill_number" NVARCHAR2(100), 
"cid" NUMBER(19,0), 
"eid" NUMBER(19,0), 
"order_channel_id" NUMBER(19,0), 
"order_dt" DATE, 
"order_terminal_type" NUMBER(10,0), 
"order_terminal_os_type" NUMBER(10,0), 
"reserve_dt" DATE, 
"is_collect_package_timeout" NUMBER(10,0), 
"pkg_id" NUMBER(19,0), 
"pkg_number" NVARCHAR2(100), 
"timeout_dt" NVARCHAR2(100), 
"transform_type" NUMBER(10,0), 
"delivery_customer_name" NVARCHAR2(100), 
"delivery_addr" NVARCHAR2(100), 
"delivery_mobile" NVARCHAR2(100), 
"delivery_tel" NVARCHAR2(100), 
"receive_customer_name" NVARCHAR2(100), 
"receive_addr" NVARCHAR2(100), 
"receive_mobile" NVARCHAR2(100), 
"receive_tel" NVARCHAR2(100), 
"cdt" DATE, 
"udt" DATE, 
"remark" NVARCHAR2(100), 
 CONSTRAINT "PK_TBL_WAYBILL" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS";

-- 运输记录表
CREATE TABLE "tbl_transport_record" (	
	"id" NUMBER(19,0) NOT NULL ENABLE, 
	"pw_id" NUMBER(19,0), 
	"pw_waybill_id" NUMBER(19,0), 
	"pw_waybill_number" NVARCHAR2(100), 
	"ow_id" NUMBER(19,0), 
	"ow_waybill_id" NUMBER(19,0), 
	"ow_waybill_number" NVARCHAR2(100), 
	"sw_id" NUMBER(19,0), 
	"ew_id" NUMBER(19,0), 
	"transport_tool_id" NUMBER(19,0), 
	"pw_driver1_id" NUMBER(19,0), 
	"pw_driver2_id" NUMBER(19,0), 
	"pw_driver3_id" NUMBER(19,0), 
	"ow_driver1_id" NUMBER(19,0), 
	"ow_driver2_id" NUMBER(19,0), 
	"ow_driver3_id" NUMBER(19,0), 
	"route_id" NUMBER(19,0), 
	"distance" NUMBER(10,0), 
	"duration" NUMBER(10,0), 
	"state" NUMBER(10,0), 
	"start_vehicle_dt" DATE, 
	"predict_arrivals_dt" DATE, 
	"actual_arrivals_dt" DATE, 
	"cdt" DATE, 
	"udt" DATE, 
	"remark" NVARCHAR2(100), 
	 CONSTRAINT "PK_TBL_TRANSPORT_RECORD" PRIMARY KEY ("id")
) TABLESPACE "TBS_LOGISTICS";

-- 查看itcast用户下所有的表
SELECT table_name FROM user_tables;
-- 修改表字段名称
ALTER TABLE "your table" RENAME COLUMN "your old column" to "your new column";
-- 修改表字段类型
ALTER TABLE "tbl_goods_rack" MODIFY ("warehouse_addr" NVARCHAR2(100))
ALTER TABLE "tbl_driver" MODIFY ("gender" NVARCHAR2(100))
ALTER TABLE "tbl_work_time" MODIFY ("start_dt" NVARCHAR2(100))
ALTER TABLE "tbl_work_time" MODIFY ("stop_dt" NVARCHAR2(100))
ALTER TABLE "tbl_work_time" MODIFY ("saturday_start_dt" NVARCHAR2(100))
ALTER TABLE "tbl_work_time" MODIFY ("saturday_stop_dt" NVARCHAR2(100))
ALTER TABLE "tbl_work_time" MODIFY ("sunday_start_dt" NVARCHAR2(100))
ALTER TABLE "tbl_work_time" MODIFY ("sunday_stop_dt" NVARCHAR2(100))
ALTER TABLE "tbl_warehouse_rack_map" MODIFY ("warehouse_addr" NVARCHAR2(100))
ALTER TABLE "tbl_store_grid" MODIFY ("warehouse_addr" NVARCHAR2(100))
ALTER TABLE "tbl_delivery_record" MODIFY ("predict_arrivals_dt" DATE)
ALTER TABLE "tbl_delivery_record" MODIFY ("actua_arrivals_dt" DATE)
ALTER TABLE "tbl_delivery_record" MODIFY ("start_vehicle_dt" DATE)

