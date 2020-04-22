package org.linlinjava.litemall.admin.web;

import com.google.common.collect.Maps;
import jodd.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.vo.RegionVo;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallRegion;
import org.linlinjava.litemall.db.service.LitemallRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/region")
@Validated
public class AdminRegionController {
    private final Log logger = LogFactory.getLog(AdminRegionController.class);

    @Autowired
    private LitemallRegionService regionService;

    @GetMapping("/clist")
    public Object clist(@NotNull Integer id) {
        List<LitemallRegion> regionList = regionService.queryByPid(id);
        return ResponseUtil.okList(regionList);
    }

    @GetMapping("/list")
    public Object list() {
        List<RegionVo> regionVoList = new ArrayList<>();

        List<LitemallRegion> litemallRegions = regionService.getAll();
        Map<Byte, List<LitemallRegion>> collect = litemallRegions.stream().collect(Collectors.groupingBy(LitemallRegion::getType));
        byte provinceType = 1;
        List<LitemallRegion> provinceList = collect.get(provinceType);
        byte cityType = 2;
        List<LitemallRegion> city = collect.get(cityType);
        Map<Integer, List<LitemallRegion>> cityListMap = city.stream().collect(Collectors.groupingBy(LitemallRegion::getPid));
        byte areaType = 3;
        List<LitemallRegion> areas = collect.get(areaType);
        Map<Integer, List<LitemallRegion>> areaListMap = areas.stream().collect(Collectors.groupingBy(LitemallRegion::getPid));

        for (LitemallRegion province : provinceList) {
            RegionVo provinceVO = new RegionVo();
            provinceVO.setId(province.getId());
            provinceVO.setName(province.getName());
            provinceVO.setCode(province.getCode());
            provinceVO.setType(province.getType());

            List<LitemallRegion> cityList = cityListMap.get(province.getId());
            List<RegionVo> cityVOList = new ArrayList<>();
            for (LitemallRegion cityVo : cityList) {
                RegionVo cityVO = new RegionVo();
                cityVO.setId(cityVo.getId());
                cityVO.setName(cityVo.getName());
                cityVO.setCode(cityVo.getCode());
                cityVO.setType(cityVo.getType());

                List<LitemallRegion> areaList = areaListMap.get(cityVo.getId());
                List<RegionVo> areaVOList = new ArrayList<>();
                for (LitemallRegion area : areaList) {
                    RegionVo areaVO = new RegionVo();
                    areaVO.setId(area.getId());
                    areaVO.setName(area.getName());
                    areaVO.setCode(area.getCode());
                    areaVO.setType(area.getType());
                    areaVOList.add(areaVO);
                }

                cityVO.setChildren(areaVOList);
                cityVOList.add(cityVO);
            }
            provinceVO.setChildren(cityVOList);
            regionVoList.add(provinceVO);
        }

        return ResponseUtil.okList(regionVoList);
    }

    @GetMapping("/region")
    public Object queryRegion(@NotNull String name) {
        List<LitemallRegion> regionList = regionService.querySelective(name, null, 1, 80, null, null);
        List<Map<String,Object>> result = new LinkedList<Map<String,Object>>();
        if (regionList != null && regionList.size() > 0) {
            regionList.forEach(reg -> {
                List<LitemallRegion> address = new LinkedList<LitemallRegion>();
                String region = recycleFindRegion(reg, address);
                System.out.println("address:" + region+reg.getName());
                Map<String,Object> res =  Maps.newTreeMap();
                res.put("id",reg.getId());
                res.put("code",reg.getCode());
                res.put("name",reg.getName());
                res.put("address",region+reg.getName());
                result.add(res);
            });
        }
        return ResponseUtil.okList(result);
    }

    private String recycleFindRegion(LitemallRegion region,   List<LitemallRegion> address) {
        int pid = region.getPid();
        if (pid != 0) {
            LitemallRegion reg = regionService.findById(pid);
            address.add(reg);
            return recycleFindRegion(reg, address);
        } else {
            address.sort(Comparator.comparingInt(LitemallRegion::getPid));
            StringBuffer sb = new StringBuffer();
            for (LitemallRegion lr : address) {
                sb.append(lr.getName());
            }
            return sb.toString();
        }
    }
}
