package com.Group6.WebAppDevGroupProject.Service;

import com.Group6.WebAppDevGroupProject.Models.MenuItem;
import com.Group6.WebAppDevGroupProject.Repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuitemService {
    // Seed menu with common items
    public void seedMenuItems() {
        if (menuRepository.count() == 0) {
            menuRepository.save(new MenuItem("Burger", "Beef patty, bun, lettuce, tomato", 5.99, 50));
            menuRepository.save(new MenuItem("Hotdog", "Grilled hotdog with bun", 3.99, 50));
            menuRepository.save(new MenuItem("Pizza", "Cheese pizza slice", 4.49, 40));
            menuRepository.save(new MenuItem("Fries", "Crispy french fries", 2.99, 100));
            menuRepository.save(new MenuItem("Juice", "Assorted fruit juices", 1.99, 60));
            menuRepository.save(new MenuItem("Milk", "Carton of milk", 1.49, 60));
            menuRepository.save(new MenuItem("Fruit Cup", "Mixed fresh fruit", 2.49, 40));
            menuRepository.save(new MenuItem("Chicken Sandwich", "Grilled chicken, bun, lettuce", 5.49, 40));
            menuRepository.save(new MenuItem("Salad", "Fresh garden salad", 3.99, 30));
            menuRepository.save(new MenuItem("Yogurt", "Fruit yogurt cup", 1.99, 30));
            menuRepository.save(new MenuItem("Soup", "Daily soup selection", 2.99, 20));
            menuRepository.save(new MenuItem("Egg Salad Sandwich", "Egg salad, bread", 3.49, 20));
            menuRepository.save(new MenuItem("Grilled Cheese", "Grilled cheese sandwich", 3.99, 25));
            menuRepository.save(new MenuItem("Veggie Wrap", "Mixed veggies, tortilla", 4.49, 20));
            menuRepository.save(new MenuItem("Apple", "Fresh apple", 0.99, 40));
            menuRepository.save(new MenuItem("Banana", "Fresh banana", 0.99, 40));
        }
    }
    @Autowired
    private MenuRepository menuRepository;

    public List<MenuItem> getAllMenuItems() {
        return menuRepository.findAll();
    }

    public Optional<MenuItem> getMenuItemById(Integer id) {
        return menuRepository.findById(id);
    }

    public MenuItem addMenuItem(MenuItem item) {
        return menuRepository.save(item);
    }

    public MenuItem updateMenuItem(Integer id, MenuItem item) {
        return menuRepository.findById(id)
                .map(existing -> {
                    existing.setName(item.getName());
                    existing.setDescription(item.getDescription());
                    existing.setPrice(item.getPrice());
                    existing.setStock(item.getStock());
                    return menuRepository.save(existing);
                })
                .orElse(null);
    }

    public void deleteMenuItem(Integer id) {
        menuRepository.deleteById(id);
    }
}
