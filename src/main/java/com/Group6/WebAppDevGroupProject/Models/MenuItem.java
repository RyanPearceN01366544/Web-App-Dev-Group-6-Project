package com.Group6.WebAppDevGroupProject.Models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "MenuItem")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer id;
    
    @Column(name = "item_name")
    private String name;
    
    @Column(name = "item_desc")
    private String description;
    
    @Column(name = "item_price")
    private BigDecimal price;
    
    @Column(name = "stock")
    private Integer stock;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "image_url")
    private String imageUrl;

    public MenuItem() {}

    public MenuItem(String name, String description, double price, Integer stock) {
        this.name = name;
        this.description = description;
        this.price = BigDecimal.valueOf(price);
        this.stock = stock;
    }

    public MenuItem(String name, String description, double price, Integer stock, String category, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = BigDecimal.valueOf(price);
        this.stock = stock;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
