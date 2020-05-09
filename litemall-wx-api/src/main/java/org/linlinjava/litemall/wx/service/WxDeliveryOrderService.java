package org.linlinjava.litemall.wx.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.notify.NotifyService;
import org.linlinjava.litemall.core.notify.NotifyType;
import org.linlinjava.litemall.core.util.JacksonUtil;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.dao.LitemallDeliveryMapper;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.service.LitemallOrderGoodsService;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.db.service.LitemallUserService;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service

public class WxDeliveryOrderService {
    private final Log logger = LogFactory.getLog(WxDeliveryOrderService.class);

    @Autowired
    private LitemallOrderGoodsService orderGoodsService;
    @Autowired
    private LitemallOrderService orderService;
    @Autowired
    private LitemallUserService userService;
    @Autowired
    private NotifyService notifyService;
    @Resource
    private LitemallDeliveryMapper deliveryMapper;


    public static final Integer ORDER_CONFIRM_NOT_ALLOWED = 620;

    public Object list(Integer userId, LocalDateTime start, LocalDateTime end,
                       Integer page, Integer limit, String sort, String order) {
        ArrayList<Short> shorts = new ArrayList<>();
        shorts.add(OrderUtil.STATUS_PAY);
        List<LitemallOrder> orderList = orderService.querySelective(userId, null, start, end, shorts, page, limit,
                sort, order);
        return ResponseUtil.okList(orderList);
    }

    public Object detail(Integer id) {
        LitemallOrder order = orderService.findById(id);
        List<LitemallOrderGoods> orderGoods = orderGoodsService.queryByOid(id);
        UserVo user = userService.findUserVoById(order.getUserId());
        Map<String, Object> data = new HashMap<>();
        data.put("order", order);
        data.put("orderGoods", orderGoods);
        data.put("user", user);

        return ResponseUtil.ok(data);
    }


    /**
     * 发货
     * 1. 检测当前订单是否能够发货
     * 2. 设置订单发货状态
     *
     * @param body 订单信息，{ orderId：xxx, shipSn: xxx, shipChannel: xxx }
     * @return 订单操作结果
     * 成功则 { errno: 0, errmsg: '成功' }
     * 失败则 { errno: XXX, errmsg: XXX }
     */
    public Object ship(String body) {
        Integer orderId = JacksonUtil.parseInteger(body, "orderId");
        String shipSn = JacksonUtil.parseString(body, "shipSn");
        String shipChannel = JacksonUtil.parseString(body, "shipChannel");

        //    - code: "TCS"
        //      name: "同城送"
        //同城配送即商家自己配送，运单号系统主动生成
        if ("TCS".equals(shipChannel)) {
            String deliveryId = JacksonUtil.parseString(body, "deliveryId");
            shipSn = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() + "T" + orderId + "D" + deliveryId;
        }
        if (orderId == null || shipSn == null || shipChannel == null) {
            return ResponseUtil.badArgument();
        }

        LitemallOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }

        // 如果订单不是已付款状态，则不能发货
        if (!order.getOrderStatus().equals(OrderUtil.STATUS_PAY)) {
            return ResponseUtil.fail(ORDER_CONFIRM_NOT_ALLOWED, "订单不能确认发货");
        }

        order.setOrderStatus(OrderUtil.STATUS_SHIP);
        order.setShipSn(shipSn);
        order.setShipChannel(shipChannel);
        order.setShipTime(LocalDateTime.now());
        if (orderService.updateWithOptimisticLocker(order) == 0) {
            return ResponseUtil.updatedDateExpired();
        }

        //TODO 发送邮件和短信通知，这里采用异步发送
        // 发货会发送通知短信给用户:          *
        // "您的订单已经发货，快递公司 {1}，快递单 {2} ，请注意查收"
        notifyService.notifySmsTemplate(order.getMobile(), NotifyType.SHIP, new String[]{shipChannel, shipSn});

        return ResponseUtil.ok();
    }

    public Object login(String body) {
        String userName = JacksonUtil.parseString(body, "userName");
        String mobile = JacksonUtil.parseString(body, "mobile");

        LitemallDeliveryExample example = new LitemallDeliveryExample();
        example.createCriteria().andDeliveryNameEqualTo(userName).andMobileEqualTo(mobile).andEnabledEqualTo(true);
        LitemallDelivery delivery = deliveryMapper.selectOneByExampleSelective(example);
        return delivery;
    }
}
