package com.module.mall.model.vo;

import java.util.Date;

/**
 * @Package: com.module.mall.model.vo
 * @Description: 订单统计
 * @author: lishuangqi
 * @date: 2023/12/21 17:03
 */
public class OrderStatisticsVO {

    private Date days;

    private Integer amount;

    public Date getDays() {
        return days;
    }

    public void setDays(Date days) {
        this.days = days;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
