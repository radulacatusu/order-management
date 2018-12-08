package com.mine.ordermgm.service;

import com.mine.ordermgm.api.UpdateProductRequest;
import com.mine.ordermgm.exception.ProductNotFoundException;
import com.mine.ordermgm.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @stefanl
 */
public interface ProductService {

    /**
     * Retrieve the product with the specified id
     *
     * @param id
     * @return
     */
    Optional<Product> findById(Long id);

    /**
     * Retrieve the products
     *
     * @return
     */
    List<Product> findAll();

    /**
     * Create product
     *
     * @param product
     * @return
     */
    long createProduct(Product product);

    /**
     * update product
     *
     * @param request
     * @return
     */
    Optional<Product> updateProduct(long id,
                                    UpdateProductRequest request);

    /**
     * @param ids
     * @return
     * @throws ProductNotFoundException
     */
    Set<Product> findProducts(List<Long> ids) throws ProductNotFoundException;
}
