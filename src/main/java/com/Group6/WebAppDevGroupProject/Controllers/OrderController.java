package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.MenuItem;
import com.Group6.WebAppDevGroupProject.Models.Order;
import com.Group6.WebAppDevGroupProject.Repositories.MenuItemRepository;
import com.Group6.WebAppDevGroupProject.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // POST: Place new order
    @PostMapping
    public String placeOrder(@RequestParam int userId, @RequestBody String orderItemsJson) {
        try {
            // Parse JSON: { "1": 2, "3": 1 }
            Map<String, Integer> items = objectMapper.readValue(orderItemsJson, Map.class);

            // Check stock & update
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                int itemId = Integer.parseInt(entry.getKey());
                int quantity = entry.getValue();

                Optional<MenuItem> optionalItem = menuItemRepository.findById(itemId);
                if (optionalItem.isEmpty()) return "Item ID " + itemId + " not found";

                MenuItem item = optionalItem.get();
                if (item.getStock() < quantity) {
                    return "Not enough stock for: " + item.getName();
                }

                // Decrease stock
                item.setStock(item.getStock() - quantity);
                menuItemRepository.save(item);
            }

            Order order = new Order();
            order.setUser_id(userId);
            order.setOrder_items(orderItemsJson);
            order.setOrder_time(LocalDateTime.now());
            order.setOrder_status("ORDER SENT");

            orderRepository.save(order);
            return "Order placed successfully!";
        } catch (Exception e) {
            return "Order failed: " + e.getMessage();
        }
    }

    // DELETE: Cancel order (only if more than 24 hours before order time)
    @DeleteMapping("/{id}")
    public String cancelOrder(@PathVariable int id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) return "Order not found";

        Order order = optionalOrder.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cancelDeadline = order.getOrder_time().plusHours(24);

        if (now.isBefore(cancelDeadline)) {
            // restore stock
            try {
                Map<String, Integer> items = objectMapper.readValue(order.getOrder_items(), Map.class);
                for (Map.Entry<String, Integer> entry : items.entrySet()) {
                    int itemId = Integer.parseInt(entry.getKey());
                    int quantity = entry.getValue();

                    MenuItem item = menuItemRepository.findById(itemId).orElse(null);
                    if (item != null) {
                        item.setStock(item.getStock() + quantity);
                        menuItemRepository.save(item);
                    }
                }
            } catch (Exception ignored) {}

            order.setOrder_status("CANCELLED");
            orderRepository.save(order);
            return "Order cancelled";
        } else {
            return "Cancellation deadline passed (must be within 24h of order time)";
        }
    }

    // GET: Admin view all orders
    @GetMapping
    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
