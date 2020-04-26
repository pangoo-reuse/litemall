package org.linlinjava.litemall.wx.service;

import com.github.pagehelper.Page;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallP2pRules;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.db.service.P2PGrouponService;
import org.linlinjava.litemall.wx.vo.P2pRuleVa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class WxP2pGrouponRuleService {
    private final Log logger = LogFactory.getLog(WxP2pGrouponRuleService.class);

    @Autowired
    private P2PGrouponService grouponRulesService;
    @Autowired
    private LitemallGoodsService litemallGoodsService;


    public List<P2pRuleVa> queryList(Integer page, Integer size) {
        return queryList(page, size, "created_time", "desc");
    }


    public List<P2pRuleVa> queryList(Integer page, Integer size, String sort, String order) {
        Page<LitemallP2pRules> grouponRulesList = (Page<LitemallP2pRules>) grouponRulesService.queryList(page, size, sort, order);

        Page<P2pRuleVa> grouponList = new Page<P2pRuleVa>();
        grouponList.setPages(grouponRulesList.getPages());
        grouponList.setPageNum(grouponRulesList.getPageNum());
        grouponList.setPageSize(grouponRulesList.getPageSize());
        grouponList.setTotal(grouponRulesList.getTotal());
        for (LitemallP2pRules rule : grouponRulesList) {
            LitemallGoods goods = litemallGoodsService.findById(rule.getGoodsId());
            if (goods != null){
                P2pRuleVa va = new P2pRuleVa();
                long currentLong = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
                long time = rule.getExpireTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() - currentLong ;
                va.setTime(time);
                va.setId(rule.getId());
                va.setGoodsId(rule.getGoodsId());
                va.setGoodsPrice(goods.getRetailPrice());
                va.setBrief(goods.getBrief());
                va.setMaxPersonCount(rule.getMaxPersonCount());
                va.setGoodsCount(rule.getGoodsCount());
                va.setGoodsName(rule.getGoodsName());
                va.setPicUrl(rule.getPicUrl());
                va.setState(rule.getState());
                va.setProductRule(rule.getProductRule());
                va.setShippingRule(rule.getShippingRule());
                va.setExpireTime(rule.getExpireTime());
                grouponList.add(va);
            }

        }


        return grouponList;
    }
}