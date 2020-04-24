package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.service.ReferrerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 推广服务
 */
@RestController
@RequestMapping("/wx/referrer")
@Validated
public class WxReferrerController {
    private final Log logger = LogFactory.getLog(WxReferrerController.class);

    @Autowired
    private ReferrerService referrerService;

    /**
     * @param referrerCode
     * @param viewerId
     * @param goodsId
     * @return
     */
    @GetMapping("createViewAlliance")
    public Object createViewAlliance(@NotNull String referrerCode, @NotNull Integer viewerId, @NotNull Integer goodsId) {
        try {
            referrerService.createViewAlliance(referrerCode, viewerId, goodsId);
        } catch (Exception e) {
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok();
    }

    @GetMapping("createOrderAlliance")
    public Object createOrderAlliance(@NotNull String referrerCode, @NotNull Integer buyerId, @NotNull Integer orderId) {
        try {
            referrerService.createOrderAlliance(referrerCode, buyerId, orderId);
        } catch (Exception e) {
            return ResponseUtil.fail();
        }
        return ResponseUtil.ok();
    }

    @GetMapping("calculateAlliance")
    public Object calculateAlliance(@NotNull Integer userId) {
        Map<String, BigDecimal> res = referrerService.calculateAlliance(userId);
        return ResponseUtil.ok(res);
    }


}