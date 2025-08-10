package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.MenuItem;
import com.Group6.WebAppDevGroupProject.Models.Order;
import com.Group6.WebAppDevGroupProject.Models.User;
import com.Group6.WebAppDevGroupProject.Service.OrderService;
import com.Group6.WebAppDevGroupProject.Service.MenuService;
import com.Group6.WebAppDevGroupProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

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
    public String list(Model model_, @ModelAttribute User user_) {
        model_.addAttribute("orders", orderService.getAllOrdersFromUser(user_.getUser_id()));
        return "orders-list";
    }
    @GetMapping("/active")
    public String listActiveOrders(Model model_, @ModelAttribute User user_) {
        model_.addAttribute("orders", orderService.getAllActiveOrdersFromUser(user_.getUser_id()));
        return "orders-list";
    }
    @GetMapping("/{id}")
    public String getOrder(Model model_, @ModelAttribute User user_, @PathVariable long id) {
        Order ord_ = orderService.getOrderById(id);
        if (ord_.getUser_id() == user_.getUser_id() ||
            user_.getRole().equals("staff") ||
            user_.getRole().equals("admin")) {
                model_.addAttribute("order", orderService.getOrderById(id));
                model_.addAttribute("menuItems", orderService.getMenuItemsFromOrder(id));
        }
        return "orders-details";
    }
    @GetMapping("/new")
    public String create(Model model_) {
        model_.addAttribute("order", new Order());
//        model_.addAttribute("menuItems", menuService) // -> Solve Later
        return "orders-create";
    }
    @PostMapping("/save")
    public String editForm(@ModelAttribute Order ord_) {
        orderService.saveOrder(ord_);
        return "orders-thankyou";
    }
    @GetMapping("/edit/{id}")
    public String editView(@PathVariable("id") long id, Model model_, @ModelAttribute User user_) {
        Order ord_ = orderService.getOrderById(user_.getUser_id());
        if (ord_.getUser_id() == user_.getUser_id() || user_.getRole().equals("staff") || user_.getRole().equals("admin")) {
            model_.addAttribute("order", orderService.getOrderById(user_.getUser_id()));
        }
        return "order-edit";
    }
    @PostMapping("/edit")
    public String edit(@ModelAttribute Order ord_) {
        orderService.updateOrder(ord_.getOrder_id(), ord_);
        return "redirect:/orders/";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders/";
    }
}
