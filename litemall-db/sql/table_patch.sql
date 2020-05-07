DROP TABLE IF EXISTS `litemall_shipping_config`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
DROP TABLE IF EXISTS `litemall_shipping_config`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_shipping_config`
(
    `id`                  int(11)        NOT NULL AUTO_INCREMENT,
    `express_freight_min` decimal(10, 2) NOT NULL DEFAULT '19.90' COMMENT '满减最低消费19.9',
    `freight_value`       decimal(10, 2) NOT NULL DEFAULT '9.90' COMMENT '不满所需运费9.9',
    `region_id`           int(11)        NOT NULL COMMENT '区域ID',
    `region_code`         int(11)        NOT NULL COMMENT '区域编号',
    `region_address`      varchar(63)    NOT NULL DEFAULT '' COMMENT '显示的地址',
    `enabled`             tinyint(1)              DEFAULT '0' COMMENT '是否启动',
    `created_time`        datetime                DEFAULT NULL COMMENT '创建时间',
    `update_time`         datetime                DEFAULT NULL COMMENT '更新时间',
    `deleted`             tinyint(1)              DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `region_code` (`region_code`),
    KEY `enabled` (`enabled`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4 COMMENT ='运输配置表';

ALTER TABLE `litemall_user`
    ADD `referral_code` varchar(64) COMMENT '用户分享码';

ALTER TABLE `litemall_order`
    ADD `p2p_order` tinyint(1) DEFAULT '0' COMMENT '是否闪购订单';


DROP TABLE IF EXISTS `litemall_referral_views`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_referral_views`
(
    `id`            int(11)        NOT NULL AUTO_INCREMENT,
    `referrer_id`   int(11) COMMENT '推广者ID',
    `viewer_id`     int(11) COMMENT '查看者ID',
    `referrer_name` varchar(63) COMMENT '推广者名字',
    `viewer_name`   varchar(63) COMMENT '查看者名字',
    `goods_id`      int(11)        NOT NULL COMMENT '产品ID',
    `goods_name`    varchar(63) COMMENT '产品名称',
    `view_price`    decimal(10, 2) NOT NULL DEFAULT '0' COMMENT '浏览的价格即为产品基础价(元)',
    `commission`    decimal(10, 2) NOT NULL DEFAULT '0' COMMENT '佣金(元 view_price * 0.04% )',
    `state`         int(1)         NOT NULL DEFAULT '-1' COMMENT '发放状态 1 已发放 -1未发放 0发放中',
    `created_time`  datetime                DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime                DEFAULT NULL COMMENT '更新时间',
    `deleted`       tinyint(1)              DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4 COMMENT ='推广浏览表';

DROP TABLE IF EXISTS `litemall_referral_orders`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_referral_orders`
(
    `id`                 int(11)        NOT NULL AUTO_INCREMENT,
    `referrer_id`        int(11) COMMENT '推广者ID',
    `buyer_id`           int(11) COMMENT '购买者ID',
    `order_id`           int(11)        NOT NULL COMMENT '订单ID',
    `referrer_name`      varchar(63) COMMENT '推广者名字',
    `buyer_name`         varchar(63) COMMENT '查看者名字',
    `order_actual_price` decimal(10, 2) NOT NULL DEFAULT '0' COMMENT '订单实际价格(元)(如果订单未完成则此列为0))',
    `commission`         decimal(10, 2) NOT NULL DEFAULT '0' COMMENT '佣金(元 order_actual_price * 4% )',
    `state`              int(1)         NOT NULL DEFAULT '-1' COMMENT '发放状态 1 已发放 -1未发放 0发放中',
    `created_time`       datetime                DEFAULT NULL COMMENT '创建时间',
    `update_time`        datetime                DEFAULT NULL COMMENT '更新时间',
    `deleted`            tinyint(1)              DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4 COMMENT ='推广订单览表';

DROP TABLE IF EXISTS `litemall_p2p_rule`;

CREATE TABLE `litemall_p2p_rule`
(
    `id`           int(11)      NOT NULL AUTO_INCREMENT,
    `goods_id`     int(11)      NOT NULL COMMENT '商品表的ID',
    `goods_name`   varchar(256) NOT NULL COMMENT '商品名称',
    `pic_url`      varchar(512) NOT NULL COMMENT '图片URL',
    `goods_desc`   varchar(512) NOT NULL COMMENT '产品描述',
    `expire_time`  datetime   DEFAULT NULL COMMENT '下线时间',
    `created_time` datetime   DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime   DEFAULT NULL COMMENT '更新时间',
    `status`       tinyint(1) DEFAULT '0' COMMENT '是否启用',
    `deleted`      tinyint(1) DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4 COMMENT ='规则表';



DROP TABLE IF EXISTS `litemall_p2p_rule_goods`;

CREATE TABLE `litemall_p2p_rule_goods`
(
    `id`           int(11) NOT NULL AUTO_INCREMENT,
    `rule_id`      int(11) NOT NULL COMMENT '规则ID',
    `product_id`   int(11) NOT NULL COMMENT '商品产品表的ID',
    `price`        decimal(10, 2)   DEFAULT '0.00' COMMENT '最低价格',
    `rule`         int(11) NOT NULL DEFAULT '0' COMMENT '商品规则: 0 退差价 ,1 补数量',
    `count`         int(3) NOT NULL DEFAULT '0' COMMENT '可售总数量',
    `created_time` datetime         DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime         DEFAULT NULL COMMENT '更新时间',
    `deleted`      tinyint(1)       DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4 COMMENT ='规则商品表';



