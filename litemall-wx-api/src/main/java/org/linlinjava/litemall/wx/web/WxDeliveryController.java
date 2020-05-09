package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.wx.annotation.IsLogin;
import org.linlinjava.litemall.wx.service.WxDeliveryOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 用户服务
 */
@RestController
@RequestMapping("/wx/delivery")
@Validated
public class WxDeliveryController {
    private final Log logger = LogFactory.getLog(WxDeliveryController.class);

    @Autowired
    private WxDeliveryOrderService deliveryOrderService;

    /**
     * 获取配送员名下所有订单
     *
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("index")
    public Object list(@IsLogin Integer userId, LocalDateTime start, LocalDateTime end,
                       Integer page, Integer limit, String sort, String order) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        return ResponseUtil.ok(deliveryOrderService.list(userId, start, end,  page, limit,
                sort, order));
    }

    /**
     * @param
     * @return
     */
    @PostMapping("send")
    public Object send(@RequestBody String body) {
        Object ship = deliveryOrderService.ship(body);
        return ResponseUtil.ok(ship);
    }
    /**
     * @param
     * @return
     */
    @PostMapping("getToken")
    public Object getToken(@RequestBody String body) {
        Object delivery = deliveryOrderService.login(body);
        if (delivery == null) ResponseUtil.fail(45155,"未找到账号或已停用");
        return ResponseUtil.ok(delivery);
    }

}