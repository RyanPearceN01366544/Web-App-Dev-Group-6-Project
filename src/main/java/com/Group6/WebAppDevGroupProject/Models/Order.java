package com.Group6.WebAppDevGroupProject.Models;

import jakarta.persistence.*;
import java.util.Date;

@Entity(name = "Orders")
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long order_id;
    private int user_id;
    private String order_items;
    @Temporal(TemporalType.TIMESTAMP)
    private Date order_time;
    @Temporal(TemporalType.TIMESTAMP)
    private Date order_requested;
    private String order_status;

    // -- Basic Getters --
    public long getOrder_id() { return order_id; }
    public int getUser_id() { return user_id; }
    public String getOrder_items() { return order_items; }
    public Date getOrder_time() { return order_time; }
    public Date getOrder_requested() { return order_requested; }
    public String getOrder_status() { return order_status; }

    // -- Basic Setters --
    public void setOrder_id(long order_id) { this.order_id = order_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }
    public void setOrder_items(String order_items) { this.order_items = order_items; }
    public void setOrder_time(Date order_time) { this.order_time = order_time; }
    public void setOrder_requested(Date order_requested) { this.order_requested = order_requested; }
    public void setOrder_status(String order_status) { this.order_status = order_status; }
}
