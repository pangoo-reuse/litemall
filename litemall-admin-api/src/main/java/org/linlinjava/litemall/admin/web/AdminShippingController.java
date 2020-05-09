package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallShippingConfig;
import org.linlinjava.litemall.db.service.LitemallShippingConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/admin/shipping")
@Validated
public class AdminShippingController {
    private final Log logger = LogFactory.getLog(AdminShippingController.class);

    @Autowired
    private LitemallShippingConfigService shippingConfigService;

    private Object validate(LitemallShippingConfig shippingConfig) {
        BigDecimal expressFreightMin = shippingConfig.getExpressFreightMin();
        BigDecimal freightValue = shippingConfig.getFreightValue();
        Integer regionId = shippingConfig.getRegionId();
        if ((expressFreightMin == null || expressFreightMin.intValue() < 0)
                && (freightValue == null || freightValue.intValue() < 0)
                && (regionId == null || regionId < 0)
        ) {
            return ResponseUtil.badArgument();
        }
        return null;
    }
    /**
     * 查询运费模板
     *
     * @param page
     * @param limit
     * @return
     */
    @RequiresPermissions("admin:shipping:list")
    @RequiresPermissionsDesc(menu = {"配置管理", "运费管理"}, button = "所有模板")
    @GetMapping("/list")
    public Object list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "") String regionAddress
            ) {
        List<LitemallShippingConfig> configs = shippingConfigService.queryList(page, limit,regionAddress);
        return ResponseUtil.okList(configs);
    }
    /**
     * 搜索运费模板
     *
     * @param regionName
     * @return
     */
    @RequiresPermissions("admin:shipping:search")
    @RequiresPermissionsDesc(menu = {"配置管理", "运费管理"}, button = "查询")
    @GetMapping("/search")
    public Object search(
            @RequestParam(defaultValue = "") String regionName) {
        List<LitemallShippingConfig> configs = shippingConfigService.queryListByRegionName(regionName);
        return ResponseUtil.okList(configs);
    }
    /**
     * 创建运费模板
     * @param shippingConfig
     * @return
     */
    @RequiresPermissions("admin:shipping:create")
    @RequiresPermissionsDesc(menu = {"配置管理", "运费管理"}, button = "创建")
    @PostMapping("/create")
    public Object create(@RequestBody LitemallShippingConfig shippingConfig) {
        Object error = validate(shippingConfig);
        if (error != null) {
            return error;
        }
        boolean exist = shippingConfigService.existByRegionId(shippingConfig.getRegionId());
        if (exist)  return ResponseUtil.existsValue();
        shippingConfigService.create(shippingConfig);
        return ResponseUtil.ok(shippingConfig);
    }
    /**
     * 创建运费模板
     * @param shippingConfig
     * @return
     */
    @RequiresPermissions("admin:shipping:update")
    @RequiresPermissionsDesc(menu = {"配置管理", "运费管理"}, button = "更新")
    @PostMapping("/update")
    public Object update(@RequestBody LitemallShippingConfig shippingConfig) {
        Object error = validate(shippingConfig);
        if (error != null) {
            return error;
        }
        boolean exist = shippingConfigService.existById(shippingConfig.getId());
        if (!exist)  return ResponseUtil.notExistsValue();
        int id = shippingConfigService.updateById(shippingConfig);
        if (id == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(shippingConfig);
    }
    /**
     * 删除运费模板
     * @param id 模板id
     */
    @RequiresPermissions("admin:shipping:delete")
    @RequiresPermissionsDesc(menu = {"配置管理", "运费管理"}, button = "删除")
    @GetMapping("/delete")
    public Object delete(@RequestParam(defaultValue = "") Integer id) {
        if (StringUtils.isEmpty(id)) return  ResponseUtil.badArgument();
        shippingConfigService.deleteById(id);
        return ResponseUtil.ok();
    }
}
