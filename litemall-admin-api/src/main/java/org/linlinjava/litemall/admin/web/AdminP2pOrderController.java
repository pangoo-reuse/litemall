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
@RequestMapping("/admin/p2p")
@Validated
public class AdminP2pOrderController {
    private final Log logger = LogFactory.getLog(AdminP2pOrderController.class);

    @Autowired
    private LitemallP2pService p2PGrouponService;
    @Autowired
    private LitemallGoodsService litemallGoodsService;
    @Autowired
    private LitemallGoodsProductService litemallGoodsProductService;

    @RequiresPermissions("admin:p2p:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String goodsName,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort(accepts = "created_time") @RequestParam(defaultValue = "created_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List< Map<String,Object>> adList = p2PGrouponService.querySelective(goodsName, page, limit, sort, order);
        return ResponseUtil.okList(adList);
    }
    @GetMapping("/queryGoods")
    public Object queryGoods(@NotNull  String keywords) {
        List<LitemallGoods> goodsList = litemallGoodsService.querySelective(null,null,keywords,null,null,0,50,null,null);
        return ResponseUtil.okList(goodsList);
    }
    @GetMapping("/queryGoodsProduct")
    public Object queryGoods(@NotNull  Integer goodsId) {
        List<LitemallGoodsProduct> goodsProducts = litemallGoodsProductService.queryByGid(goodsId);
        return ResponseUtil.okList(goodsProducts);
    }

    @RequiresPermissions("admin:p2p:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody Map<String,Object> content) throws Exception {
        LitemallP2pRule p2PRule = p2PGrouponService.createP2PRule(content);
        return ResponseUtil.ok(p2PRule);
    }

    @RequiresPermissions("admin:p2p:read")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        LitemallP2pRule rules = p2PGrouponService.findById(id);
        return ResponseUtil.ok(rules);
    }
    @RequiresPermissions("admin:p2p:query")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "")
    @GetMapping("/query")
    public Object query(@NotNull Integer productId) {
        Map info = p2PGrouponService.findProductInfoByGoodsId(productId);
        return ResponseUtil.ok(info);
    }

    @RequiresPermissions("admin:p2p:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody Map<String,Object> content) throws Exception {
        LitemallP2pRule p2pRule = p2PGrouponService.updateP2PRule(content);
        if (p2pRule == null) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok(p2pRule);
    }

    @RequiresPermissions("admin:p2p:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "p2p管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody LitemallP2pRule rules) {
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
