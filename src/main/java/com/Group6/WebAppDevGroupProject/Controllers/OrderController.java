package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.MenuItem;
import com.Group6.WebAppDevGroupProject.Models.Order;
import com.Group6.WebAppDevGroupProject.Service.OrderService;
import com.Group6.WebAppDevGroupProject.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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


}
