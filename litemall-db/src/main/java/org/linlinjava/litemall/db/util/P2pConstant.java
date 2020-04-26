package org.linlinjava.litemall.db.util;

public interface P2pConstant {
    boolean RULE_STATE_ONLINE = true;
    boolean RULE_STATE_OFFLINE = false;
    /**固定人数应搭配按分数发货使用**/
    int RULE_PRODUCT_RULE_COUNT = 0;// 固定份数即为用户总购买的分数不能超过设置的商品数
    int RULE_PRODUCT_RULE_PERSON = 1;// 固定人数即为用户总人数不能超过设定的最大参与人，即每个人得到商品超过一份
    int RULE_SHIPPING_RULE_REFUND = 0;// 退款即为退出购买分摊后多余的金额给用户
    int RULE_SHIPPING_RULE_SEND = 1;// 按每人得到的份数直接发货

    /**ORDER_PROCESS_STATE 恤搭配 RULE_SHIPPING_RULE_REFUND 使用**/
    int ORDER_PROCESS_STATE_REFUND = 1 ;// 已退款
    int ORDER_PROCESS_STATE_DEFAULT = 0 ;// 默认

}
