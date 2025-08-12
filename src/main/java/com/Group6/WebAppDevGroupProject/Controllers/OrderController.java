package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.MenuItem;
import com.Group6.WebAppDevGroupProject.Models.Order;
import com.Group6.WebAppDevGroupProject.Models.User;
import com.Group6.WebAppDevGroupProject.Service.MenuService;
import com.Group6.WebAppDevGroupProject.Service.OrderService;
import com.Group6.WebAppDevGroupProject.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    /* ---------------- ADD ITEM TO ORDER ---------------- */
    @PostMapping("/add")
    public String addToOrder(@SessionAttribute(value = "user", required = false) User user,
                             @RequestParam("itemId") Integer itemId) {
        if (user == null) return "redirect:/users/login";

        List<Order> orders = orderService.getAllActiveOrdersFromUser(user.getUser_id());
        Order order;
        if (orders.isEmpty()) {
            order = orderService.createOrderForUser(user.getUser_id());
        } else {
            order = orders.get(0);
        }
        orderService.addItemToOrder(order, itemId);
        return "redirect:/order/checkout";
    }

    /* ---------------- CHECKOUT PAGE ---------------- */
    @GetMapping("/checkout")
    public String checkout(@SessionAttribute(value = "user", required = false) User user, Model model) {
        if (user == null) return "redirect:/users/login";

        List<Order> orders = orderService.getAllActiveOrdersFromUser(user.getUser_id());
        if (orders.isEmpty()) {
            model.addAttribute("error", "No active order to checkout.");
            return "checkout";
        }

        Order order = orders.get(0);
        List<MenuItem> orderItems = orderService.getMenuItemsFromOrder(order.getOrder_id());

        Map<String, Map<String, Object>> summary = new LinkedHashMap<>();
        double total = 0.0;

        for (MenuItem item : orderItems) {
            String key = item.getName() + ":" + item.getPrice();
            summary.computeIfAbsent(key, k -> {
                Map<String, Object> entry = new HashMap<>();
                entry.put("id", item.getId());
                entry.put("name", item.getName());
                entry.put("description", item.getDescription());
                entry.put("price", item.getPrice());
                entry.put("quantity", 0);
                return entry;
            });
            Map<String, Object> entry = summary.get(key);
            entry.put("quantity", (int) entry.get("quantity") + 1);
            total += item.getPrice().doubleValue();
        }

        model.addAttribute("order", order);
        model.addAttribute("orderSummary", summary.values());
        model.addAttribute("total", total);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getRole());
        return "checkout";
    }

    /* ---------------- PROCESS CHECKOUT ---------------- */
    @PostMapping("/checkout")
    public String processCheckout(@SessionAttribute(value = "user", required = false) User user,
                                  @RequestParam(value = "requestedDate", required = false) String requestedDateStr,
                                  @RequestParam(value = "orderId", required = false) Integer orderId) {
        if (user == null) return "redirect:/users/login";

        Order order;

        if (orderId != null) {
            order = orderService.getOrderById(orderId);
            if (order == null || !order.getUser_id().equals(user.getUser_id())) {
                return "redirect:/order/my-orders";
            }

            long currentTime = System.currentTimeMillis();
            long dueTime = order.getOrder_requested().getTime();
            long oneHourInMs = 3600000;

            if (dueTime - currentTime <= oneHourInMs) {
                return "redirect:/order/my-orders";
            }
        } else {
            List<Order> orders = orderService.getAllActiveOrdersFromUser(user.getUser_id());
            if (orders.isEmpty()) {
                return "redirect:/order/checkout";
            }
            order = orders.get(0);
        }

        if (requestedDateStr != null && !requestedDateStr.isEmpty()) {
            LocalDateTime ldt = LocalDateTime.parse(requestedDateStr);
            Date requestedDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            order.setOrder_requested(requestedDate);
        }

        order.setOrder_status("PENDING");
        orderService.saveOrder(order);

        return (orderId != null) ? "redirect:/order/my-orders" : "redirect:/order/payment";
    }

    /* ---------------- PAYMENT PAGE ---------------- */
    @GetMapping("/payment")
    public String paymentPage(@SessionAttribute(value = "user", required = false) User user, Model model) {
        if (user == null) return "redirect:/users/login";

        List<Order> orders = orderService.getAllActiveOrdersFromUser(user.getUser_id());
        Order order = orders.stream()
                .filter(o -> "PENDING".equalsIgnoreCase(o.getOrder_status()))
                .findFirst()
                .orElse(null);

        if (order == null) {
            model.addAttribute("error", "No pending order to pay for.");
            return "payment";
        }

        List<MenuItem> orderItems = orderService.getMenuItemsFromOrder(order.getOrder_id());
        Map<String, Map<String, Object>> summary = buildOrderSummary(orderItems);

        model.addAttribute("order", order);
        model.addAttribute("orderSummary", summary.values());
        model.addAttribute("total", calculateTotal(orderItems));
        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getRole());
        return "payment";
    }

    /* ---------------- PROCESS PAYMENT ---------------- */
    @PostMapping("/payment")
    public String processPayment(@SessionAttribute(value = "user", required = false) User user,
                                 @RequestParam(value = "paymentType", required = false) String paymentType,
                                 @RequestParam(value = "requestedDate", required = false) String requestedDateStr,
                                 @RequestParam(value = "cardNumber", required = false) String cardNumber,
                                 @RequestParam(value = "expiry", required = false) String expiry,
                                 @RequestParam(value = "cvv", required = false) String cvv,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (user == null) return "redirect:/users/login";

        List<Order> orders = orderService.getAllActiveOrdersFromUser(user.getUser_id());
        Order order = orders.stream()
                .filter(o -> "PENDING".equalsIgnoreCase(o.getOrder_status()))
                .findFirst()
                .orElse(null);

        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "No pending order to pay for.");
            return "redirect:/order/payment";
        }

        // Payment validation
        if ("card".equals(paymentType) && (cardNumber.isEmpty() || expiry.isEmpty() || cvv.isEmpty())) {
            redirectAttributes.addFlashAttribute("error", "Please fill in all card details");
            return "redirect:/order/payment";
        } else if (paymentType == null) {
            redirectAttributes.addFlashAttribute("error", "Please select a payment method");
            return "redirect:/order/payment";
        }

        if (requestedDateStr != null && !requestedDateStr.isEmpty()) {
            LocalDateTime ldt = LocalDateTime.parse(requestedDateStr);
            Date requestedDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            order.setOrder_requested(requestedDate);
        }

        order.setOrder_status("PLACED");
        orderService.saveOrder(order);

        List<MenuItem> orderItems = orderService.getMenuItemsFromOrder(order.getOrder_id());
        updateStock(orderItems);

        // Create fresh order (new empty cart)
        orderService.createOrderForUser(user.getUser_id());

        // Clear session cache if any
        session.removeAttribute("cart");
        session.removeAttribute("order");

        // Add order info as flash attributes to display on success page
        redirectAttributes.addFlashAttribute("order", order);
        redirectAttributes.addFlashAttribute("orderSummary", buildOrderSummary(orderItems).values());
        redirectAttributes.addFlashAttribute("total", calculateTotal(orderItems));

        return "redirect:/order/success";
    }

    /* ---------------- ORDER SUCCESS PAGE ---------------- */
    @GetMapping("/success")
    public String orderSuccess(@SessionAttribute(value = "user", required = false) User user, Model model) {
        if (user == null) return "redirect:/users/login";

        // If order info not in model (refresh or direct access), redirect to menu or orders page
        if (!model.containsAttribute("order")) {
            return "redirect:/menu";
        }

        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", user.getRole());

        return "order-success";
    }



    /* ---------------- CANCEL ORDER ---------------- */
    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable("id") int orderId,
                              @SessionAttribute(value = "user", required = false) User user) {
        if (user == null) return "redirect:/users/login";

        Order order = orderService.getOrderById(orderId);
        if (order == null || !order.getUser_id().equals(user.getUser_id())) {
            return "redirect:/order/my-orders";
        }

        long currentTime = System.currentTimeMillis();
        long dueTime = order.getOrder_requested().getTime();
        long oneHourInMs = 3600000;

        if (dueTime - currentTime > oneHourInMs) {
            orderService.cancelOrder(orderId);
        }
        return "redirect:/order/my-orders";
    }

    /* ---------------- MY ORDERS PAGE ---------------- */
    @GetMapping("/my-orders")
    public String userOrderHistory(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "You must be logged in to view your orders.");
            return "my-order";
        }

        List<Order> orders = orderService.getAllOrdersFromUser(user.getUser_id());
        Map<Integer, List<MenuItem>> orderItemsMap = new HashMap<>();

        for (Order order : orders) {
            orderItemsMap.put(order.getOrder_id(), orderService.getMenuItemsFromOrder(order.getOrder_id()));
        }

        model.addAttribute("orders", orders);
        model.addAttribute("orderItemsMap", orderItemsMap);
        return "my-order";
    }

    /* ---------------- HELPER METHODS ---------------- */
    private Map<String, Map<String, Object>> buildOrderSummary(List<MenuItem> orderItems) {
        Map<String, Map<String, Object>> summary = new LinkedHashMap<>();
        for (MenuItem item : orderItems) {
            String key = item.getName() + ":" + item.getPrice();
            summary.computeIfAbsent(key, k -> {
                Map<String, Object> entry = new HashMap<>();
                entry.put("name", item.getName());
                entry.put("price", item.getPrice());
                entry.put("quantity", 0);
                return entry;
            });
            Map<String, Object> entry = summary.get(key);
            entry.put("quantity", (int) entry.get("quantity") + 1);
        }
        return summary;
    }

    private double calculateTotal(List<MenuItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(item -> item.getPrice().doubleValue())
                .sum();
    }

    private void updateStock(List<MenuItem> orderItems) {
        for (MenuItem item : orderItems) {
            item.setStock(item.getStock() - 1);
            menuService.save(item);
        }
    }
}
