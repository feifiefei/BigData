-- MySQL
SELECT COUNT(1) AS cnt FROM crm_address UNION ALL
SELECT COUNT(1) AS cnt FROM crm_customer UNION ALL
SELECT COUNT(1) AS cnt FROM crm_consumer_address_map
-- Oracle
SELECT COUNT(1) AS cnt FROM "tbl_address" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_areas" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_charge_standard" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_codes" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_collect_package" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_company" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_company_dot_map" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_company_transport_route_ma" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_company_warehouse_map" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_consumer_address_map" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_consumer_sender_info" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_courier" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_customer" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_deliver_package" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_deliver_region" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_delivery_record" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_department" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_dot" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_dot_transport_tool" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_driver" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_emp" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_emp_info_map" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_express_bill" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_express_package" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_fixed_area" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_goods_rack" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_job" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_out_warehouse" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_out_warehouse_detail" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_pkg" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_postal_standard" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_push_warehouse" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_push_warehouse_detail" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_route" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_service_evaluation" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_store_grid" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_test" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_transport_record" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_transport_tool" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_vehicle_monitor" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_warehouse" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_warehouse_emp" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_warehouse_rack_map" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_warehouse_receipt" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_warehouse_receipt_detail" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_warehouse_send_vehicle" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_warehouse_transport_tool" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_warehouse_vehicle_map" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_waybill" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_waybill_line" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_waybill_state_record" UNION ALL
SELECT COUNT(1) AS cnt FROM "tbl_work_time"
-- ClickHouse
SELECT SUM(t.cnt) FROM (
	SELECT COUNT(1) AS cnt FROM logistics.tbl_address UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_areas UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_codes UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_collect_package UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_consumer_address_map UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_consumer_sender_info UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_courier UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_customer UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_deliver_package UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_dot UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_dot_transport_tool UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_express_bill UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_express_package UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_out_warehouse UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_pkg UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_push_warehouse UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_route UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_transport_record UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_transport_tool UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_emp UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_receipt UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_transport_tool UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_vehicle_map UNION ALL
	SELECT COUNT(1) AS cnt FROM logistics.tbl_waybill
) AS t;
-- Kudu
SELECT SUM(t.cnt) FROM (
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_address) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_areas) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_charge_standard) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_codes) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_collect_package) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_company) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_company_dot_map) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_company_transport_route_ma) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_company_warehouse_map) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_consumer_address_map) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_consumer_sender_info) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_courier) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_customer) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_deliver_package) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_deliver_region) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_delivery_record) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_department) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_dot) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_dot_transport_tool) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_driver) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_emp) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_emp_info_map) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_express_bill) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_express_package) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_fixed_area) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_goods_rack) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_job) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_out_warehouse) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_pkg) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_postal_standard) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_push_warehouse) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_route) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_service_evaluation) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_store_grid) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_transport_tool) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_vehicle_monitor) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_emp) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_rack_map) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_receipt) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_send_vehicle) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_transport_tool) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_warehouse_vehicle_map) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_waybill) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_waybill_line) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_waybill_state_record) UNION
	(SELECT COUNT(1) AS cnt FROM logistics.tbl_work_time)
) AS t