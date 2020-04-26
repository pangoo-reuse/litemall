DROP TABLE IF EXISTS `litemall_shipping_config`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
DROP TABLE IF EXISTS `litemall_shipping_config`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_shipping_config`
(
    `id`                  int(11)     NOT NULL AUTO_INCREMENT,
    `express_freight_min` decimal(10, 2) NOT NULL DEFAULT '19.90' COMMENT '满减最低消费19.9',
    `freight_value`       decimal(10, 2) NOT NULL DEFAULT '9.90' COMMENT '不满所需运费9.9',
    `region_id`           int(11)     NOT NULL COMMENT '区域ID',
    `region_code`         int(11)     NOT NULL COMMENT '区域编号',
    `region_address`      varchar(63) NOT NULL DEFAULT '' COMMENT '显示的地址',
    `enabled`             tinyint(1)           DEFAULT '0' COMMENT '是否启动',
    `created_time`        datetime             DEFAULT NULL COMMENT '创建时间',
    `update_time`         datetime             DEFAULT NULL COMMENT '更新时间',
    `deleted`             tinyint(1)           DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `region_code` (`region_code`),
    KEY `enabled` (`enabled`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4 COMMENT ='运输配置表';

ALTER TABLE `litemall_user`
    ADD `referral_code` varchar(64) COMMENT '用户分享码';


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

DROP TABLE IF EXISTS `litemall_p2p_rules`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_p2p_rules`
(
        `id`             int(11) NOT NULL AUTO_INCREMENT,
        `goods_id` int(11) COMMENT '商品ID',
        `max_person_count` int(11) COMMENT '最大限制人数',
        `goods_count` int(11) COMMENT 'p购商品基础总数量',
        `goods_name` varchar(128) COMMENT '商品名',
        `pic_url`     varchar(255) COMMENT 'p2p购买封面',
        `state`   int(1)  NOT NULL DEFAULT '0' COMMENT '状态: 1 线上， 0 下线',
        `product_rule`   int(1)  NOT NULL DEFAULT '0' COMMENT '产品规则： 1 固定总份数 2 固定总人数',
        `shipping_rule`   int(1)  NOT NULL DEFAULT '0' COMMENT '发货规则： 1 退多余款，2 按每人得到的份数发货',
        `expire_time`   datetime         DEFAULT NULL COMMENT '过期时间',
        `created_time`   datetime         DEFAULT NULL COMMENT '创建时间',
        `update_time`    datetime         DEFAULT NULL COMMENT '更新时间',
        `deleted`        tinyint(1)       DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4 COMMENT ='P2P活动规则表';


DROP TABLE IF EXISTS `litemall_p2p_orders`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `litemall_p2p_orders`
(
        `id`             int(11) NOT NULL AUTO_INCREMENT,
        `order_id` int(11) COMMENT '订单ID',
        `shipping_rule_id` int(11) COMMENT 'P购活动ID',
        `user_id` int(11) COMMENT '用户ID',
        `order_price`     decimal(10,2) DEFAULT '0.00' COMMENT '下单价格',
        `current_goods_price`   decimal(10,2) DEFAULT '0.00' COMMENT '当前P购商品价格(活动未结束此价格=order_price)',
        `state`   int(1)  NOT NULL DEFAULT '0' COMMENT '当前状态： 1 已退款 0 未退款 p2p_rules.shipping_rule == 1 时有效  ',
        `created_time`   datetime         DEFAULT NULL COMMENT '创建时间',
        `update_time`    datetime         DEFAULT NULL COMMENT '更新时间',
        `deleted`        tinyint(1)       DEFAULT '0' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `order_id` (`order_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4 COMMENT ='P2P活动订单表';