package com.example.solexclusive.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Orders {
    private int id_order;
    private Users id_user;
    private LocalDateTime date;
    private double total;

    private List<OrderItems> items;

    public Orders() {
        this.id_user = new Users();
        this.items = new ArrayList<>();
    }

    public List<OrderItems> getItems() {
        return items;
    }

    public void setItems(List<OrderItems> items) {
        this.items = items;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public Users getId_user() {
        return id_user;
    }

    public void setId_user(Users id_user) {
        this.id_user = id_user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    public double calculateTotal() {
        double total = 0;
        for(OrderItems item : items){
            total += item.getQuantity() * item.getUnit_price();
        }
        return total;
    }
}
