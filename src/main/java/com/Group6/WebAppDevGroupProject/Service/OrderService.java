package com.Group6.WebAppDevGroupProject.Service;

import com.Group6.WebAppDevGroupProject.Models.MenuItem;
import com.Group6.WebAppDevGroupProject.Models.Order;
import com.Group6.WebAppDevGroupProject.Repository.MenuRepository;
import com.Group6.WebAppDevGroupProject.Repository.OrderRepository;
import com.Group6.WebAppDevGroupProject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private MenuRepository menuRepository;

    // Delete order by ID
    public void deleteOrder(Integer orderId) {
        orderRepo.deleteById(orderId);
    }

    // Create empty order for a user, status PENDING means active/open
    public Order createOrderForUser(Integer userId) {
        Order order = new Order();
        order.setUser_id(userId);
        order.setOrder_status("PENDING");  // Active status
        order.setOrder_time(new Date());
        order.setOrder_items("[]");
        return orderRepo.save(order);
    }

    // Add menu item to order
    public void addItemToOrder(Order order, Integer itemId) {
        List<String> itemsList = parseItems(order.getOrder_items());
        itemsList.add(String.valueOf(itemId));
        order.setOrder_items(formatItems(itemsList));
        orderRepo.save(order);
    }

    // Remove menu item by index
    public void removeItemFromOrder(Order order, int idx) {
        List<String> itemsList = parseItems(order.getOrder_items());
        if (idx >= 0 && idx < itemsList.size()) {
            itemsList.remove(idx);
        }
        order.setOrder_items(formatItems(itemsList));
        orderRepo.save(order);
    }

    // Update item quantity by index
    public void updateItemQuantity(Order order, int idx, int quantity) {
        List<String> itemsList = parseItems(order.getOrder_items());
        if (idx >= 0 && idx < itemsList.size() && quantity > 0) {
            String itemId = itemsList.get(idx);
            // Remove old occurrences of that index
            itemsList.remove(idx);
            // Insert new quantity times
            for (int i = 0; i < quantity; i++) {
                itemsList.add(idx, itemId);
            }
        }
        order.setOrder_items(formatItems(itemsList));
        orderRepo.save(order);
    }

    // Parse stored order item IDs into list of strings
    private List<String> parseItems(String itemsStr) {
        if (itemsStr == null || itemsStr.trim().equals("[]") || itemsStr.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(itemsStr.replaceAll("[\\[\\]]", "").split(",")));
    }

    // Format list of item IDs back into string for DB
    private String formatItems(List<String> items) {
        return "[" + String.join(",", items) + "]";
    }

    // Fetch all orders in DB
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    // Fetch all active orders (PENDING status)
    public List<Order> getAllActiveOrders() {
        return orderRepo.findAllActive();
    }

    // Fetch all orders for a user (any status)
    public List<Order> getAllOrdersFromUser(Integer userId) {
        if (userRepo.findById(userId).isPresent()) {
            return orderRepo.findAllByUser(userId);
        }
        return Collections.emptyList();
    }

    // Fetch only active orders for a user (status PENDING)
    public List<Order> getAllActiveOrdersFromUser(Integer userId) {
        if (userRepo.findById(userId).isPresent()) {
            return orderRepo.findAllActiveByUser(userId);
        }
        return Collections.emptyList();
    }

    // Fetch order by ID
    public Order getOrderById(Integer orderId) {
        return orderRepo.findById(orderId).orElse(null);
    }

    // Convert stored order item IDs into list of MenuItem objects
    public List<MenuItem> getMenuItemsFromOrder(Integer orderId) {
        Optional<Order> ordOpt = orderRepo.findById(orderId);
        if (ordOpt.isEmpty()) return Collections.emptyList();

        List<String> ids = parseItems(ordOpt.get().getOrder_items());
        List<MenuItem> menuItems = new ArrayList<>();

        for (String idStr : ids) {
            try {
                Integer menuItemId = Integer.parseInt(idStr.trim());
                menuRepository.findById(menuItemId).ifPresent(menuItems::add);
            } catch (NumberFormatException e) {
                System.err.println("Invalid menu item ID: " + idStr);
            }
        }
        return menuItems;
    }

    private static final long CANCEL_TIME_LIMIT_MS = 60L * 60L * 1000L; // 1 hour

    // Save or update order, set defaults if missing
    public Order saveOrder(Order ord_) {
        if (ord_.getOrder_status() == null || ord_.getOrder_status().isEmpty()) {
            ord_.setOrder_status("PENDING");
        }
        if (ord_.getOrder_time() == null) {
            ord_.setOrder_time(new Date());
        }
        return orderRepo.save(ord_);
    }

    // Cancel order if within allowed time and status is PENDING
    public boolean cancelOrder(Integer orderId) {
        Optional<Order> orderOpt = orderRepo.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (!"PENDING".equals(order.getOrder_status())) return false;
            Date requestedDate = order.getOrder_requested();
            if (requestedDate == null) return false;

            long now = System.currentTimeMillis();
            long requestedTime = requestedDate.getTime();
            if (requestedTime - now >= CANCEL_TIME_LIMIT_MS) {
                order.setOrder_status("CANCELLED");
                orderRepo.save(order);
                return true;
            }
        }
        return false;
    }

    // Update order details by ID
    public Order updateOrder(Integer orderId, Order o) {
        return orderRepo.findById(orderId).map(order -> {
            order.setOrder_items(o.getOrder_items());
            order.setOrder_requested(o.getOrder_requested());
            order.setOrder_time(o.getOrder_time());
            order.setOrder_status(o.getOrder_status());
            return orderRepo.save(order);
        }).orElse(null);
    }
}
