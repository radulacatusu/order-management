package com.mine.ordermgm.repository;

import com.mine.ordermgm.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @stefanl
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderDateBetweenOrderByOrderDateDesc(Timestamp startDate,
                                                           Timestamp endDate);
}
