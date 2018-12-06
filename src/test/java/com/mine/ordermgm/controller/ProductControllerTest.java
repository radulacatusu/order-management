package com.mine.ordermgm.controller;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.mine.ordermgm.OrderApplication;
import com.mine.ordermgm.api.UpdateProductRequest;
import com.mine.ordermgm.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.*;

/**
 * @stefanl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = OrderApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class ProductControllerTest {

    @Test
    public void createNewProductInvalidName() {
        Product request = newProduct(null, new BigDecimal(100.00));
        Response response = createProductRequest(request);
        assertEquals(400, response.statusCode());
    }

    @Test
    public void createNewProductInvalidPrice() {
        Product request = newProduct("Test", null);
        Response response = createProductRequest(request);
        assertEquals(400, response.statusCode());
    }

    @Test
    public void createNewProduct() {
        Product request = newProduct("Test Product New", new BigDecimal(100.00));
        Response response = createProductRequest(request);
        assertEquals(201, response.statusCode());
    }

    @Test
    public void createExistingProduct() {
        Product request = newProduct("Test Product", new BigDecimal(100.00));
        createProductRequest(request);
        request = newProduct("Test Product", new BigDecimal(1200.00));
        Response response = createProductRequest(request);
        assertEquals(409, response.statusCode());
    }

    @Test
    public void getProducts() {
        Product request = newProduct("Test Product", new BigDecimal(100.00));
        createProductRequest(request);
        Response response = getProductsRequest();
        assertEquals(200, response.statusCode());
        List<Product> list = response.body().as(List.class);
        assertFalse(list.isEmpty());
    }

    @Test
    public void updateNotExistingProduct() {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setPrice(new BigDecimal(100.00));
        Response response = updateProductRequest(100, request);
        assertEquals(404, response.statusCode());
    }

    @Test
    public void updateProduct() {

        Product request = newProduct("Test Product", new BigDecimal(100.00));
        createProductRequest(request);

        UpdateProductRequest updateRequest = new UpdateProductRequest();
        updateRequest.setPrice(new BigDecimal(100.00));
        updateRequest.setName("New name");

        Response response = updateProductRequest(1, updateRequest);
        assertEquals(200, response.statusCode());

        Product newProduct = response.body().as(Product.class);
        assertEquals(updateRequest.getName(), newProduct.getName());
        assertTrue(updateRequest.getPrice().compareTo(newProduct.getPrice()) == 0);
    }

    private Response updateProductRequest(long productId,
                                          UpdateProductRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/products/" + productId);
    }

    private Response getProductsRequest() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/products");
    }

    private Response createProductRequest(Product request) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/products");
    }

    private Product newProduct(String name,
                               BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}