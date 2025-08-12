package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.Order;
import com.Group6.WebAppDevGroupProject.Service.MenuService;
import com.Group6.WebAppDevGroupProject.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("orders/api")
@CrossOrigin
public class OrderAdminController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    @GetMapping("/")
    public List<Order> list() {
        return orderService.getAllOrders();
    }
    @GetMapping("/{id}")
    public Order getOrderDetails(@PathVariable long id) {
        return orderService.getOrderById(id);
    }
    @GetMapping("/active/")
    public List<Order> listActiveOrders() {
        return orderService.getAllActiveOrders();
    }
    @GetMapping("/active/{id}")
    public List<Order> listActiveUserOrders(@PathVariable long id) {
        return orderService.getAllActiveOrdersFromUser(id);
    }
    @PostMapping("/")
    public Order saveOrder(@RequestBody Order ord_)
    {
        return orderService.saveOrder(ord_);
    }
    @PutMapping("/{id_}")
    public ResponseEntity<Order> updateOrder(@PathVariable long id_, @RequestBody Order ord_)
    {
        Order updated_ = orderService.updateOrder(id_, ord_);
        return (updated_ != null) ? ResponseEntity.ok(updated_) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id_}")
    public ResponseEntity<Void> deleteOrder(@PathVariable long id_)
    {
        orderService.deleteOrder(id_);
        return ResponseEntity.noContent().build();
    }
}
