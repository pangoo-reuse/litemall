package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallDelivery;
import org.linlinjava.litemall.db.domain.LitemallShippingConfig;
import org.linlinjava.litemall.db.service.LitemallDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/admin/delivery")
@Validated
public class AdminDeliveryController {
    private final Log logger = LogFactory.getLog(AdminDeliveryController.class);

    @Autowired
    private LitemallDeliveryService deliveryService;

    /**
     * 查询运费模板
     *
     * @param page
     * @param limit
     * @return
     */
    @RequiresPermissions("admin:delivery:list")
    @RequiresPermissionsDesc(menu = {"配置管理", "配送配置"}, button = "所有送货员")
    @GetMapping("/list")
    public Object list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "") String deliveryName
    ) {
        List<LitemallDelivery> deliveries = deliveryService.queryList(page, limit, deliveryName);
        return ResponseUtil.okList(deliveries);
    }

    /**
     * 创建运费模板
     *
     * @param delivery
     * @return
     */
    @RequiresPermissions("admin:delivery:create")
    @RequiresPermissionsDesc(menu = {"配置管理", "配送配置"}, button = "创建")
    @PostMapping("/create")
    public Object create(@RequestBody LitemallDelivery delivery) {
        boolean exist = deliveryService.existByUserName(delivery.getDeliveryName());
        if (exist) return ResponseUtil.existsValue();
        deliveryService.create(delivery);
        return ResponseUtil.ok(delivery);
    }

    /**
     * 创建运费模板
     *
     * @param delivery
     * @return
     */
    @RequiresPermissions("admin:delivery:update")
    @RequiresPermissionsDesc(menu = {"配置管理", "配送配置"}, button = "更新")
    @PostMapping("/update")
    public Object update(@RequestBody LitemallDelivery delivery) {
        boolean exist = deliveryService.existById(delivery.getId());

        if (!exist) return ResponseUtil.notExistsValue();
        int id = deliveryService.updateById(delivery);
        if (id == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(delivery);
    }

    /**
     * 删除运费模板
     *
     * @param id 模板id
     */
    @RequiresPermissions("admin:delivery:delete")
    @RequiresPermissionsDesc(menu = {"配置管理", "配送配置"}, button = "删除")
    @GetMapping("/delete")
    public Object delete(@RequestParam(defaultValue = "") Integer id) {
        if (StringUtils.isEmpty(id)) return ResponseUtil.badArgument();
        deliveryService.deleteById(id);
        return ResponseUtil.ok();
    }
}
