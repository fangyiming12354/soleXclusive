package com.example.solexclusive.Model;

public class OrderItems {
    private int id_item;
    private int quantity;
    private double size,unit_price;

    private Orders id_order;
    private Sneakers id_sneaker;

    public OrderItems() {
    }

    public Sneakers getId_sneaker() {
        return id_sneaker;
    }

    public void setId_sneaker(Sneakers id_sneaker) {
        this.id_sneaker = id_sneaker;
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }


    public Orders getId_order() {
        return id_order;
    }

    public void setId_order(Orders id_order) {
        this.id_order = id_order;
    }
}
