package com.example.solexclusive.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo que representa un pedido realizado por un usuario.
 * Corresponde a la tabla 'orders' de la base de datos.
 * Contiene la lista de líneas del pedido (OrderItems) y el total calculado.
 */
public class Orders {
    // Identificador único del pedido
    private int id_order;
    // Usuario que realizó el pedido
    private Users id_user;
    // Fecha y hora en que se realizó el pedido
    private LocalDateTime date;
    // Total del pedido en euros
    private double total;
    // Lista de productos incluidos en el pedido
    private List<OrderItems> items;

    public Orders() {
        // Inicializar objetos relacionados para evitar NullPointerException
        this.id_user = new Users();
        this.items = new ArrayList<>();
    }

    public List<OrderItems> getItems() {
        return items;
    }

    public void setItems(List<OrderItems> items) {
        this.items = items;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public Users getId_user() {
        return id_user;
    }

    public void setId_user(Users id_user) {
        this.id_user = id_user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Calcula el total del pedido sumando (cantidad * precio_unitario) de cada línea.
     * Se usa antes de guardar el pedido en la base de datos.
     */
    public double calculateTotal() {
        double total = 0;
        for (OrderItems item : items) {
            total += item.getQuantity() * item.getUnit_price();
        }
        return total;
    }
}
