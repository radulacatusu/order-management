package com.mine.ordermgm.repository;

import com.mine.ordermgm.model.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @stefanl
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void findByName() {
        Product product = new Product();
        product.setName("Test product");
        product.setPrice(new BigDecimal(100.00));
        entityManager.persist(product);
        entityManager.flush();

        Optional<Product> found = productRepository.findByName(product.getName());
        Assert.assertTrue(found.isPresent());
        Assert.assertNotNull(found.get().getId());
        Assert.assertEquals(found.get().getName(), product.getName());
    }
}