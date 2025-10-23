package com.example.Ecom.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    private Long id;
    private double totalAmount;
    private String status;
    private LocalDateTime date;

    private String username;
    private String email;
    private List<OrderItemDTO> orderItems;

    public OrderDTO(Long id, double totalAmount, String status, LocalDateTime date, String username, String email, List<OrderItemDTO> orderItems) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.status = status;
        this.date = date;
        this.username = username;
        this.email = email;
        this.orderItems = orderItems;
    }

    public OrderDTO(Long id, double totalAmount, String status, LocalDateTime date, List<OrderItemDTO> orderItems) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.status = status;
        this.date = date;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
