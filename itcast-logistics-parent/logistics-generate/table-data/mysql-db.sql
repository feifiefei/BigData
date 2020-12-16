-- 客户关系管理系统数据库
create database crm character set utf8 collate utf8_general_ci;

DROP TABLE IF EXISTS `crm_address`;
-- 客户地址表
CREATE TABLE `crm_address` (
  `id` bigint NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `tel` varchar(20) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `detail_addr` varchar(100) DEFAULT NULL,
  `area_id` bigint DEFAULT NULL,
  `gis_addr` varchar(20) DEFAULT NULL,
  `cdt` datetime DEFAULT NULL,
  `udt` datetime DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 客户表
DROP TABLE IF EXISTS `crm_customer`;
CREATE TABLE `crm_customer` (
  `id` bigint NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `tel` varchar(20) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `type` bigint DEFAULT NULL,
  `is_own_reg` bigint DEFAULT NULL,
  `reg_dt` datetime DEFAULT NULL,
  `reg_channel_id` bigint DEFAULT NULL,
  `state` int DEFAULT NULL,
  `cdt` datetime DEFAULT NULL,
  `udt` datetime DEFAULT NULL,
  `last_login_dt` datetime DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 客户-地址-关联表
DROP TABLE IF EXISTS `crm_consumer_address_map`;
CREATE TABLE `crm_consumer_address_map` (
  `id` bigint NOT NULL,
  `consumer_id` bigint DEFAULT NULL,
  `address_id` bigint DEFAULT NULL,
  `cdt` datetime DEFAULT NULL,
  `udt` datetime DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;