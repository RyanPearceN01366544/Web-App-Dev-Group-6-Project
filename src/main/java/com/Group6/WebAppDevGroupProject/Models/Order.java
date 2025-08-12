package com.Group6.WebAppDevGroupProject.Models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "`Order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer order_id;

    @Column(name = "user_id", nullable = false)
    private Integer user_id;

    @Column(name = "order_items", nullable = false, columnDefinition = "TEXT")
    private String order_items;

    @Column(name = "order_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date order_time;

    @Column(name = "order_requested")
    @Temporal(TemporalType.TIMESTAMP)
    private Date order_requested;

    @Column(name = "order_status", nullable = false)
    private String order_status;

    // ----- Getters -----
    public Integer getOrder_id() { return order_id; }
    public Integer getUser_id() { return user_id; }
    public String getOrder_items() { return order_items; }
    public Date getOrder_time() { return order_time; }
    public Date getOrder_requested() { return order_requested; }
    public String getOrder_status() { return order_status; }

    // ----- Setters -----
    public void setOrder_id(Integer order_id) { this.order_id = order_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }
    public void setOrder_items(String order_items) { this.order_items = order_items; }
    public void setOrder_time(Date order_time) { this.order_time = order_time; }
    public void setOrder_requested(Date order_requested) { this.order_requested = order_requested; }
    public void setOrder_status(String order_status) { this.order_status = order_status; }
}
