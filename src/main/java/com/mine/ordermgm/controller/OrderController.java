package com.mine.ordermgm.controller;

import com.mine.ordermgm.api.PlaceOrderRequest;
import com.mine.ordermgm.api.PlaceOrderResponse;
import com.mine.ordermgm.exception.ProductNotFoundException;
import com.mine.ordermgm.model.Order;
import com.mine.ordermgm.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
 * @stefanl
 */
@RestController
@RequestMapping("/orders")
@Api(value="orders", description="Operations related to orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "Place an order", response = PlaceOrderResponse.class)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@Valid @RequestBody PlaceOrderRequest order) {
        PlaceOrderResponse response;
        try {
            response = orderService.createOrder(order);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, null, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Retrieving all orders within a given time period", response = List.class)
    @GetMapping
    public ResponseEntity<List<Order>> getOrders(@NotNull @RequestParam("start") Timestamp start,
                                                 @NotNull @RequestParam("end") Timestamp end) {
        List<Order> orders = orderService.findByRange(start, end);
        return new ResponseEntity<>(orders, null, HttpStatus.OK);
    }

    @ApiOperation(value = "(Re)calculate the total order amount", response = Order.class)
    @PutMapping("/{id}/calculate")
    public ResponseEntity<?> calculateAmount(@PathVariable long id) {
        Order order = orderService.updateOrderPrice(id);
        if (order == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, null, HttpStatus.OK);
    }
}
