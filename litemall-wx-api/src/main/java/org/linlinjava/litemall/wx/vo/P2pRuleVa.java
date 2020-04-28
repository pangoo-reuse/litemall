package org.linlinjava.litemall.wx.vo;

import org.linlinjava.litemall.db.domain.LitemallP2pRule;

import java.math.BigDecimal;

public class P2pRuleVa extends LitemallP2pRule {
    private long time;
    private BigDecimal price;
    private String brief;
    private Long saleCount;

    public Long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Long saleCount) {
        this.saleCount = saleCount;
    }

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
