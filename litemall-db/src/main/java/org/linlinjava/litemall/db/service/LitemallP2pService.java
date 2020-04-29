package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.*;
import org.linlinjava.litemall.db.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.util.*;

import static java.time.temporal.ChronoField.*;

@Service
public class LitemallP2pService {
    @Resource
    private LitemallP2pRuleMapper p2pRuleMapper;
    @Resource
    private LitemallP2pRuleGoodsMapper p2pRuleGoodsMapper;
    @Resource
    private CustomSqlMapper customSqlMapper;

    @Resource
    private LitemallOrderMapper litemallOrderMapper;
    @Resource
    private LitemallGoodsMapper litemallGoodsMapper;
    @Resource
    private LitemallGoodsProductMapper litemallGoodsProductMapper;
    private DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendLiteral('-')
            .appendValue(MONTH_OF_YEAR, 2)
            .appendLiteral('-')
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral(" ")
            .appendValue(HOUR_OF_DAY)
            .appendLiteral(":")
            .appendValue(MINUTE_OF_HOUR)
            .appendLiteral(":")
            .appendValue(SECOND_OF_MINUTE)
            .toFormatter(Locale.CHINA);


    private boolean valid(LitemallP2pRule p2pRules) {
        if (p2pRules == null || !p2pRules.getStatus() || p2pRules.getExpireTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() > LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()) {
            return false;
        }
        return true;
    }

    @Transactional
    public Map<String, Object> createP2PRule(Map<String, Object> data) throws Exception {
        Integer goodsId = (Integer) data.getOrDefault("goodsId", 0);
        boolean enable = (boolean) data.getOrDefault("enable", false);
        String expireTime = (String) data.get("expireTime");
        List<Map<String, Object>> products = (List<Map<String, Object>>) data.get("products");

        if (goodsId != null && !StringUtils.isEmpty(expireTime) && products != null && products.size() > 0) {
            LitemallP2pRuleExample ruleExample = new LitemallP2pRuleExample();
            ruleExample.createCriteria().andDeletedEqualTo(false).andGoodsIdEqualTo(goodsId);
            long ruleCount = p2pRuleMapper.countByExample(ruleExample);
            if (ruleCount > 0) throw new RuntimeException("活动已存在");

            DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                    .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                    .appendLiteral('-')
                    .appendValue(MONTH_OF_YEAR, 2)
                    .appendLiteral('-')
                    .appendValue(DAY_OF_MONTH, 2)
                    .appendLiteral(" ")
                    .appendValue(HOUR_OF_DAY)
                    .appendLiteral(":")
                    .appendValue(MINUTE_OF_HOUR)
                    .appendLiteral(":")
                    .appendValue(SECOND_OF_MINUTE)
                    .toFormatter(Locale.CHINA);
            LocalDateTime expire = LocalDateTime.parse(expireTime, dateTimeFormatter);
            long expir = expire.toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long now = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            if (now >= expir) throw new RuntimeException("下线时间需大于当前时间");
            LitemallGoods goods = litemallGoodsMapper.selectByPrimaryKey(goodsId);
            if (goods == null) throw new RuntimeException("未找到产品");
            LitemallP2pRule p2pRule = new LitemallP2pRule();
            String goodsName = goods.getName();
            String desc = goods.getBrief();
            String picUrl = goods.getPicUrl();
            p2pRule.setCreatedTime(LocalDateTime.now());
            p2pRule.setUpdateTime(LocalDateTime.now());
            p2pRule.setGoodsId(goodsId);
            p2pRule.setGoodsName(goodsName);
            p2pRule.setGoodsDesc(desc);
            p2pRule.setPicUrl(picUrl);
            p2pRule.setStatus(enable);
            p2pRule.setExpireTime(expire);
            p2pRuleMapper.insertSelective(p2pRule);
            Integer rid = p2pRule.getId();
            p2pRule.setId(rid);


            List<LitemallP2pRuleGoods> ruleGoodsList = new ArrayList<LitemallP2pRuleGoods>();
            for (Map<String, Object> product : products) {
                Integer productId = (Integer) product.getOrDefault("id", 0);
                Integer remoteGoodsId = (Integer) product.getOrDefault("goodsId", 0);
                Object price = product.getOrDefault("price", "0.00");
                Integer rule = (Integer) product.getOrDefault("rule", 0);
                LitemallP2pRuleGoodsExample ruleGoodsExample = new LitemallP2pRuleGoodsExample();
                ruleGoodsExample.createCriteria().andRuleIdEqualTo(rid).andProductIdEqualTo(productId);
                LitemallP2pRuleGoods ruleGoods = p2pRuleGoodsMapper.selectOneByExampleSelective(ruleGoodsExample);
                if (goodsId.compareTo(remoteGoodsId) == 0) {
                    ruleGoods = new LitemallP2pRuleGoods();
                    buildRuleGoods(rid, productId, price, rule, ruleGoods);
                    p2pRuleGoodsMapper.insertSelective(ruleGoods);
                    ruleGoodsList.add(ruleGoods);
                }
            }
            if (ruleGoodsList.size() > 0) {
                ruleGoodsList.clear();
                Map<String, Object> info = getRuleInfo(p2pRule);
                return info;
            }


        } else {
            throw new RuntimeException("参数错误");
        }

        return null;
    }


    public Map<String, Object> updateP2PRule(Map<String, Object> data) throws Exception {
        Integer goodsId = (Integer) data.getOrDefault("goodsId", 0);
        boolean enable = (boolean) data.getOrDefault("enable", false);
        String expireTime = (String) data.get("expireTime");
        List<Map<String, Object>> products = (List<Map<String, Object>>) data.get("products");

        if (goodsId != null && !StringUtils.isEmpty(expireTime) && products != null && products.size() > 0) {
            LitemallP2pRuleExample ruleExample = new LitemallP2pRuleExample();
            ruleExample.createCriteria().andDeletedEqualTo(false).andGoodsIdEqualTo(goodsId);
            LitemallP2pRule p2pRule = p2pRuleMapper.selectOneByExample(ruleExample);
            if (p2pRule == null) throw new RuntimeException("活动不存在");


            LocalDateTime expire = LocalDateTime.parse(expireTime, dateTimeFormatter);
            long expir = expire.toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long now = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            if (now >= expir) throw new RuntimeException("下线时间需大于当前时间");
            LitemallGoods goods = litemallGoodsMapper.selectByPrimaryKey(goodsId);
            if (goods == null) throw new RuntimeException("未找到产品");

            p2pRule.setUpdateTime(LocalDateTime.now());
            p2pRule.setGoodsId(goodsId);
            p2pRule.setStatus(enable);
            p2pRule.setExpireTime(expire);
            p2pRuleMapper.updateByPrimaryKeySelective(p2pRule);
            Integer rid = p2pRule.getId();
            p2pRule.setId(rid);


            List<LitemallP2pRuleGoods> ruleGoodsList = new ArrayList<LitemallP2pRuleGoods>();
            for (Map<String, Object> product : products) {
                Integer productId = (Integer) product.getOrDefault("id", 0);
                Object price = product.getOrDefault("price", "0.00");
                Object number = product.getOrDefault("number", "0");
                Integer rule = (Integer) product.getOrDefault("rule", 0);
                boolean enabled = (boolean) product.getOrDefault("enabled", true);
                Integer ruleId = (Integer) product.getOrDefault("ruleId", -1);
                if (ruleId == -1) {
                    // 表示新加的
                    LitemallP2pRuleGoods ruleGoods = new LitemallP2pRuleGoods();
                    buildRuleGoods(rid, productId, price,  rule, ruleGoods);
                    p2pRuleGoodsMapper.insertSelective(ruleGoods);
                    ruleGoodsList.add(ruleGoods);
                } else {
                    LitemallP2pRuleGoodsExample ruleGoodsExample = new LitemallP2pRuleGoodsExample();
                    ruleGoodsExample.createCriteria().andRuleIdEqualTo(rid).andProductIdEqualTo(productId);
                    LitemallP2pRuleGoods ruleGoods = p2pRuleGoodsMapper.selectOneByExampleSelective(ruleGoodsExample);

                    ruleGoods.setPrice(new BigDecimal(price.toString()));
                    ruleGoods.setRule(rule);
                    ruleGoods.setUpdateTime(LocalDateTime.now());
                    p2pRuleGoodsMapper.updateByPrimaryKeySelective(ruleGoods);
                    ruleGoodsList.add(ruleGoods);
                }

                if (!enabled) {
                    LitemallP2pRuleGoodsExample example = new LitemallP2pRuleGoodsExample().createCriteria().andProductIdEqualTo(productId).example();
                    p2pRuleGoodsMapper.deleteByExample(example);
                }
            }
            if (ruleGoodsList.size() > 0) {
                ruleGoodsList.clear();
                Map<String, Object> info = getRuleInfo(p2pRule);
                return info;
            }
        } else {
            throw new RuntimeException("参数错误");
        }
        return null;
    }

    private void buildRuleGoods(Integer rid, Integer productId, Object price,Integer rule, LitemallP2pRuleGoods ruleGoods) {
        ruleGoods.setRuleId(rid);
        ruleGoods.setProductId(productId);
        ruleGoods.setPrice(new BigDecimal(price.toString()));
        ruleGoods.setRule(rule);
        ruleGoods.setCreatedTime(LocalDateTime.now());
        ruleGoods.setUpdateTime(LocalDateTime.now());
    }

    @Transactional
    public List<Integer> deleteP2pRules(List<Integer> ids) {
        if (ids != null && ids.size() > 0) {
            List<Integer> deleteIds = new ArrayList<Integer>();
            for (Integer id : ids) {
                int rid = p2pRuleMapper.deleteByPrimaryKey(id);
                LitemallP2pRuleGoodsExample example = new LitemallP2pRuleGoodsExample().createCriteria().andRuleIdEqualTo(id).example();
                p2pRuleGoodsMapper.deleteByExample(example);
                deleteIds.add(rid);
            }
            return deleteIds;
        }
        return null;
    }


    public List<Map<String, Object>> querySelective(String goodsName, Integer page, Integer limit, String sort, String order) {
        LitemallP2pRuleExample example = new LitemallP2pRuleExample();
        LitemallP2pRuleExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(goodsName)) {
            criteria.andGoodsNameLike("%" + goodsName + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        List<LitemallP2pRule> p2pRules = p2pRuleMapper.selectByExample(example);
        List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
        for (LitemallP2pRule p2pRule : p2pRules) {
            Map<String, Object> info = getRuleInfo(p2pRule);
            res.add(info);
        }
        return res;
    }

    private Map<String, Object> getRuleInfo(LitemallP2pRule p2pRule) {
        Map<String, Object> info = new HashMap<String, Object>();

        LitemallP2pRuleGoodsExample goodsExample = new LitemallP2pRuleGoodsExample();
        goodsExample.createCriteria().andRuleIdEqualTo(p2pRule.getId());
        List<LitemallP2pRuleGoods> ruleGoodsList = p2pRuleGoodsMapper.selectByExampleSelective(goodsExample);

        LitemallGoodsProductExample productExample = new LitemallGoodsProductExample();
        productExample.createCriteria().andGoodsIdEqualTo(p2pRule.getGoodsId());
        List<LitemallGoodsProduct> products = litemallGoodsProductMapper.selectByExample(productExample);

        List<Map<String, Object>> ps = new ArrayList<Map<String, Object>>();
        for (LitemallGoodsProduct gp : products) {
            Integer productId = gp.getId();
            HashMap<String, Object> product = new HashMap<>();
            product.put("id", productId);
            product.put("goodsId", gp.getGoodsId());
            product.put("specifications", gp.getSpecifications());
            product.put("price", gp.getPrice());
            product.put("url", gp.getUrl());
            product.put("number", gp.getNumber());
            product.put("enabled", false);
            product.put("rule", 0);

            for (LitemallP2pRuleGoods rg : ruleGoodsList) {
                if (productId.compareTo(rg.getProductId()) == 0) {
                    product.remove("price");
                    product.remove("number");
                    product.remove("enabled");
                    product.remove("rule");
                    product.put("price", rg.getPrice());
                    product.put("number", gp.getNumber());
                    product.put("enabled", true);
                    product.put("rule", rg.getRule());
                    product.put("ruleId", rg.getRuleId());
                }
            }
            ps.add(product);
        }
        info.put("products", ps);
        info.put("id", p2pRule.getId());
        info.put("goodsId", p2pRule.getGoodsId());
        info.put("unit", litemallGoodsMapper.selectByPrimaryKey(p2pRule.getGoodsId()).getUnit());
        info.put("goodsName", p2pRule.getGoodsName());
        info.put("picUrl", p2pRule.getPicUrl());
        info.put("goodsDesc", p2pRule.getGoodsDesc());
        info.put("expireTime", p2pRule.getExpireTime());
        info.put("createdTime", p2pRule.getCreatedTime());
        info.put("updateTime", p2pRule.getUpdateTime());
        info.put("status", p2pRule.getStatus());
        return info;
    }

    public LitemallP2pRule findById(Integer id) {
        return p2pRuleMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取首页团购规则列表
     *
     * @param page
     * @param limit
     * @return
     */
    public List<LitemallP2pRule> queryList(Integer page, Integer limit) {
        return queryList(page, limit, "created_time", "desc");
    }

    public List<LitemallP2pRule> queryList(Integer page, Integer limit, String sort, String order) {
        LitemallP2pRuleExample example = new LitemallP2pRuleExample();
//        example.or().andStateEqualTo(P2pConstant.RULE_STATE_ONLINE).andDeletedEqualTo(false)
//                .andExpireTimeGreaterThan(LocalDateTime.now());
        example.setOrderByClause(sort + " " + order);
        PageHelper.startPage(page, limit);
        return p2pRuleMapper.selectByExample(example);
    }


    /**
     * 查询某个商品关联的团购规则
     *
     * @param goodsId
     * @return
     */
    public LitemallP2pRule queryByGoodsId(Integer goodsId) {
        LitemallP2pRuleExample example = new LitemallP2pRuleExample();
//        example.or().andProductIdEqualTo(goodsId).andStateEqualTo(P2pConstant.RULE_STATE_ONLINE).andDeletedEqualTo(false);
        return p2pRuleMapper.selectOneByExample(example);
    }

    public Map findProductInfoByGoodsId(Integer goodsId) {
        HashMap<String, Object> map = new HashMap<>();
        LitemallGoods goods = litemallGoodsMapper.selectByPrimaryKey(goodsId);


        if (goods != null) {
            String name = goods.getName();
            LitemallGoodsProductExample goodsExample = new LitemallGoodsProductExample();
            goodsExample.or().andGoodsIdEqualTo(goods.getId());
            LitemallGoodsProduct product = litemallGoodsProductMapper.selectOneByExample(goodsExample);
            map.put("info", product);
            map.put("name", name);
        }

        return map;
    }

}