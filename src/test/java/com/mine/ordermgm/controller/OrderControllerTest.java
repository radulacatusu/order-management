package com.mine.ordermgm.controller;

import com.mine.ordermgm.OrderApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * @stefanl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = OrderApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class OrderControllerTest {

    @Test
    public void createOrder() {
    }

    @Test
    public void getOrders() {
    }

    @Test
    public void calculateAmount() {
    }
}