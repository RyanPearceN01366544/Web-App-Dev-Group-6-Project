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
    @Query("SELECT o FROM Orders o WHERE o.user_id = :userId")
    List<Order> findAllByUser(@Param("userId") long userId);

    @Query("SELECT o FROM Orders o WHERE o.order_status <> 'CLOSED'")
    List<Order> findAllActive();

    @Query("SELECT o FROM Orders o WHERE o.user_id = :userId AND o.order_status <> 'CLOSED'")
    List<Order> findAllActiveByUser(@Param("userId") long userId);
}
