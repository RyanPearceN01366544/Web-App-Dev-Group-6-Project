package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.MenuItem;
import com.Group6.WebAppDevGroupProject.Repositories.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/menu")
@CrossOrigin
public class MenuItemController {

    @Autowired
    private MenuItemRepository menuItemRepository;

    // GET all items
    @GetMapping
    public List<MenuItem> getAllItems() {
        return menuItemRepository.findAll();
    }

    // GET item by id
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getItemById(@PathVariable int id) {
        return menuItemRepository.findById(id)
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
        if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.badRequest().body("Price cannot be negative");
        }
        if (item.getStock() < 0) {
            return ResponseEntity.badRequest().body("Stock cannot be negative");
        }
        MenuItem saved = menuItemRepository.save(item);
        return ResponseEntity.ok(saved);
    }

    // PUT update existing item
    @PutMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable int id, @RequestBody MenuItem newItem) {
        return menuItemRepository.findById(id)
                .map(item -> {
                    if (newItem.getName() == null || newItem.getName().trim().isEmpty()) {
                        return ResponseEntity.badRequest().body("Name cannot be blank");
                    }
                    if (newItem.getPrice() == null || newItem.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                        return ResponseEntity.badRequest().body("Price cannot be negative");
                    }
                    if (newItem.getStock() < 0) {
                        return ResponseEntity.badRequest().body("Stock cannot be negative");
                    }
                    item.setName(newItem.getName());
                    item.setDescription(newItem.getDescription());
                    item.setPrice(newItem.getPrice());
                    item.setStock(newItem.getStock());
                    menuItemRepository.save(item);
                    return ResponseEntity.ok(item);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE item by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable int id) {
        if (!menuItemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        menuItemRepository.deleteById(id);
        return ResponseEntity.ok().body("Menu item deleted successfully");
    }
}

