package com.Group6.WebAppDevGroupProject.Service;

import com.Group6.WebAppDevGroupProject.Models.MenuItem;
import com.Group6.WebAppDevGroupProject.Models.Order;
import com.Group6.WebAppDevGroupProject.Repository.MenuRepository;
import com.Group6.WebAppDevGroupProject.Repository.OrderRepository;
import com.Group6.WebAppDevGroupProject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OrderService {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MenuRepository menuRepository;


    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
    public List<Order> getAllActiveOrders() {
        return orderRepo.findAllActive();
    }
    public List<Order> getAllOrdersFromUser(long id_) {
        if (userRepo.findById(id_).isPresent()) {
            return orderRepo.findAllByUser(id_);
        }
        return null;
    }
    public List<Order> getAllActiveOrdersFromUser(long id_) {
        if (userRepo.findById(id_).isPresent()) {
            return orderRepo.findAllActiveByUser(id_);
        }
        return null;
    }
    public Order getOrderById(long id_) {
        return orderRepo.findById(id_).orElse(null);
    }

    public List<MenuItem> getMenuItemsFromOrder(long id_) {
        Optional<Order> ord_ = orderRepo.findById(id_);
        if (ord_.isPresent()) {
            if (!ord_.get().getOrder_items().equals("[]")) {
                List<String> orderItemsIdsStr = Arrays.stream(ord_.get().getOrder_items().replaceAll("[\\[\\]]", "").split(",")).toList();
                long[] orderItemsIds = orderItemsIdsStr.stream().map(String::trim).mapToLong(Long::parseLong).toArray();
                List<MenuItem> menuItems = new ArrayList<>();
                for (int x_ = 0; x_ <= orderItemsIds.length; x_ = x_ + 1) {
                    menuItems.add(menuRepository.findById(orderItemsIds[x_]).orElse(null));
                }
                return menuItems;
            }
        }
        return null;
    }

    public Order saveOrder(Order ord_) {
        return orderRepo.save(ord_);
    }
    public Order updateOrder(long id_, Order ord_) {
        return orderRepo.findById(id_).map(order -> {
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
