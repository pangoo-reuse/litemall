package org.linlinjava.litemall.db.domain;

import org.springframework.beans.BeanUtils;

public class LitemallGoodsVo extends LitemallGoods {
    private boolean isP2p;

    public boolean isP2p() {
        return isP2p;
    }

    public void setP2p(boolean p2p) {
        isP2p = p2p;
    }

    public static LitemallGoodsVo build(LitemallGoods goods, boolean isP2p) {
        LitemallGoodsVo goodsVo = new LitemallGoodsVo();
        BeanUtils.copyProperties(goods, goodsVo);
        goodsVo.setP2p(isP2p);
        return goodsVo;
    }
}
