package com.mine.ordermgm.service;

import com.mine.ordermgm.api.PlaceOrderRequest;
import com.mine.ordermgm.api.PlaceOrderResponse;
import com.mine.ordermgm.api.UpdateProductRequest;
import com.mine.ordermgm.exception.ProductNotFoundException;
import com.mine.ordermgm.model.Order;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @stefanl
 */
public interface OrderService {

    /**
     * Retrieve the orders
     *
     * @return
     */
    List<Order> findByRange(Timestamp start, Timestamp end);

    /**
     * Create order
     *
     * @param order
     * @return
     */
    PlaceOrderResponse createOrder(PlaceOrderRequest order) throws ProductNotFoundException;

    /**
     * update order price
     *
     * @param id
     * @return
     */
    Order updateOrderPrice(long id);
}
