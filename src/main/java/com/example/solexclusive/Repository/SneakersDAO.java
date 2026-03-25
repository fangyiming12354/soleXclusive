package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Brands;
import com.example.solexclusive.Model.Sneakers;

import java.util.List;

public interface SneakersDAO {
    void save(Sneakers sneaker);
    void update(Sneakers sneaker);
    void delete(int id);
    Sneakers findById(int id);
    List<Sneakers> findAll();
    Sneakers findByBrand(Brands id);
}
