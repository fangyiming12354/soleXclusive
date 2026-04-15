package com.example.solexclusive.Model;

/**
 * Modelo que representa una línea de un pedido (un producto dentro de un pedido).
 * Corresponde a la tabla 'order_items' de la base de datos.
 * Cada línea indica qué zapatilla se compró, en qué talla, cuántas unidades y a qué precio.
 */
public class OrderItems {
    // Identificador único de la línea del pedido
    private int id_item;
    // Cantidad de unidades compradas
    private int quantity;
    // Talla de la zapatilla comprada
    private double size;
    // Precio unitario en el momento de la compra (histórico)
    private double unit_price;
    // Pedido al que pertenece esta línea
    private Orders id_order;
    // Zapatilla comprada
    private Sneakers id_sneaker;

    public OrderItems() {
    }

    public Sneakers getId_sneaker() {
        return id_sneaker;
    }

    public void setId_sneaker(Sneakers id_sneaker) {
        this.id_sneaker = id_sneaker;
    }

    public int getId_item() {
        return id_item;
    }

    public void setId_item(int id_item) {
        this.id_item = id_item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public Orders getId_order() {
        return id_order;
    }

    public void setId_order(Orders id_order) {
        this.id_order = id_order;
    }
}
