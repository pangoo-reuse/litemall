DROP TABLE IF EXISTS `litemall_shipping_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_shipping_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `express_freight_min` tinyint(3) DEFAULT '1' COMMENT '满减最低消费',
  `freight_value` tinyint(3) DEFAULT '1' COMMENT '不满所需运费',
  `region_id` int(11) NOT NULL COMMENT '区域ID',
  `region_code` int(11) NOT NULL COMMENT '区域编号',
  `region_address` varchar(63) NOT NULL DEFAULT '' COMMENT '显示的地址',
  `enabled` tinyint(1) DEFAULT '0' COMMENT '是否启动',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='运输配置表';