package com.mine.ordermgm.repository;

import com.mine.ordermgm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @stefanl
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * @param name
     * @return
     */
    Optional<Product> findByName(String name);

}
