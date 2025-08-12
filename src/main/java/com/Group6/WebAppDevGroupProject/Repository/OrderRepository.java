package com.Group6.WebAppDevGroupProject.Repository;

import com.Group6.WebAppDevGroupProject.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Use JPQL for custom queries
    @Query("SELECT o FROM Order o WHERE o.user_id = ?1")
    List<Order> findAllByUser(long userID_);

    @Query("SELECT o FROM Order o WHERE o.order_status <> 'CLOSED'")
    List<Order> findAllActive();

    @Query("SELECT o FROM Order o WHERE o.user_id = ?1 AND o.order_status <> 'CLOSED'")
    List<Order> findAllActiveByUser(long userID_);
}
