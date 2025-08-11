package com.Group6.WebAppDevGroupProject.Repository;

import com.Group6.WebAppDevGroupProject.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// Used Reference: https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
// (Can't Remember if we covered it in class or not. - Ryan)
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT * FROM orders o WHERE o.user_id = :user_id", nativeQuery = true)
    List<Order> findAllByUser(@Param("user_id") long user_id);
    @Query(value = "SELECT * FROM orders WHERE NOT status = 'CLOSED'", nativeQuery = true)
    List<Order> findAllActive();
    @Query(value = "SELECT * FROM orders WHERE user_id = :user_id AND NOT status = 'CLOSED'", nativeQuery = true)
    List<Order> findAllActiveByUser(@Param("user_id") long user_id);
}
