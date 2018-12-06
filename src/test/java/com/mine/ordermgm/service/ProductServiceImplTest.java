package com.mine.ordermgm.service;

import com.mine.ordermgm.api.UpdateProductRequest;
import com.mine.ordermgm.model.Product;
import com.mine.ordermgm.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @stefanl
 */
@RunWith(SpringRunner.class)
public class ProductServiceImplTest {

    @Autowired
    private ProductService productService;
    @MockBean
    private ProductRepository productRepository;

    @Before
    public void setUp() {
        Product productOne = newProduct(1, "Test product", new BigDecimal(200.00));
        Product productTwo = newProduct(2, "Test product two", new BigDecimal(300.00));
        Mockito.when(productRepository.findById(productOne.getId()))
                .thenReturn(Optional.of(productOne));
        Mockito.when(productRepository.findByName(productOne.getName()))
                .thenReturn(Optional.of(productOne));
        Mockito.when(productRepository.findById((long)100))
                .thenReturn(Optional.empty());
        List<Product> all = Arrays.asList(productOne);
        Mockito.when(productRepository.findAll())
                .thenReturn(all);
        Mockito.when(productRepository.save(productTwo))
                .thenReturn(productTwo);

    }

    @Test
    public void findById() {
        long id = 1;
        Optional<Product> found = productService.findById(id);
        assertTrue(found.isPresent());
        assertEquals(found.get().getId(), id);
    }

    @Test
    public void findByNonExistingId() {
        long id = 100;
        Optional<Product> found = productService.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    public void findAll() {
        List<Product> all = productService.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    public void createProduct() {
        Product productOne = newProduct(1, "Test product two", new BigDecimal(200.00));
        long result = productService.createProduct(productOne);
        Assert.assertNotEquals(result, -1);
    }

    @Test
    public void createExistingProduct() {
        Product product = new Product();
        product.setName("Test product");
        product.setPrice(new BigDecimal(100.00));

        long result = productService.createProduct(product);
        assertEquals(result, -1);
    }

    @Test
    public void updateProduct() {
        UpdateProductRequest request = newUpdateProductRequest("New product", new BigDecimal(500.00));
        Optional<Product> result = productService.updateProduct(1, request);
        assertTrue(result.isPresent());
        assertEquals(request.getName(), result.get().getName());
        assertTrue(request.getPrice().compareTo(result.get().getPrice()) == 0);
    }

    @Test
    public void updateNotExistingProduct() {
        UpdateProductRequest request = newUpdateProductRequest("New product", new BigDecimal(500.00));
        Optional<Product> result = productService.updateProduct(100, request);
        assertFalse(result.isPresent());
    }

    public Product newProduct(long id, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public UpdateProductRequest newUpdateProductRequest(String name, BigDecimal price) {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName(name);
        request.setPrice(price);
        return request;
    }

    @TestConfiguration
    static class ProductServiceImplTestContextConfiguration {

        @Bean
        public ProductService productService() {
            return new ProductServiceImpl();
        }
    }
}