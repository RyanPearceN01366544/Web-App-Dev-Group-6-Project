package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.Order;
import com.Group6.WebAppDevGroupProject.Models.User;
import com.Group6.WebAppDevGroupProject.Service.OrderService;
import com.Group6.WebAppDevGroupProject.Service.MenuService;
import com.Group6.WebAppDevGroupProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String list(Principal auth_, Model model_) {
        User user_ = userService.findByUsername(auth_.getName()).orElse(null);
        if (user_ != null) {
            model_.addAttribute("orders", orderService.getAllOrdersFromUser(user_.getUser_id()));
            return "orders-list";
        }
        return "redirect:/users/login";
    }
    @GetMapping("/active")
    public String listActiveOrders(Principal auth_, Model model_) {
        User user_ = userService.findByUsername(auth_.getName()).orElse(null);
        if (user_ != null) {
            model_.addAttribute("orders", orderService.getAllActiveOrdersFromUser(user_.getUser_id()));
            return "orders-list";
        }
        return "redirect:/users/login";
    }
    @GetMapping("/{id}")
    public String getOrder(Principal auth_, Model model_, @PathVariable("id") long id) {
        User user_ = userService.findByUsername(auth_.getName()).orElse(null);
        if (user_ != null) {
            Order ord_ = orderService.getOrderById(id);
            if (ord_.getUser_id() == user_.getUser_id() ||
                user_.getRole().equals("staff") ||
                user_.getRole().equals("admin")) {
                    model_.addAttribute("order", orderService.getOrderById(id));
                    model_.addAttribute("menuItems", orderService.getMenuItemsFromOrder(id));
            }
            return "orders-details";
        }
        return "redirect:/users/login";
    }
    @GetMapping("/new")
    public String create(Principal auth_, Model model_) {
        User user_ = userService.findByUsername(auth_.getName()).orElse(null);
        if (user_ != null) {
            model_.addAttribute("order", new Order());
            model_.addAttribute("menuItems", menuService); // -> Solve Later
            return "orders-create";
        }
        return "redirect:/users/login";
    }
    @PostMapping("/save")
    public String editForm(Model model_, @ModelAttribute Order ord_) {
        orderService.saveOrder(ord_);
        return "orders-thankyou";
    }
  
    @GetMapping("/edit/{id}")
    public String editView(Principal auth_, Model model_, @PathVariable("id") long id) {
        User user_ = userService.findByUsername(auth_.getName()).orElse(null);
        if (user_ != null) {
            Order ord_ = orderService.getOrderById(user_.getUser_id());
            if (user_.getUser_id() == ord_.getUser_id() || !Objects.equals(user_.getRole(), "customer")) {
                if (ord_.getUser_id() == user_.getUser_id() || user_.getRole().equals("staff") || user_.getRole().equals("admin")) {
                    model_.addAttribute("order", orderService.getOrderById(user_.getUser_id()));
                }
                return "order-edit";
            }
        }
        return "redirect:/users/login";
    }
    @PostMapping("/edit")
    public String edit(Principal auth_, @ModelAttribute Order ord_) {
        User user_ = userService.findByUsername(auth_.getName()).orElse(null);
        if (user_ != null && (user_.getUser_id() == ord_.getUser_id() || !Objects.equals(user_.getRole(), "customer"))) {
            orderService.updateOrder(ord_.getOrder_id(), ord_);
            return "redirect:/orders/";
        }
        return "redirect:/users/login";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders/";
    }
}