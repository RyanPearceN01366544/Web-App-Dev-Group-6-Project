package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.Order;
import com.Group6.WebAppDevGroupProject.Service.MenuService;
import com.Group6.WebAppDevGroupProject.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders/api")
@CrossOrigin
public class OrderAdminController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    // ✅ Get all orders
    @GetMapping("/")
    public List<Order> list() {
        return orderService.getAllOrders();
    }

    // ✅ Get details of a single order
    @GetMapping("/{id}")
    public Order getOrderDetails(@PathVariable Integer id) {
        return orderService.getOrderById(id);
    }

    // ✅ Get all active orders
    @GetMapping("/active/")
    public List<Order> listActiveOrders() {
        return orderService.getAllActiveOrders();
    }

    // ✅ Get all active orders for a specific user
    @GetMapping("/active/{id}")
    public List<Order> listActiveUserOrders(@PathVariable Integer id) {
        return orderService.getAllActiveOrdersFromUser(id);
    }

    // ✅ Save a new order
    @PostMapping("/")
    public Order saveOrder(@RequestBody Order ord_) {
        return orderService.saveOrder(ord_);
    }

    // ✅ Update an existing order
    @PutMapping("/{id_}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id_, @RequestBody Order ord_) {
        Order updated_ = orderService.updateOrder(id_, ord_);
        return (updated_ != null) ? ResponseEntity.ok(updated_) : ResponseEntity.notFound().build();
    }

    // ✅ Delete an order
    @DeleteMapping("/{id_}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id_) {
        orderService.deleteOrder(id_);
        return ResponseEntity.noContent().build();
    }
}
