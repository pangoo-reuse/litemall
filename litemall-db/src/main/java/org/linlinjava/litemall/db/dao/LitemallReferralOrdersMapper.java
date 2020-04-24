package org.linlinjava.litemall.db.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallReferralOrders;
import org.linlinjava.litemall.db.domain.LitemallReferralOrdersExample;

public interface LitemallReferralOrdersMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    long countByExample(LitemallReferralOrdersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    int deleteByExample(LitemallReferralOrdersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    int insert(LitemallReferralOrders record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    int insertSelective(LitemallReferralOrders record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    LitemallReferralOrders selectOneByExample(LitemallReferralOrdersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    LitemallReferralOrders selectOneByExampleSelective(@Param("example") LitemallReferralOrdersExample example, @Param("selective") LitemallReferralOrders.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    List<LitemallReferralOrders> selectByExampleSelective(@Param("example") LitemallReferralOrdersExample example, @Param("selective") LitemallReferralOrders.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    List<LitemallReferralOrders> selectByExample(LitemallReferralOrdersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    LitemallReferralOrders selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") LitemallReferralOrders.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    LitemallReferralOrders selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    LitemallReferralOrders selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") LitemallReferralOrders record, @Param("example") LitemallReferralOrdersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") LitemallReferralOrders record, @Param("example") LitemallReferralOrdersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(LitemallReferralOrders record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(LitemallReferralOrders record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    int logicalDeleteByExample(@Param("example") LitemallReferralOrdersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_referral_orders
     *
     * @mbg.generated
     */
    int logicalDeleteByPrimaryKey(Integer id);
}