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
    List<Sneakers> findByBrand(int id_brand);
    List<Sneakers> findByType(int id_type_sneakers);
    List<Sneakers> findByBrandType(int id_brand,int id_type_sneakers);
}
