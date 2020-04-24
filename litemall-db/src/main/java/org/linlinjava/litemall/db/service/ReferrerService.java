package org.linlinjava.litemall.db.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.dao.*;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 推广返佣
 */
@Service
public class ReferrerService {
    private final Log logger = LogFactory.getLog(ReferrerService.class);
    @Resource
    private LitemallReferralOrdersMapper referralOrdersMapper;
    @Resource
    private LitemallReferralViewsMapper referralViewsMapper;

    @Resource
    private LitemallUserMapper userMapper;

    @Resource
    private LitemallGoodsMapper goodsMapper;

    @Resource
    private LitemallOrderMapper litemallOrderMapper;

    @Resource
    private CustomSqlMapper customSqlMapper;

    /**
     * 浏览返佣
     *
     * @param referrerCode
     * @param viewerId
     * @param goodsId
     * @throws Exception
     */
    public void createViewAlliance(String referrerCode, Integer viewerId, Integer goodsId) throws Exception {

        //--     `id`             int(11) NOT NULL AUTO_INCREMENT,
        //--     `referrer_id` int(11) COMMENT '推广者ID',
        //--     `viewer_id` int(11) COMMENT '查看者ID',
        //--     `referrer_name`   varchar(63) COMMENT '推广者名字',
        //--     `viewer_name`   varchar(63) COMMENT '查看者名字',
        //--     `goods_id`     int(11) NOT NULL COMMENT '产品ID',
        //--     `goods_name`   varchar(63) COMMENT '产品名称',
        //--     `view_price`  decimal(10,2) NOT NULL DEFAULT '0' COMMENT '浏览的价格即为产品基础价(元)',
        //--     `commission`    decimal(10,2) NOT NULL DEFAULT '0' COMMENT '佣金(元 view_price * 0.04% )',
        //--     `state`   int(1)  NOT NULL DEFAULT '0' COMMENT '发放状态 1 已发放 0未发放',
        //--     `created_time`   datetime         DEFAULT NULL COMMENT '创建时间',
        //--     `update_time`    datetime         DEFAULT NULL COMMENT '更新时间',
        //--     `deleted`        tinyint(1)       DEFAULT '0' COMMENT '逻辑删除',

        //referrer_id == viewer_id(推广者与浏览者为同一用户不记录) || viewer_id && goods_id exist (同一个浏览者多次浏览同一个商品只记录一次)
        if (StringUtils.isEmpty(referrerCode)) return;
        LitemallGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
        if (goods != null && goods.getIsOnSale() && !goods.getDeleted()) {
            // 产品存在且在售
            byte[] userNameByte = Base64.getDecoder().decode(referrerCode);
            String userName = new String(userNameByte, "UTF-8");
            LitemallUserExample userExample = new LitemallUserExample();
            userExample.or().andUsernameEqualTo(userName);
            LitemallUser referrer = userMapper.selectOneByExample(userExample);
            if (referrer != null && referrer.getStatus() == 0) {
                if (referrer.getId().compareTo(viewerId) == 0) {
                    logger.info("自己分享自己浏览不算");
                    return;// 查看者跟推广者是同一人不记录
                }
                // 推广者存在且可用
                LitemallUser viewer = userMapper.selectByPrimaryKey(viewerId);
                if (viewer != null && viewer.getStatus() == 0) {
                    // 浏览者存在且可用
                    LitemallReferralViewsExample viewsExample = new LitemallReferralViewsExample();
                    viewsExample.or().andViewerIdEqualTo(viewerId).andGoodsIdEqualTo(goodsId);
                    long viewCount = referralViewsMapper.countByExample(viewsExample);
                    if (viewCount == 0) {
                        // 本浏览者没有浏览过本商品
                        LitemallReferralViews referralViews = new LitemallReferralViews();
                        referralViews.setReferrerId(referrer.getId());
                        referralViews.setViewerId(viewer.getId());
                        referralViews.setReferrerName(referrer.getUsername());
                        referralViews.setViewerName(viewer.getUsername());
                        referralViews.setGoodsId(goodsId);
                        referralViews.setGoodsName(goods.getName());
                        BigDecimal viewPrice = goods.getRetailPrice();
                        referralViews.setViewPrice(viewPrice);
                        referralViews.setCommission(viewPrice.multiply(new BigDecimal(0.0004 /*   0.04 %  */)));
                        referralViews.setCreatedTime(LocalDateTime.now());
                        referralViews.setUpdateTime(LocalDateTime.now());
                        referralViewsMapper.insertSelective(referralViews);
                    } else {
                        logger.info("viewer " + viewer.getUsername() + " 浏览过 【" + goods.getName() + "】不及入庫");
                    }
                }
            } else {
                logger.info("用户名为:" + userName + "不合法");
            }
        } else {
            logger.info("商品ID:" + goodsId + "不可用或已下线");
        }
    }

    public void createOrderAlliance(String referrerCode, Integer buyerId, Integer orderId) throws Exception {

        //--     `id`             int(11) NOT NULL AUTO_INCREMENT,
        //--     `referrer_id` int(11) COMMENT '推广者ID',
        //--     `buyer_id` int(11) COMMENT '购买者ID',
        //--     `order_id`     int(11) NOT NULL COMMENT '订单ID',
        //--     `referrer_name`   varchar(63) COMMENT '推广者名字',
        //--     `buyer_name`   varchar(63) COMMENT '查看者名字',
        //--     `order_actual_price`  decimal(10,2) NOT NULL DEFAULT '0' COMMENT '订单实际价格(元)(如果订单未完成则此列为0))',
        //--     `commission`    decimal(10,2) NOT NULL DEFAULT '0' COMMENT '佣金(元 order_actual_price * 4% )',
        //--     `state`   int(1)  NOT NULL DEFAULT '0' COMMENT '发放状态 1 已发放 0未发放',
        //--     `created_time`   datetime         DEFAULT NULL COMMENT '创建时间',
        //--     `update_time`    datetime         DEFAULT NULL COMMENT '更新时间',
        //--     `deleted`        tinyint(1)       DEFAULT '0' COMMENT '逻辑删除',

        //广告联盟（alliance） controller ->
        // createOrderAlliance() ->   referrer_id，buyer_id，order_id，
        // referrer_name，buyer_name，order_actual_price，
        // created_time，update_time写入 alliance orders 表
        if (StringUtils.isEmpty(referrerCode)) return;
        LitemallOrder order = litemallOrderMapper.selectByPrimaryKey(orderId);
        if (order != null && !order.getDeleted()) {
            byte[] userNameByte = Base64.getDecoder().decode(referrerCode);
            String userName = new String(userNameByte, "UTF-8");
            LitemallUserExample userExample = new LitemallUserExample();
            userExample.or().andUsernameEqualTo(userName);
            LitemallUser referrer = userMapper.selectOneByExample(userExample);
            if (referrer != null && referrer.getStatus() == 0) {
                if (referrer.getId().compareTo(buyerId) == 0) {
                    logger.info("自己分享自己购买不算");
                    return;// 查看者跟推广者是同一人不记录
                }
                // 推广者存在且可用
                LitemallUser buyer = userMapper.selectByPrimaryKey(buyerId);
                if (buyer != null && buyer.getStatus() == 0 && buyer.getId().compareTo(buyerId) == 0) {
                    // 购买者者存在且可用
                    LitemallReferralOrdersExample orderExample = new LitemallReferralOrdersExample();
                    orderExample.or().andBuyerIdEqualTo(buyerId).andOrderIdEqualTo(order.getId());
                    long orderCount = referralOrdersMapper.countByExample(orderExample);
                    if (orderCount == 0) {
                        // 本购买者没有购买过本商品
                        LitemallReferralOrders referralOrders = new LitemallReferralOrders();
                        referralOrders.setReferrerId(referrer.getId());
                        referralOrders.setBuyerId(buyer.getId());
                        referralOrders.setReferrerName(referrer.getUsername());
                        referralOrders.setBuyerName(buyer.getUsername());
                        referralOrders.setOrderId(order.getId());
                        BigDecimal actualPrice = order.getActualPrice();
                        referralOrders.setOrderActualPrice(actualPrice);
                        referralOrders.setCommission(BigDecimal.ZERO);// 未完成则佣金为0
                        referralOrders.setCreatedTime(LocalDateTime.now());
                        referralOrders.setUpdateTime(LocalDateTime.now());
                        referralOrdersMapper.insertSelective(referralOrders);
                    } else {
                        logger.info("order  " + order.getOrderSn() + "已经记录过");
                    }
                }
            } else {
                logger.info("用户名为:" + userName + "不合法");
            }

        } else {
            logger.info("訂單ID:" + orderId + "不存在");
        }
    }

    public void updateOrderAlliance(LitemallOrder order) {
        if (order != null &&
                (OrderUtil.STATUS_CONFIRM.compareTo(order.getOrderStatus()) == 0 ||
                        OrderUtil.STATUS_AUTO_CONFIRM.compareTo(order.getOrderStatus()) == 0)
        ) {
            // 订单存在且已完成
            LitemallReferralOrdersExample ordersExample = new LitemallReferralOrdersExample();
            ordersExample.or().andOrderIdEqualTo(order.getId()).andBuyerIdEqualTo(order.getUserId()).andStateEqualTo(0);
            LitemallReferralOrders referralOrders = referralOrdersMapper.selectOneByExampleSelective(ordersExample);
            if (referralOrders != null) {
                referralOrders.setUpdateTime(LocalDateTime.now());
                referralOrders.setCommission(referralOrders.getOrderActualPrice().multiply(new BigDecimal(0.04 /*   4 %  */)));
                referralOrdersMapper.updateByPrimaryKey(referralOrders);
            }
        }

    }

    public Map<String,BigDecimal> calculateAlliance(Integer userId) {
        LitemallUser user = userMapper.selectByPrimaryKey(userId);
        TreeMap<String, BigDecimal> treeMap = new TreeMap<>();
        if (user != null && user.getStatus().compareTo((byte) 0) == 0)
        {
            BigDecimal orderV = customSqlMapper.calculateReferrerOrders(userId);
            BigDecimal total = customSqlMapper.calculateReferrerViews(userId);

            orderV = orderV == null ?BigDecimal.ZERO:orderV;
            total = total == null ? BigDecimal.ZERO:total;
            total = total.add(orderV);

            BigDecimal orderAv = customSqlMapper.calculateReferrerOrdersAvailable(userId);
            BigDecimal available = customSqlMapper.calculateReferrerViewsAvailable(userId);
            orderAv =  orderAv == null ? BigDecimal.ZERO:orderAv;
            available =  available == null ? BigDecimal.ZERO:available;
            available = orderAv.add(available);

            treeMap.put("available",available);
            treeMap.put("total",total);
        }else {
            treeMap.put("available",BigDecimal.ZERO);
            treeMap.put("total",BigDecimal.ZERO);
        }
       return treeMap;
    }

}