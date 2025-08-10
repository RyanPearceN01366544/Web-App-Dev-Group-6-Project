package com.Group6.WebAppDevGroupProject.Repository;

import com.Group6.WebAppDevGroupProject.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// Used Reference: https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
// (Can't Remember if we covered it in class or not. - Ryan)
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT * FROM orders o WHERE o.user_id = ?1")
    List<Order> findAllByUser(long userID_);
    @Query("SELECT * FROM orders WHERE NOT status = 'CLOSED'")
    List<Order> findAllActive();
    @Query("SELECT * FROM orders WHERE user_id = ?1 AND NOT status = 'CLOSED'")
    List<Order> findAllActiveByUser(long userID_);
}
