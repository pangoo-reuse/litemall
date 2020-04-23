package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.*;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.util.OrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class LitemallReferralService {

    @Resource
    private LitemallUserReferralMapper userReferralMapper;
    @Resource
    private LitemallUserMapper  litemallUserMapper;
    @Resource
    private LitemallOrderMapper litemallOrderMapper;
    @Resource
    private LitemallOrderGoodsMapper orderGoodsMapper;


    public void createReferral(String referralCode, Integer sawUserId, Integer productId,String productName,  BigDecimal price) {
        if (StringUtils.isEmpty(referralCode)) return;
        LitemallUserExample example = new LitemallUserExample();
        LitemallUserExample.Criteria criteria = example.or();
        String userName = null;
        try {
             userName = new String(Base64.getDecoder().decode(referralCode), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        criteria.andUsernameEqualTo(userName);
        LitemallUser user = litemallUserMapper.selectOneByExampleSelective(example);
        if (user == null || user.getId().compareTo(sawUserId) == 0) return;

        LitemallUserReferralExample referralExample = new LitemallUserReferralExample();
        referralExample.createCriteria().andSawUserIdEqualTo(sawUserId).andProductIdEqualTo(productId);
        long count = userReferralMapper.countByExample(referralExample);
        if (count <=0){
            // 如果同一用户多次浏览同一个产品只记录一次
            //    `id`             int(11) NOT NULL AUTO_INCREMENT,
            //    `shared_user_id` int(11) COMMENT '分享者',
            //    `target_user_id` int(11) COMMENT '查看者',
            //    `product_id`     int(11) NOT NULL COMMENT '产品ID',
            //    `product_name`   varchar(63) COMMENT '产品名称',
            //    `product_price`  int(11) NOT NULL DEFAULT '0' COMMENT '浏览或成交时的价格(分)',
            //    `grant_price`    int(11) NOT NULL DEFAULT '0' COMMENT '发放金额(分)',
            //    `final_status`   int(1)  NOT NULL DEFAULT '0' COMMENT '最终状态 1 成交 0 浏览',
            //    `grant_status`   int(1)  NOT NULL DEFAULT '0' COMMENT '发放状态 1 已发放 0未发放',
            //    `created_time`   datetime         DEFAULT NULL COMMENT '创建时间',
            //    `update_time`    datetime         DEFAULT NULL COMMENT '更新时间',
            //    `deleted`        tinyint(1)       DEFAULT '0' COMMENT '逻辑删除',
            example.createCriteria().andIdEqualTo(sawUserId);
            LitemallUser sawer = litemallUserMapper.selectOneByExample(example);
            LitemallUserReferral userReferral = new LitemallUserReferral();
            userReferral.setSharedUserId(user.getId());
            userReferral.setSawUserId(sawUserId);
            userReferral.setProductId(productId);
            userReferral.setProductName(productName);
            userReferral.setSharedUserName(user.getUsername());
            userReferral.setSawUserName(sawer.getUsername());
            userReferral.setProductPrice(price);
            userReferral.setCreatedTime(LocalDateTime.now());
            userReferral.setUpdateTime(LocalDateTime.now());
            userReferralMapper.insertSelective(userReferral);
        }
    }

    public void checkReferrals() {
        LitemallUserReferralExample referralExample = new LitemallUserReferralExample();
        referralExample.createCriteria()
                .andGrantStatusEqualTo(0);

        List<LitemallUserReferral> referrals = userReferralMapper.selectByExampleSelective(referralExample);
        for (LitemallUserReferral referral: referrals ) {
            Integer finalStatus = referral.getFinalStatus();
            if (0 == finalStatus){
                // 达成浏览
                Integer sawUserId = referral.getSawUserId();
                Integer rpid = referral.getProductId();
                LitemallOrderExample orderExample = new LitemallOrderExample();
                List<Short> status = new ArrayList<Short>();
                status.add(OrderUtil.STATUS_CONFIRM);
                status.add(OrderUtil.STATUS_AUTO_CONFIRM);
                orderExample.createCriteria().andOrderStatusIn(status)
                .andUserIdEqualTo(sawUserId);
                List<LitemallOrder> orders = litemallOrderMapper.selectByExample(orderExample);
                List<Integer> ids = new ArrayList<Integer>();
                for (LitemallOrder order : orders) {
                    Integer id = order.getId();
                    if (id != null){
                        ids.add(id);
                    }
                }
                LitemallOrderGoodsExample orderGoodsExample = new LitemallOrderGoodsExample();
                orderGoodsExample.or()
                        .andOrderIdIn(ids);
                List<LitemallOrderGoods> orderGoods = orderGoodsMapper.selectByExample(orderGoodsExample);
                if (orderGoods != null && orderGoods.size() > 0){
                    LinkedHashMap<String, BigDecimal> moneyPool = new LinkedHashMap<>();
                    for (LitemallOrderGoods ogs : orderGoods) {
                        Integer productId = ogs.getProductId();
                        if (rpid.compareTo(productId) ==0){
                            // 浏览并购买了
                            Short number = ogs.getNumber();
                            BigDecimal price = ogs.getPrice();
                            BigDecimal mce = moneyPool.get(productId);
                            BigDecimal totalPrice = new BigDecimal(number).multiply(price);
                            if (mce == null){
                                moneyPool.put(String.valueOf(productId),totalPrice);
                            }else {
                                BigDecimal add = mce.add(totalPrice);
                                moneyPool.put(String.valueOf(productId),add);
                            }
                        }

                    }
                    // 比例为 4%
                    for (Map.Entry<String, BigDecimal> entry : moneyPool.entrySet()) {
                        String pid = entry.getKey();
                        BigDecimal mon = entry.getValue();
                        if (pid.equals(String.valueOf(rpid))) {
                            BigDecimal multiply = mon.multiply(BigDecimal.valueOf(0.04));
                            referral.setFinalStatus(1);
                            referral.setProductPrice(mon);
                            referral.setGrantPrice(multiply);
                            referral.setUpdateTime(LocalDateTime.now());
                            userReferralMapper.updateByPrimaryKey(referral);
                        }

                    }

                }else {
                    // 未发现已完成订单
                }

            }else if (1 == finalStatus){

            }
        }
    }
}

