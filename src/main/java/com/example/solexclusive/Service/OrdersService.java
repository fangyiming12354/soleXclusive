package com.example.solexclusive.Service;

import com.example.solexclusive.Model.Orders;
import com.example.solexclusive.Repository.OrdersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersService {
    private final OrdersDAO ordersDAO;
    @Autowired
    public OrdersService(@Qualifier("ordersDAOJdbc") OrdersDAO ordersDAO) {this.ordersDAO = ordersDAO;}

    public void save(Orders orders){ordersDAO.save(orders);}
    public Orders findById(int id){return ordersDAO.findById(id);}
    public void delete(int id){ordersDAO.delete(id);}
    public void update(Orders orders){ordersDAO.save(orders);}
    public List<Orders> findAll(){return ordersDAO.findAll();}
    public List<Orders> findByCustomerId(int id){return ordersDAO.findByCustomerId(id);}

}
