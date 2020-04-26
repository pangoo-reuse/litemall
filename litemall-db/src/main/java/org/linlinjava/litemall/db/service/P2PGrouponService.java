package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallGoodsMapper;
import org.linlinjava.litemall.db.dao.LitemallOrderMapper;
import org.linlinjava.litemall.db.dao.LitemallP2pOrdersMapper;
import org.linlinjava.litemall.db.dao.LitemallP2pRulesMapper;
import org.linlinjava.litemall.db.domain.*;
import org.linlinjava.litemall.db.util.GrouponConstant;
import org.linlinjava.litemall.db.util.P2pConstant;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class P2PGrouponService {
    @Resource
    private LitemallP2pRulesMapper p2pRulesMapper;

    @Resource
    private LitemallP2pOrdersMapper p2pOrdersMapper;
    @Resource
    private LitemallOrderMapper litemallOrderMapper;
    @Resource
    private LitemallGoodsMapper litemallGoodsMapper;

    public static void main(String args[]) {
        c(1, 30.0, 16.0, 3000);
    }

    private static void c(int personCount, double originPrice, double minPrice, int maxWeight) {
        //价格:30 ,总份数:300

        // 首先每个用户只允许下一单,不然不好计算。后期可允许用户多下几单
        // 300 jin 卖给 x 个人，每个人买(x * y )斤 ，直到单价大于等于最低价。
        // 本来你要拿着30元去进一斤，这个时候原价为16，也就是说你一份你赚14元。
        // 而这个时候你把14元不赚了，平均分配给了300份，所以这个时候价格就低于30.
        // 也就是说用户的30可以买30多元钱的货，也是就是货物不止最初的一份了。肯定比一份多。
        // 你就必须拿出你赚的14元再多进一些货发给用户。这个时候你就赚不到14了。当再有用户购买，你又让出他的那份利润14元。分担到这原本300个订单上，这原本就不止一份的现在又多了一点，
        // 你又要拿出上次的14元剩下的跟这次的14元再去躲进一些分配给这两个用户。依此类推，你每单的利润就会递减。
        // 同时用户的单价就递减，也就是同样的钱可以买更多的东西了

        // 但总体来讲，每单的利润低了，但单数多了你的利润也起来了。同样用户同样的价格买了更多的东西。互利共赢的

        double currentPrice = originPrice - (originPrice - minPrice) / maxWeight * personCount;
        System.out.println("购买人数:" + personCount);
        System.out.println("当前单价:" + currentPrice);
        System.out.println("每人得到多少份:" + originPrice / currentPrice);
        if (currentPrice - minPrice > 0 && maxWeight > 0) {
            personCount++;
            maxWeight--;
            c(personCount, originPrice, minPrice, maxWeight);

        }
    }

    private boolean valid(LitemallP2pRules p2pRules) {
        if (p2pRules == null || p2pRules.getMaxPersonCount() == 0 || p2pRules.getGoodsCount() == 0 || p2pRules.getGoodsId() == 0) {
            return false;
        }
        return true;
    }

    public LitemallP2pRules createP2PRule(LitemallP2pRules p2pRules) throws Exception {
        if (valid(p2pRules)) {
            Integer goodsId = p2pRules.getGoodsId();
            LitemallGoods goods = litemallGoodsMapper.selectByPrimaryKey(goodsId);
            LitemallP2pRulesExample rulesExample = new LitemallP2pRulesExample();
            rulesExample.createCriteria().andGoodsIdEqualTo(goodsId).andStateEqualTo(true);
            long onlineRuleCount = p2pRulesMapper.countByExample(rulesExample);
            if (goods == null) throw new RuntimeException("未找到改商品");
            if (onlineRuleCount == 0) {
                p2pRules.setPicUrl(goods.getPicUrl());
                p2pRules.setGoodsName(goods.getName());
                p2pRules.setCreatedTime(LocalDateTime.now());
                p2pRules.setUpdateTime(LocalDateTime.now());
                int id = p2pRulesMapper.insertSelective(p2pRules);
                p2pRules.setId(id);
                return p2pRules;
            }else {
                throw new RuntimeException("活动已存在");
            }
        }
        return null;
    }

    public Integer updateP2PRule(LitemallP2pRules p2pRules) throws Exception {
        if (valid(p2pRules) && p2pRules.getId() != null) {
            int i = p2pRulesMapper.updateByPrimaryKey(p2pRules);
            return i;
        }
        return null;
    }

    public List<Integer> deleteP2pRules(List<Integer> ids) {
        if (ids != null && ids.size() > 0) {
            List<Integer> deleteIds = new ArrayList<Integer>();
            for (Integer id : ids) {
                int rid = p2pRulesMapper.deleteByPrimaryKey(id);
                deleteIds.add(rid);
            }
            return deleteIds;
        }
        return null;
    }

    public LitemallP2pOrders createP2pOrder(Integer orderId, Integer ruleId) {
        LitemallOrder litemallOrder = litemallOrderMapper.selectByPrimaryKey(orderId);
        if (litemallOrder != null) {
            LitemallP2pOrders p2pOrders = new LitemallP2pOrders();
            p2pOrders.setOrderId(litemallOrder.getId());
            p2pOrders.setShippingRuleId(ruleId);
            p2pOrders.setUserId(litemallOrder.getUserId());
            p2pOrders.setOrderPrice(litemallOrder.getActualPrice());
            p2pOrders.setCurrentGoodsPrice(litemallOrder.getActualPrice());
            p2pOrders.setCreatedTime(LocalDateTime.now());
            p2pOrders.setUpdateTime(LocalDateTime.now());
            int id = p2pOrdersMapper.insertSelective(p2pOrders);
            p2pOrders.setId(id);
            return p2pOrders;
        }
        return null;
    }

    public int update(LitemallP2pOrders litemallP2pOrders) {
        return p2pOrdersMapper.updateByPrimaryKey(litemallP2pOrders);
    }

    public List<LitemallP2pRules> querySelective(String goodsName,  Integer page, Integer limit, String sort, String order) {
        LitemallP2pRulesExample example = new LitemallP2pRulesExample();
        LitemallP2pRulesExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(goodsName)) {
            criteria.andGoodsNameLike("%" + goodsName + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return p2pRulesMapper.selectByExample(example);
    }

    public LitemallP2pRules findById(Integer id) {
        return p2pRulesMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取首页团购规则列表
     *
     * @param page
     * @param limit
     * @return
     */
    public List<LitemallP2pRules> queryList(Integer page, Integer limit) {
        return queryList(page, limit, "created_time", "desc");
    }

    public List<LitemallP2pRules> queryList(Integer page, Integer limit, String sort, String order) {
        LitemallP2pRulesExample example = new LitemallP2pRulesExample();
        example.or().andStateEqualTo(P2pConstant.RULE_STATE_ONLINE).andDeletedEqualTo(false)
        .andExpireTimeGreaterThan(LocalDateTime.now());
        example.setOrderByClause(sort + " " + order);
        PageHelper.startPage(page, limit);
        return p2pRulesMapper.selectByExample(example);
    }
}