package com.mine.ordermgm.service;

import com.mine.ordermgm.api.PlaceOrderRequest;
import com.mine.ordermgm.api.PlaceOrderResponse;
import com.mine.ordermgm.exception.ProductNotFoundException;
import com.mine.ordermgm.model.Order;
import com.mine.ordermgm.model.Product;
import com.mine.ordermgm.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @stefanl
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> findByRange(Timestamp start, Timestamp end) {
        return orderRepository.findByOrderDateBetweenOrderByOrderDateDesc(start,end);
    }

    @Override
    public PlaceOrderResponse createOrder(PlaceOrderRequest request) throws ProductNotFoundException {

        Set<Product> products = productService.findProducts(request.getProducts());

        BigDecimal orderAmount = calculateOrderAmount(products);

        Order order = new Order();
        order.setEmail(request.getEmail());
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        order.setProducts(products);
        order.setOrderAmount(orderAmount);

        order = orderRepository.save(order);

        return createPlaceOrderResponse(order);
    }

    private BigDecimal calculateOrderAmount(Set<Product> products) {
        return products.stream().map(product -> product.getPrice())
                    .reduce(BigDecimal::add).get();
    }

    private PlaceOrderResponse createPlaceOrderResponse(Order order) {
        PlaceOrderResponse response = new PlaceOrderResponse();
        response.setOrderAmount(order.getOrderAmount());
        response.setOrderId(order.getId());
        return response;
    }



    @Override
    public Order updateOrderPrice(long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            return null;
        }

        Order order = orderOptional.get();
        order.setOrderAmount(calculateOrderAmount(order.getProducts()));
        order = orderRepository.save(order);
        return order;
    }
}
