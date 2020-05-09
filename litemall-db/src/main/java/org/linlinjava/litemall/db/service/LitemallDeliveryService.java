package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallDeliveryMapper;
import org.linlinjava.litemall.db.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class LitemallDeliveryService {

    @Resource
    private LitemallDeliveryMapper litemallDeliveryMapper;

    public List<LitemallDelivery> getAll() {
        LitemallDeliveryExample example = new LitemallDeliveryExample();
        example.or().andLogicalDeleted(false);
        return litemallDeliveryMapper.selectByExample(example);
    }

    /**
     * 根据姓名查询
     *
     * @param userName
     * @return
     */
    public LitemallDelivery queryByUserName(String userName) {
        LitemallDeliveryExample example = new LitemallDeliveryExample();
        LitemallDeliveryExample.Criteria criteria = example.createCriteria();
        criteria.andDeliveryNameEqualTo(userName);
        return litemallDeliveryMapper.selectOneByExample(example);
    }



    public List<LitemallDelivery> queryList(int page, int limit,String userName) {
        LitemallDeliveryExample example = new LitemallDeliveryExample();
        LitemallDeliveryExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(LitemallDelivery.NOT_DELETED);
        example.setOrderByClause(LitemallDelivery.Column.updateTime.desc());
        if (!StringUtils.isEmpty(userName)) {
            criteria.andDeliveryNameLike("%" + userName + "%");
        }
        PageHelper.startPage(page, limit);
        return litemallDeliveryMapper.selectByExample(example);
    }

    public int create(LitemallDelivery delivery) {
        delivery.setCreatedTime(LocalDateTime.now());
        delivery.setUpdateTime(LocalDateTime.now());
       return  litemallDeliveryMapper.insertSelective(delivery);
    }

    public int updateById(LitemallDelivery delivery) {
        delivery.setUpdateTime(LocalDateTime.now());
        return litemallDeliveryMapper.updateByPrimaryKeySelective(delivery);
    }

    public void deleteById(Integer id) {
        litemallDeliveryMapper.logicalDeleteByPrimaryKey(id);
    }

    public boolean existByUserName(String userName) {
        LitemallDeliveryExample example = new LitemallDeliveryExample();
        example.or().andDeliveryNameEqualTo(userName).andDeletedEqualTo(LitemallShippingConfig.NOT_DELETED);
        return litemallDeliveryMapper.countByExample(example) > 0;
    }

    public boolean existById(Integer id) {
        LitemallDeliveryExample example = new LitemallDeliveryExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(LitemallShippingConfig.NOT_DELETED);
        return litemallDeliveryMapper.countByExample(example) > 0;
    }
}

