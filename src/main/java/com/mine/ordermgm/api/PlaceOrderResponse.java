package com.mine.ordermgm.api;

import java.math.BigDecimal;

/**
 * @stefanl
 */
public class PlaceOrderResponse {

    private long orderId;

    private BigDecimal orderAmount;

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
