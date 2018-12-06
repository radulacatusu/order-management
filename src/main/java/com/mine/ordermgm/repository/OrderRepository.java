package com.mine.ordermgm.repository;

import com.mine.ordermgm.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @stefanl
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
