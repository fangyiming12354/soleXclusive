package com.example.solexclusive.Model;

public class Sneakers {
    private int id_sneaker;
    private String name,description,filePath;
    private double price;
    private Brands id_brands;

    public Sneakers() {
        this.id_brands = id_brands;
    }

    public int getId_sneaker() {
        return id_sneaker;
    }

    public void setId_sneaker(int id_sneaker) {
        this.id_sneaker = id_sneaker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Brands getId_brands() {
        return id_brands;
    }

    public void setId_brands(Brands id_brands) {
        this.id_brands = id_brands;
    }
}
