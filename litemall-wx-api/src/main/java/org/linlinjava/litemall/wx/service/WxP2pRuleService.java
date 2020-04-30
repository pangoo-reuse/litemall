package org.linlinjava.litemall.wx.service;

import com.github.pagehelper.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.dao.CustomSqlMapper;
import org.linlinjava.litemall.db.dao.LitemallOrderMapper;
import org.linlinjava.litemall.db.dao.LitemallP2pRuleGoodsMapper;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallP2pRule;
import org.linlinjava.litemall.db.domain.LitemallP2pRuleGoods;
import org.linlinjava.litemall.db.domain.LitemallP2pRuleGoodsExample;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.db.service.LitemallP2pService;
import org.linlinjava.litemall.wx.vo.P2pRuleVa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class WxP2pRuleService {
    private final Log logger = LogFactory.getLog(WxP2pRuleService.class);

    @Autowired
    private LitemallP2pService p2pRuleService;
    @Resource
    private LitemallP2pRuleGoodsMapper litemallP2pRuleGoodsMapper;
    @Resource
    private LitemallOrderMapper litemallOrderMapper;
    @Resource
    private CustomSqlMapper customSqlMapper;
    @Autowired
    private LitemallGoodsService litemallGoodsService;


    public List<P2pRuleVa> queryList(Integer page, Integer size) {
        return queryList(page, size, "created_time", "desc");
    }


    public List<P2pRuleVa> queryList(Integer page, Integer size, String sort, String order) {
        Page<LitemallP2pRule> grouponRulesList = (Page<LitemallP2pRule>) p2pRuleService.queryList(page, size, sort, order);
//
        Page<P2pRuleVa> grouponList = new Page<P2pRuleVa>();
        grouponList.setPages(grouponRulesList.getPages());
        grouponList.setPageNum(grouponRulesList.getPageNum());
        grouponList.setPageSize(grouponRulesList.getPageSize());
        grouponList.setTotal(grouponRulesList.getTotal());
        for (LitemallP2pRule rule : grouponRulesList) {
            LitemallGoods goods = litemallGoodsService.findById(rule.getGoodsId());
            if (goods != null) {
                LitemallP2pRuleGoodsExample ruleGoodsExample = new LitemallP2pRuleGoodsExample();
                ruleGoodsExample.createCriteria().andRuleIdEqualTo(rule.getId());
                List<LitemallP2pRuleGoods> ruleGoodsList = litemallP2pRuleGoodsMapper.selectByExampleSelective(ruleGoodsExample);
                int saleCount = 10;
                for (LitemallP2pRuleGoods ruleGoods : ruleGoodsList) {
                    Integer productId = ruleGoods.getProductId();
                    int productSaleCount = customSqlMapper.orderP2pCountByProductId(productId);// 当前产品的闪购订单已卖出多少件
                    saleCount += productSaleCount;
                }


                P2pRuleVa va = new P2pRuleVa();
                long currentLong = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
                long time = rule.getExpireTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() - currentLong;
                va.setTime(time);
                va.setId(rule.getId());
                va.setSaleCount(saleCount);

                va.setGoodsId(rule.getGoodsId());
                va.setPrice(goods.getRetailPrice());
                va.setBrief(goods.getBrief());
                va.setGoodsName(rule.getGoodsName());
                va.setPicUrl(rule.getPicUrl());
                va.setStatus(rule.getStatus());
                va.setExpireTime(rule.getExpireTime());
//                int orderCount = customSqlMapper.orderCountByRuleOrderId(rule.getId());
//                //int orderCount, double originPrice, double minPrice, int maxPiece
//                grouponRulesService.getCurrentPrice(orderCount,va.getPrice(),);
                grouponList.add(va);
            }

        }


        return grouponList;
    }
}