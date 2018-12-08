package com.mine.ordermgm.repository;

import com.mine.ordermgm.model.Order;
import com.mine.ordermgm.model.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @stefanl
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;


    @Test
    public void findByOrderDateBetweenOrderByOrderDateDesc() {
        Product productOne = new Product();
        productOne.setName("Test product");
        productOne.setPrice(new BigDecimal(100.00));
        productOne = entityManager.persist(productOne);
        entityManager.flush();

        Product productTwo = new Product();
        productTwo.setName("Test product two");
        productTwo.setPrice(new BigDecimal(300.00));
        productTwo = entityManager.persist(productTwo);
        entityManager.flush();

        Order order = new Order();
        order.setEmail("aaa@aaa.com");
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        Set<Product> products = new HashSet<>(Arrays.asList(productOne, productTwo));
        order.setProducts(products);
        order.setOrderAmount(new BigDecimal(400.00));
        order = entityManager.persist(order);
        entityManager.flush();

        LocalDateTime start = LocalDateTime.now().minusMinutes(2);
        LocalDateTime end = LocalDateTime.now();

        List<Order> orders = orderRepository.findByOrderDateBetweenOrderByOrderDateDesc(Timestamp.valueOf(start),
                Timestamp.valueOf(end));
        Assert.assertNotNull(orders);
        Assert.assertEquals(1, orders.size());
        Assert.assertNotNull(orders.get(0).getId());
        Assert.assertEquals(order.getOrderAmount(), orders.get(0).getOrderAmount());
        Assert.assertEquals(2, orders.get(0).getProducts().size());
    }
}