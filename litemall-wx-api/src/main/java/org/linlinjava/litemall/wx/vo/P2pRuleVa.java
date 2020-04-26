package org.linlinjava.litemall.wx.vo;

import org.linlinjava.litemall.db.domain.LitemallP2pRules;

import java.math.BigDecimal;

public class P2pRuleVa extends LitemallP2pRules {
    private long time;
    private BigDecimal price;
    private String brief;

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
