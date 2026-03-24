package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Brands;

import java.util.List;

public interface BrandsDAO {
    void addBrand(Brands b);
    void deleteBrand(int id);
    void updateBrand(Brands b);
    Brands findById(int id);
    List<Brands> findAllBrands();
}
