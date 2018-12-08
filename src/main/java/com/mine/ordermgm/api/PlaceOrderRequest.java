package com.mine.ordermgm.api;

import com.mine.ordermgm.validator.NotEmptyLongFields;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @stefanl
 */
public class PlaceOrderRequest {

    @NotNull
    @Email
    private String email;

    @NotEmptyLongFields
    private List<Long> products;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getProducts() {
        return products;
    }

    public void setProducts(List<Long> products) {
        this.products = products;
    }
}
