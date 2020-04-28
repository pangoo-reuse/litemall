package org.linlinjava.litemall.db.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.linlinjava.litemall.db.domain.LitemallP2pRule;
import org.linlinjava.litemall.db.domain.LitemallP2pRuleGoods;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CustomSqlMapper {
    List<Map> excSql(String sql);

    @Select("SELECT SUM(if(referrer_id = #{userId,jdbcType=INTEGER} ,commission,0.0)) FROM litemall_referral_views GROUP BY referrer_id")
    BigDecimal calculateReferrerViews(@Param("userId") Integer userId);

    @Select("SELECT SUM(if(referrer_id = #{userId,jdbcType=INTEGER} ,commission,0.0)) FROM litemall_referral_orders GROUP BY referrer_id")
    BigDecimal calculateReferrerOrders(@Param("userId") Integer userId);

    @Select("SELECT SUM(if(referrer_id = #{userId,jdbcType=INTEGER} ,commission,0.0)) FROM litemall_referral_views WHERE state = -1 GROUP BY referrer_id")
    BigDecimal calculateReferrerViewsAvailable(@Param("userId") Integer userId);

    @Select("SELECT SUM(if(referrer_id = #{userId,jdbcType=INTEGER} ,commission,0.0)) FROM litemall_referral_orders WHERE state = -1 GROUP BY referrer_id")
    BigDecimal calculateReferrerOrdersAvailable(@Param("userId") Integer userId);

    @Insert("INSERT INTO litemall_p2p_rule_goods (rule_id,product_id,price,number,rule,created_time,update_time) VALUES " +
            "<foreach collection = 'list' item='goods' separator =','>" +
            "(#goods.rule_id,#goods.product_id,#goods.price,#goods.number,#goods.rule,#goods.created_time,#goods.update_time)" +
            "</foreach>")
    Object insertBatchRuleGoods(List<LitemallP2pRuleGoods> goodsList);

}
