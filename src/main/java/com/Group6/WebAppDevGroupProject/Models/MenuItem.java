package com.Group6.WebAppDevGroupProject.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int item_id;
    private String name;
    private String description;
    private float price;
    private int stock;

    // -- Basic Getters --
    public int getItem_id() { return item_id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public float getPrice() { return price; }
    public int getStock() { return stock; }

    // -- Basic Setters --
    public void setItem_id(int item_id) { this.item_id = item_id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(float price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
}