package com.mine.ordermgm.api;

import java.math.BigDecimal;

/**
 * @stefanl
 */
public class UpdateProductRequest {

    private BigDecimal price;
    private String name;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
