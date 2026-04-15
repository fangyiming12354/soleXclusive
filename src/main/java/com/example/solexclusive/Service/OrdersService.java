package com.example.solexclusive.Service;

import com.example.solexclusive.Model.Orders;
import com.example.solexclusive.Repository.OrdersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión de pedidos.
 * Actúa como capa intermedia entre el controlador y el repositorio,
 * inyectando la implementación JDBC de OrdersDAO.
 */
@Service
public class OrdersService {
    private final OrdersDAO ordersDAO;

    @Autowired
    public OrdersService(@Qualifier("ordersDAOJdbc") OrdersDAO ordersDAO) {this.ordersDAO = ordersDAO;}

    // Guarda un pedido completo (inserta pedido, líneas y descuenta stock)
    public void save(Orders orders) {ordersDAO.save(orders);}
    public Orders findById(int id) {return ordersDAO.findById(id);}
    // Elimina un pedido restaurando el stock previamente
    public void delete(int id) {ordersDAO.delete(id);}
    // update llama a save porque el pedido se regenera completo
    public void update(Orders orders) {ordersDAO.save(orders);}
    // Devuelve todos los pedidos (panel de administración)
    public List<Orders> findAll() {return ordersDAO.findAll();}
    // Devuelve los pedidos de un cliente concreto (historial de compras)
    public List<Orders> findByCustomerId(int id) {return ordersDAO.findByCustomerId(id);}
}
