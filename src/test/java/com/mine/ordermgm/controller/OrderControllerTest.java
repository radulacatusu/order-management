package com.mine.ordermgm.controller;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.mine.ordermgm.OrderApplication;
import com.mine.ordermgm.api.PlaceOrderRequest;
import com.mine.ordermgm.api.PlaceOrderResponse;
import com.mine.ordermgm.api.UpdateProductRequest;
import com.mine.ordermgm.model.Order;
import com.mine.ordermgm.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static com.mine.ordermgm.controller.ProductControllerTest.createProductRequest;
import static com.mine.ordermgm.controller.ProductControllerTest.newProduct;
import static com.mine.ordermgm.controller.ProductControllerTest.updateProductRequest;
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
        Product firstProd = newProduct("Test Product", new BigDecimal(200.00));
        firstProd = createProductRequest(firstProd).as(Product.class);

        Product secondProd = newProduct("Test Product New", new BigDecimal(300.00));
        secondProd = createProductRequest(secondProd).as(Product.class);

        PlaceOrderRequest request = newPlaceOrderRequest("aaa@aaa.com", Arrays.asList(firstProd.getId(),
                secondProd.getId()));
        Response response = placeOrderRequest(request);
        assertEquals(201, response.statusCode());

        PlaceOrderResponse poResponse = response.body().as(PlaceOrderResponse.class);
        assertNotNull(poResponse.getOrderId());
        assertTrue(new BigDecimal(500.00).compareTo(poResponse.getOrderAmount()) == 0);
    }

    @Test
    public void createOrderProductNotExists() {
        PlaceOrderRequest request = newPlaceOrderRequest("aaa@aaa.com", Arrays.asList((long) 1000));
        Response response = placeOrderRequest(request);
        assertEquals(400, response.statusCode());
    }

    @Test
    public void createOrderInvalidEmail() {
        PlaceOrderRequest request = newPlaceOrderRequest("email", Arrays.asList((long) 1, (long) 2));
        Response response = placeOrderRequest(request);
        assertEquals(400, response.statusCode());
    }

    @Test
    public void createOrderInvalidProducts() {
        PlaceOrderRequest request = newPlaceOrderRequest("email", null);
        Response response = placeOrderRequest(request);
        assertEquals(400, response.statusCode());
    }

    @Test
    public void getOrders() {
        LocalDateTime start = LocalDateTime.of(2018, 12, 6, 10, 0);
        LocalDateTime end = LocalDateTime.of(2018, 12, 6, 12, 30);
        Response response = getOrderRequest(Timestamp.valueOf(start), Timestamp.valueOf(end));
        assertEquals(200, response.statusCode());

        List<Order> orders = response.body().as(List.class);
        assertNotNull(orders);
    }

    @Test
    public void updateAmount() {
        Product firstProd = newProduct("Test Product To calculate", new BigDecimal(200.00));
        firstProd = createProductRequest(firstProd).as(Product.class);

        Product secondProd = newProduct("Test Product to calculate two", new BigDecimal(300.00));
        secondProd = createProductRequest(secondProd).as(Product.class);

        PlaceOrderRequest request = newPlaceOrderRequest("aaa@aaa.com", Arrays.asList(firstProd.getId(), secondProd.getId()));
        PlaceOrderResponse placeOrderResponse = placeOrderRequest(request).as(PlaceOrderResponse.class);

        UpdateProductRequest updateRequest = new UpdateProductRequest();
        updateRequest.setPrice(new BigDecimal(700.00));

        updateProductRequest(firstProd.getId(), updateRequest);
        Response response = updateAmount(placeOrderResponse.getOrderId());

        assertEquals(200, response.statusCode());
        Order orderUpdated = response.as(Order.class);
        assertTrue(new BigDecimal(1000.00).compareTo(orderUpdated.getOrderAmount()) == 0);
    }

    @Test
    public void updateAmountNoOrderFound() {
        updateAmount(1001).then().assertThat().statusCode(404);
    }

    private Response updateAmount(long id) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .put("/orders/" + id + "/calculate");
    }


    private Response placeOrderRequest(PlaceOrderRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/orders");
    }

    private Response getOrderRequest(Timestamp start, Timestamp end) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/orders?start=" + start + "&end=" + end);
    }

    private PlaceOrderRequest newPlaceOrderRequest(String email,
                                                   List<Long> products) {
        PlaceOrderRequest request = new PlaceOrderRequest();
        request.setEmail(email);
        request.setProducts(products);
        return request;
    }
}