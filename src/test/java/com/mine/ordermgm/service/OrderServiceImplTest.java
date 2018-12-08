package com.mine.ordermgm.service;

import com.mine.ordermgm.api.PlaceOrderRequest;
import com.mine.ordermgm.api.PlaceOrderResponse;
import com.mine.ordermgm.api.UpdateProductRequest;
import com.mine.ordermgm.exception.ProductNotFoundException;
import com.mine.ordermgm.model.Order;
import com.mine.ordermgm.model.Product;
import com.mine.ordermgm.repository.OrderRepository;
import com.mine.ordermgm.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.mine.ordermgm.service.ProductServiceImplTest.newProduct;
import static com.mine.ordermgm.service.ProductServiceImplTest.newUpdateProductRequest;
import static org.junit.Assert.*;

/**
 * @stefanl
 */
@RunWith(SpringRunner.class)
public class OrderServiceImplTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductRepository productRepository;

    static Order newOrder(long id, String email, Timestamp orderDate, Set<Product> products, BigDecimal orderAmount) {
        Order order = new Order();
        order.setId(id);
        order.setEmail(email);
        order.setOrderDate(orderDate);
        order.setProducts(products);
        order.setOrderAmount(orderAmount);
        return order;
    }

    @Before
    public void setUp() {
        LocalDateTime orderDate = LocalDateTime.of(2018, 12, 2, 10, 10);
        Product productOne = newProduct(1, "Test product", new BigDecimal(200.00));
        Product productTwo = newProduct(2, "Test product two", new BigDecimal(300.00));
        Product productThree = newProduct(3, "Test product three", new BigDecimal(500.00));

        Product productUpdated = newProduct(3, "Test product three", new BigDecimal(800.00));

        Set<Product> products = new HashSet<>(Arrays.asList(productOne, productTwo));
        Order orderOne = newOrder(1,"aaa@aaa.com", Timestamp.valueOf(orderDate), products, new BigDecimal(500.00));

        orderDate = LocalDateTime.of(2018, 12, 6, 11, 11);
        products = new HashSet<>(Arrays.asList(productTwo));
        Order orderTwo = newOrder(2, "aaa@aaa.com", Timestamp.valueOf(orderDate), products, new BigDecimal(300.00));

        orderDate = LocalDateTime.of(2018, 12, 6, 12, 12);
        products = new HashSet<>(Arrays.asList(productThree));
        Order orderThree = newOrder(3,"aaa@aaa.com", Timestamp.valueOf(orderDate), products, new BigDecimal(500.00));

        Mockito.when(productRepository.findByName(productThree.getName()))
                .thenReturn(Optional.of(productThree));
        Mockito.when(productRepository.findById(productOne.getId()))
                .thenReturn(Optional.of(productOne));
        Mockito.when(productRepository.findById(productTwo.getId()))
                .thenReturn(Optional.of(productTwo));
        Mockito.when(productRepository.findById(productThree.getId()))
                .thenReturn(Optional.of(productThree));
        Mockito.when(orderRepository.save(orderOne))
                .thenReturn(orderOne);
        Mockito.when(orderRepository.save(orderTwo))
                .thenReturn(orderTwo);
        Mockito.when(orderRepository.save(orderThree))
                .thenReturn(orderThree);

        Mockito.when(orderRepository.findById(orderThree.getId()))
                .thenReturn(Optional.of(orderThree));

        LocalDateTime start = LocalDateTime.of(2018, 12, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2018, 12, 3, 0, 0);

        Mockito.when(orderRepository.findByOrderDateBetweenOrderByOrderDateDesc(Timestamp.valueOf(start), Timestamp.valueOf(end)))
                .thenReturn(Arrays.asList(orderOne));

        start = LocalDateTime.of(2018, 12, 6, 10, 0);
        end = LocalDateTime.of(2018, 12, 6, 12, 30);

        Mockito.when(orderRepository.findByOrderDateBetweenOrderByOrderDateDesc(Timestamp.valueOf(start), Timestamp.valueOf(end)))
                .thenReturn(Arrays.asList(orderTwo, orderThree));

        start = LocalDateTime.of(2018, 12, 4, 0, 0);
        end = LocalDateTime.of(2018, 12, 4, 0, 0);

        Mockito.when(orderRepository.findByOrderDateBetweenOrderByOrderDateDesc(Timestamp.valueOf(start), Timestamp.valueOf(end)))
                .thenReturn(Arrays.asList());

    }

    @Test
    public void findByRangeOne() {
        LocalDateTime start = LocalDateTime.of(2018, 12, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2018, 12, 3, 0, 0);
        List<Order> orders = orderService.findByRange(Timestamp.valueOf(start), Timestamp.valueOf(end));

        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
    }

    @Test
    public void findByRangeTwo() {
        LocalDateTime start = LocalDateTime.of(2018, 12, 6, 10, 0);
        LocalDateTime end = LocalDateTime.of(2018, 12, 6, 12, 30);
        List<Order> orders = orderService.findByRange(Timestamp.valueOf(start), Timestamp.valueOf(end));

        assertFalse(orders.isEmpty());
        assertEquals(2, orders.size());
    }

    @Test
    public void findByRangeNoOrder() {
        LocalDateTime start = LocalDateTime.of(2018, 12, 4, 0, 0);
        LocalDateTime end = LocalDateTime.of(2018, 12, 5, 0, 0);
        List<Order> orders = orderService.findByRange(Timestamp.valueOf(start), Timestamp.valueOf(end));

        assertTrue(orders.isEmpty());
    }

    @Test
    public void createOrder() throws ProductNotFoundException {
        PlaceOrderRequest request = newPlaceOrderRequest("aaa@aaa.com", Arrays.asList((long) 1, (long) 2));
        PlaceOrderResponse result = orderService.createOrder(request);
        Assert.assertNotNull(result);
        assertTrue(new BigDecimal(500.00).compareTo(result.getOrderAmount()) == 0);
    }

    @Test
    public void updateNoOrderPrice() {
        assertNull(orderService.updateOrderPrice(1000));
    }

    @Test
    public void updateOrderPrice() throws ProductNotFoundException {


        PlaceOrderRequest request = newPlaceOrderRequest("aaa@aaa.com", Arrays.asList((long) 3));
        PlaceOrderResponse placeOrderResponse = orderService.createOrder(request);

        UpdateProductRequest updateProductRequest = newUpdateProductRequest(null, new BigDecimal(800.00));
        productService.updateProduct(3, updateProductRequest);

        Order result = orderService.updateOrderPrice(placeOrderResponse.getOrderId());
        assertNotNull(result);
        assertTrue(new BigDecimal(800.00).compareTo(result.getOrderAmount()) == 0);
    }

    public PlaceOrderRequest newPlaceOrderRequest(String email, List<Long> products) {
        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setEmail(email);
        request.setProducts(products);
        return request;
    }

    @TestConfiguration
    static class OrderServiceImplTestContextConfiguration {

        @Bean
        public ProductService productService() {
            return new ProductServiceImpl();
        }

        @Bean
        public OrderService orderService() {
            return new OrderServiceImpl();
        }
    }
}