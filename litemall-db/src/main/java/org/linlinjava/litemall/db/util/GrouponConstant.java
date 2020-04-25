package org.linlinjava.litemall.db.util;

import org.linlinjava.litemall.db.domain.LitemallOrder;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_UNNECESSARY;
import static java.math.BigDecimal.ROUND_UP;

public class GrouponConstant {
    public static final Short RULE_STATUS_ON = 0;
    public static final Short RULE_STATUS_DOWN_EXPIRE = 1;
    public static final Short RULE_STATUS_DOWN_ADMIN = 2;


    public static final Short STATUS_NONE = 0;
    public static final Short STATUS_ON = 1;
    public static final Short STATUS_SUCCEED = 2;
    public static final Short STATUS_FAIL = 3;

    public static void main(String args[]) {
        c(1, 30.0, 16.0, 300);
    }

    private static void c(int weight, double originPrice, double minPrice, int maxWeight) {
        //价格:30 ,总份数:300

        // 首先每个用户只允许下一单,不然不好计算。后期可允许用户多下几单
        // 300 jin 卖给 x 个人，每个人买(x * y )斤 ，直到单价大于等于最低价。
        // 本来你要拿着30元去进一斤，这个时候原价为16，也就是说你一份你赚14元。
        // 而这个时候你把14元不赚了，平均分配给了300份，所以这个时候价格就低于30.
        // 也就是说用户的30可以买30多元钱的货，也是就是货物不止最初的一份了。肯定比一份多。
        // 你就必须拿出你赚的14元再多进一些货发给用户。这个时候你就赚不到14了。当再有用户购买，你又让出他的那份利润14元。分担到这原本300个订单上，这原本就不止一份的现在又多了一点，
        // 你又要拿出上次的14元剩下的跟这次的14元再去躲进一些分配给这两个用户。依此类推，你每单的利润就会递减。
        // 同时用户的单价就递减，也就是同样的钱可以买更多的东西了

        // 但总体来讲，每单的利润低了，但单数多了你的利润也起来了。同样用户同样的价格买了更多的东西。互利共赢的

        double currentPrice = originPrice - (originPrice- minPrice)/maxWeight *weight;
        System.out.println("购买人数:"+weight);
        System.out.println("当前单价:"+currentPrice);
        System.out.println("每人得到多少份:"+originPrice/currentPrice);
        if (currentPrice - minPrice >0) {
            weight++;
            c(weight, originPrice, minPrice, maxWeight);

        }
    }
}
