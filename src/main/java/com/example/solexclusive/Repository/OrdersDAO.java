package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Orders;

import java.util.List;

/**
 * Interfaz DAO para la entidad Orders.
 * Define las operaciones disponibles para gestionar pedidos.
 */
public interface OrdersDAO {
    /**
     * Guarda un pedido completo: inserta en orders, en order_items
     * y descuenta el stock de cada zapatilla comprada.
     */
    void save(Orders orders);
    // Devuelve todos los pedidos (para el panel de administración)
    List<Orders> findAll();
    // Busca un pedido por su id, incluyendo sus líneas de productos
    Orders findById(int id);
    /**
     * Elimina un pedido y sus líneas, restaurando el stock de las zapatillas.
     */
    void delete(int id);
    // Devuelve los pedidos de un cliente específico ordenados por fecha descendente
    List<Orders> findByCustomerId(int id);
}
