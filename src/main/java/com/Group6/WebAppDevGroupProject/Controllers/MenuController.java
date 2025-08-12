package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.MenuItem;
import com.Group6.WebAppDevGroupProject.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;

@Controller
@RequestMapping("/menu")
@CrossOrigin
public class MenuController {

    @Autowired
    private MenuService menuService;

    // GET all items
    @GetMapping
    public String showMenu(Model model, @SessionAttribute(value = "user", required = false) com.Group6.WebAppDevGroupProject.Models.User user) {
        List<MenuItem> menuItems = menuService.getAllMenuItems();
        model.addAttribute("menuItems", menuItems);
        if (user != null) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("role", user.getRole());
        }
        return "menu";
    }

    // GET item by id
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getItemById(@PathVariable int id) {
        return menuService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create new item
    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody MenuItem item) {
        // Basic validation
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name cannot be blank");
        }
        if (item.getPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body("Price cannot be negative");
        }
        if (item.getStock() < 0) {
            return ResponseEntity.badRequest().body("Stock cannot be negative");
        }
        MenuItem saved = menuService.save(item);
        return ResponseEntity.ok(saved);
    }

    // PUT update existing item
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable int id, @RequestBody MenuItem newItem) {
        return menuService.findById(id)
                .map(item -> {
                    if (newItem.getName() == null || newItem.getName().trim().isEmpty()) {
                        return ResponseEntity.badRequest().body("Name cannot be blank");
                    }
                    if (newItem.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                        return ResponseEntity.badRequest().body("Price must be positive");
                    }
                    if (newItem.getStock() < 0) {
                        return ResponseEntity.badRequest().body("Stock cannot be negative");
                    }
                    item.setName(newItem.getName());
                    item.setDescription(newItem.getDescription());
                    item.setPrice(newItem.getPrice());
                    item.setStock(newItem.getStock());
                    item.setCategory(newItem.getCategory());
                    item.setImageUrl(newItem.getImageUrl());
                    menuService.save(item);
                    return ResponseEntity.ok(item);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE item by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable int id) {
        if (!menuService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        menuService.deleteById(id);
        return ResponseEntity.ok().body("Menu item deleted successfully");
    }
}

