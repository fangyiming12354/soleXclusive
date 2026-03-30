package com.example.solexclusive.Model;

public class Stocks {
    private int id_stock,quantity;
    private double size;
    private Sneakers id_sneaker;
    private Brands id_brands;
    private  TypeSneakers id_type_sneakers;

    public Stocks() {
        this.id_sneaker = new Sneakers();
        this.id_brands = new Brands();
        this.id_type_sneakers = new TypeSneakers();
    }

    public Brands getId_brands() {
        return id_brands;
    }

    public void setId_brands(Brands id_brands) {
        this.id_brands = id_brands;
    }

    public TypeSneakers getId_type_sneakers() {
        return id_type_sneakers;
    }

    public void setId_type_sneakers(TypeSneakers id_type_sneakers) {
        this.id_type_sneakers = id_type_sneakers;
    }

    public int getId_stock() {
        return id_stock;
    }

    public void setId_stock(int id_stock) {
        this.id_stock = id_stock;
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

    public Sneakers getId_sneaker() {
        return id_sneaker;
    }

    public void setId_sneaker(Sneakers id_sneaker) {
        this.id_sneaker = id_sneaker;
    }
}
