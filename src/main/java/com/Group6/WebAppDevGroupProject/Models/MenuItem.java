package com.Group6.WebAppDevGroupProject.Models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String category;
    private String imageUrl;

    public MenuItem() {}

    public MenuItem(String name, String description, double price, int stock) {
        this.name = name;
        this.description = description;
        this.price = BigDecimal.valueOf(price);
        this.stock = stock;
    }

    public MenuItem(String name, String description, double price, int stock, String category, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = BigDecimal.valueOf(price);
        this.stock = stock;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
