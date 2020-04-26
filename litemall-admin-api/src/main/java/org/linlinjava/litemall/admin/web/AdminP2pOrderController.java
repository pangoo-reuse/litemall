package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.LitemallP2pRules;
import org.linlinjava.litemall.db.service.P2PGrouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/p2p")
@Validated
public class AdminP2pOrderController {
    private final Log logger = LogFactory.getLog(AdminP2pOrderController.class);

    @Autowired
    private P2PGrouponService p2PGrouponService;

    @RequiresPermissions("admin:p2p:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String goodsName,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort(accepts = "created_time") @RequestParam(defaultValue = "created_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<LitemallP2pRules> adList = p2PGrouponService.querySelective(goodsName, page, limit, sort, order);
        return ResponseUtil.okList(adList);
    }

    @RequiresPermissions("admin:p2p:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody LitemallP2pRules p2pRules) throws Exception {
        LitemallP2pRules p2PRule = p2PGrouponService.createP2PRule(p2pRules);
        return ResponseUtil.ok(p2PRule);
    }

    @RequiresPermissions("admin:p2p:read")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        LitemallP2pRules rules = p2PGrouponService.findById(id);
        return ResponseUtil.ok(rules);
    }

    @RequiresPermissions("admin:p2p:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody LitemallP2pRules rules) throws Exception {
        if (p2PGrouponService.updateP2PRule(rules) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok(rules);
    }

    @RequiresPermissions("admin:p2p:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody LitemallP2pRules rules) {
        Integer id = rules.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        p2PGrouponService.deleteP2pRules(ids);
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:p2p:orderList")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "删除")
    @PostMapping("/orderList")
    public Object orderList(@RequestBody LitemallP2pRules rules) {
        Integer id = rules.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }
        List<Integer> ids = new ArrayList<>();
        ids.add(id);
        p2PGrouponService.deleteP2pRules(ids);
        return ResponseUtil.ok();
    }

}
