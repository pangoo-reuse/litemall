package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallShippingConfigMapper;
import org.linlinjava.litemall.db.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
public class LitemallShippingConfigService {

    @Resource
    private LitemallShippingConfigMapper shippingConfigMapper;
    @Resource
    private LitemallRegionService litemallRegionService;

    public List<LitemallShippingConfig> getAll() {
        LitemallShippingConfigExample example = new LitemallShippingConfigExample();
        example.or().andLogicalDeleted(false);
        return shippingConfigMapper.selectByExample(example);
    }

    /**
     * 根据区域id查询
     *
     * @param regionId
     * @return
     */
    public LitemallShippingConfig queryByRid(Integer regionId) {
        LitemallShippingConfigExample example = new LitemallShippingConfigExample();
        LitemallShippingConfigExample.Criteria criteria = example.createCriteria();
        criteria.andRegionIdEqualTo(regionId);
        return shippingConfigMapper.selectOneByExample(example);
    }


    public List<LitemallShippingConfig> queryListByRegionName(String regionName) {
        LitemallShippingConfigExample example = new LitemallShippingConfigExample();
        LitemallShippingConfigExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(regionName)) {
            List<LitemallRegion> regionList = litemallRegionService.querySelective(regionName, null, 1, 80, null, null);
            if (regionList.size() > 0) {
                List<Integer> rids = new LinkedList<Integer>();
                for (LitemallRegion rg : regionList) {
                    Integer id = rg.getId();
                    rids.add(id);
                }
                criteria.andRegionIdIn(rids);
            }

        }
        return shippingConfigMapper.selectByExample(example);
    }

    public List<LitemallShippingConfig> queryList(int page, int limit,String regionAddress) {
        LitemallShippingConfigExample example = new LitemallShippingConfigExample();
        LitemallShippingConfigExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(LitemallShippingConfig.NOT_DELETED);
        example.setOrderByClause(LitemallShippingConfig.Column.updateTime.desc());
        if (!StringUtils.isEmpty(regionAddress)) {
            criteria.andRegionAddressLike("%" + regionAddress + "%");
        }
        PageHelper.startPage(page, limit);
        return shippingConfigMapper.selectByExample(example);
    }

    public int create(LitemallShippingConfig shippingConfig) {
        shippingConfig.setCreatedTime(LocalDateTime.now());
        shippingConfig.setUpdateTime(LocalDateTime.now());
       return  shippingConfigMapper.insertSelective(shippingConfig);
    }

    public int updateById(LitemallShippingConfig shippingConfig) {
        shippingConfig.setUpdateTime(LocalDateTime.now());
        return shippingConfigMapper.updateByPrimaryKeySelective(shippingConfig);
    }

    public void deleteById(Integer id) {
        shippingConfigMapper.logicalDeleteByPrimaryKey(id);
    }

    public boolean existByRegionId(Integer regionId) {
        LitemallShippingConfigExample example = new LitemallShippingConfigExample();
        example.or().andRegionIdEqualTo(regionId).andDeletedEqualTo(LitemallShippingConfig.NOT_DELETED);
        return shippingConfigMapper.countByExample(example) > 0;
    }

    public LitemallShippingConfig findByAreaCode(String areaCode) {
        LitemallShippingConfigExample example = new LitemallShippingConfigExample();
        LitemallShippingConfig config = findShippingConfigRecycle(example, Integer.parseInt(areaCode));
        return config;
    }

    private LitemallShippingConfig findShippingConfigRecycle(LitemallShippingConfigExample example, int areaCode) {
        LitemallRegion region = litemallRegionService.queryByCode(areaCode);
        if (region != null) {
            example.or().andRegionCodeEqualTo(areaCode).andDeletedEqualTo(LitemallShippingConfig.NOT_DELETED);
            LitemallShippingConfig shippingConfig = shippingConfigMapper.selectOneByExample(example);
            if (shippingConfig == null && region.getPid() != 0) {
                return findShippingConfigRecycle(example, litemallRegionService.findById(region.getPid()).getCode());
            } else return shippingConfig;
        }
        return null;
    }
}

