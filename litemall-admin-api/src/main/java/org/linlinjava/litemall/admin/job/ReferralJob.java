package org.linlinjava.litemall.admin.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.system.SystemConfig;
import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.domain.LitemallOrderGoods;
import org.linlinjava.litemall.db.domain.LitemallUserReferral;
import org.linlinjava.litemall.db.service.*;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检测分享发放金额
 */
@Component
public class ReferralJob {
    private final Log logger = LogFactory.getLog(ReferralJob.class);

    @Autowired
    private LitemallOrderGoodsService orderGoodsService;
    @Autowired
    private LitemallOrderService orderService;
    @Autowired
    private LitemallGoodsProductService productService;
    @Autowired
    private LitemallReferralService referralService;

    /**
     * 确认发放推广费
     * <p>
     *
     * 定时时间是每天凌晨45点。
     * <p>

     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void checkReferral() {
        logger.info("系统开启定时任务检查推广费用发放");
        referralService.checkReferrals();
//        List<LitemallOrder> orderList = referralService.queryUnconfirm(SystemConfig.getOrderUnconfirm());
//        for (LitemallOrder order : orderList) {
//
//            // 设置订单已取消状态
//            order.setOrderStatus(OrderUtil.STATUS_AUTO_CONFIRM);
//            order.setConfirmTime(LocalDateTime.now());
//            if (orderService.updateWithOptimisticLocker(order) == 0) {
//                logger.info("订单 ID=" + order.getId() + " 数据已经更新，放弃自动确认收货");
//            } else {
//                logger.info("订单 ID=" + order.getId() + " 已经超期自动确认收货");
//            }
//        }

        logger.info("系统任务检查推广费用发放");
    }

}
