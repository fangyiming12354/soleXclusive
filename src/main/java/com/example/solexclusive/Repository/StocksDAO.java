package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Stocks;

import java.util.List;

public interface StocksDAO {
    void add(Stocks stocks);
    void delete(int id_stock);
    void update(Stocks stocks);
    Stocks findById(int id_stock);
    List<Stocks> findAll();
    List<Stocks> findByBrandId(int id_brand);
    List<Stocks> findByBrandType(int id_brand,int id_type_sneakers);
    List<Stocks> findByType(int id_type_sneakers);

}
