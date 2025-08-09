package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Service.MenuService;
import com.Group6.WebAppDevGroupProject.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders/api")
@CrossOrigin
public class OrderAdminController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;


}
