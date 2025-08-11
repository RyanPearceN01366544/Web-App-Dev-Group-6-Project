package com.Group6.WebAppDevGroupProject.Service;

import com.Group6.WebAppDevGroupProject.Models.Order;
import com.Group6.WebAppDevGroupProject.Repository.OrderRepository;
import com.Group6.WebAppDevGroupProject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;
    private UserRepository userRepo;

    public Optional<Order> getOrderById(long id_) {
        return orderRepo.findById(id_);
    }
    public Order[] getAllOrdersFromUser(long id_) {
        if (userRepo.findById(id_).isPresent())
        {
            return orderRepo.findAllByUser(id_);
        }
        return null;
    }
    public Order saveOrder(Order ord_) {
        return orderRepo.save(ord_);
    }
    public Order updateOrder(Order ord_) {
        return orderRepo.findById(ord_.getOrder_id()).map(order -> {
            order.setOrder_items(ord_.getOrder_items());
            order.setOrder_requested(ord_.getOrder_requested());
            order.setOrder_time(ord_.getOrder_time());
            order.setOrder_status(ord_.getOrder_status());
            return orderRepo.save(order);
        }).orElse(null);
    }
    public void deleteOrder(long id_) {
        orderRepo.deleteById(id_);
    }
}
