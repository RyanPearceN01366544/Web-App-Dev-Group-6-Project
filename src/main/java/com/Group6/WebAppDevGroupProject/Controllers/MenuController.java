package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.MenuItem;
import com.Group6.WebAppDevGroupProject.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/menu")
@CrossOrigin
public class MenuController {
    @Autowired
    private MenuService menuService;

    // GET all items
    @GetMapping
    public List<MenuItem> getAllItems() {
        return menuService.getAllMenuItems();
    }

    // GET item by id
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getItemById(@PathVariable("id") long id) {
        return menuService.getMenuItemById(id)
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
        if (item.getPrice().doubleValue() < 0.0) {
            return ResponseEntity.badRequest().body("Price cannot be negative");
        }
        if (item.getStock() < 0) {
            return ResponseEntity.badRequest().body("Stock cannot be negative");
        }
        MenuItem saved = menuService.addMenuItem(item);
        return ResponseEntity.ok(saved);
    }

    // PUT update existing item
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable int id, @RequestBody MenuItem newItem) {
        return menuService.getMenuItemById(id)
                .map(item -> {
                    if (newItem.getName() == null || newItem.getName().trim().isEmpty()) {
                        return ResponseEntity.badRequest().body("Name cannot be blank");
                    }
                    if (newItem.getPrice().doubleValue() < 0.0) {
                        return ResponseEntity.badRequest().body("Price cannot be negative nor 0.0");
                    }
                    if (newItem.getStock() < 0) {
                        return ResponseEntity.badRequest().body("Stock cannot be negative");
                    }
                    item.setName(newItem.getName());
                    item.setDescription(newItem.getDescription());
                    item.setPrice(newItem.getPrice());
                    item.setStock(newItem.getStock());
                    menuService.addMenuItem(item);
                    return ResponseEntity.ok(item);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE item by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable int id) {
        if (menuService.getMenuItemById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        menuService.deleteMenuItem(id);
        return ResponseEntity.ok().body("Menu item deleted successfully");
    }
}

