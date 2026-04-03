package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Orders;

import java.util.List;

public interface OrdersDAO {
    void save(Orders orders);
    List<Orders> findAll();
    Orders findById(int id);
    void delete(int id);
    List<Orders> findByCustomerId(int id);

}
