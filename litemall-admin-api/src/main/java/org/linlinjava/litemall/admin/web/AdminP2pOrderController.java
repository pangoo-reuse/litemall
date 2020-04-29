package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.domain.LitemallGoodsProduct;
import org.linlinjava.litemall.db.domain.LitemallP2pRule;
import org.linlinjava.litemall.db.service.LitemallGoodsProductService;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.db.service.LitemallP2pService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/p2p-order")
@Validated
public class AdminP2pOrderController {
    private final Log logger = LogFactory.getLog(AdminP2pOrderController.class);

    @Autowired
    private LitemallP2pService p2PGrouponService;
    @Autowired
    private LitemallGoodsService litemallGoodsService;
    @Autowired
    private LitemallGoodsProductService litemallGoodsProductService;



    @RequiresPermissions("admin:p2p:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody Map<String,Object> content) throws Exception {
        Map<String,Object> info = p2PGrouponService.createP2PRule(content);
        return ResponseUtil.ok(info);
    }


    @RequiresPermissions("admin:p2p:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody Map<String,Object> content) throws Exception {
        Map<String,Object> p2pRule = p2PGrouponService.updateP2PRule(content);
        if (p2pRule == null) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok(p2pRule);
    }



    @RequiresPermissions("admin:p2p:orderList")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "删除")
    @PostMapping("/orderList")
    public Object orderList(@RequestBody LitemallP2pRule rules) {
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
