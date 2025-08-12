package com.Group6.WebAppDevGroupProject.Repository;

import com.Group6.WebAppDevGroupProject.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE o.user_id = :userId")
    List<Order> findAllByUser(@Param("userId") Integer userId);

    // Only orders with status PENDING are considered active
    @Query("SELECT o FROM Order o WHERE o.order_status = 'PENDING'")
    List<Order> findAllActive();

    @Query("SELECT o FROM Order o WHERE o.user_id = :userId AND o.order_status = 'PENDING'")
    List<Order> findAllActiveByUser(@Param("userId") Integer userId);
}
