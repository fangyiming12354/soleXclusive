package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.OrderItems;
import com.example.solexclusive.Model.Orders;
import com.example.solexclusive.Model.Sneakers;
import com.example.solexclusive.Model.Users;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Qualifier("ordersDAOJdbc")
public class OrdersDAOJdbc implements OrdersDAO {

    private Connection getConnection() {return Conexion.getInstancia().getConnection();}

    @Override
    public void save(Orders orders) {
        String sqlOrder = "INSERT INTO orders (id_user, total) VALUES (?, ?)";
        String sqlItem = "INSERT INTO order_items (id_order, id_sneaker, quantity, unit_price, size) VALUES (?, ?, ?, ?, ?)";
        String sqlStock = "UPDATE stocks SET quantity = quantity - ? WHERE id_sneaker = ? AND size = ? AND quantity >= ?";

        try (Connection conn = this.getConnection()) {

            // ------------------- Insert order -------------------
            int idOrder;
            try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setInt(1, orders.getId_user().getId_user());
                psOrder.setDouble(2, orders.calculateTotal());
                psOrder.executeUpdate();

                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    if (rs.next()) {
                        idOrder = rs.getInt(1);
                        orders.setId_order(idOrder);
                    } else {
                        throw new SQLException("No se pudo generar el id del pedido");
                    }
                }
            }

            // ------------------- Insert order_items -------------------
            try (PreparedStatement psItem = conn.prepareStatement(sqlItem)) {
                for (OrderItems item : orders.getItems()) {
                    psItem.setInt(1, orders.getId_order());
                    psItem.setInt(2, item.getId_sneaker().getId_sneaker());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setDouble(4, item.getUnit_price());
                    psItem.setDouble(5, item.getSize());
                    psItem.executeUpdate();
                }
            }

            // ------------------- Update stocks -------------------
            try (PreparedStatement psStock = conn.prepareStatement(sqlStock)) {
                for (OrderItems item : orders.getItems()) {
                    psStock.setInt(1, item.getQuantity());
                    psStock.setInt(2, item.getId_sneaker().getId_sneaker());
                    psStock.setDouble(3, item.getSize());
                    psStock.setInt(4, item.getQuantity());
                    psStock.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Orders> findAll() {
        List<Orders> pedidos = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS user_name FROM orders o " +
                "JOIN users u ON o.id_user = u.id_user " +
                "ORDER BY o.date DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Orders order = new Orders();
                order.setId_order(rs.getInt("id_order"));

                Users user = new Users();
                user.setId_user(rs.getInt("id_user"));
                user.setName(rs.getString("user_name"));
                order.setId_user(user);

                order.setDate(rs.getTimestamp("date").toLocalDateTime());
                order.setTotal(rs.getDouble("total"));

                // No cargamos los items aquí
                pedidos.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pedidos;
    }

    @Override
    public Orders findById(int id) {
        Orders order = null;
        String sqlOrder = "SELECT o.*, u.name AS user_name FROM orders o " +
                "JOIN users u ON o.id_user = u.id_user " +
                "WHERE o.id_order = ?";
        String sqlItems = "SELECT oi.*, s.name AS sneaker_name FROM order_items oi " +
                "JOIN sneakers s ON oi.id_sneaker = s.id_sneaker " +
                "WHERE oi.id_order = ?";

        try (Connection conn = getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlOrder)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    order = new Orders();
                    order.setId_order(rs.getInt("id_order"));
                    Users user = new Users();
                    user.setId_user(rs.getInt("id_user"));
                    user.setName(rs.getString("user_name"));
                    order.setId_user(user);
                    order.setDate(rs.getTimestamp("date").toLocalDateTime());
                    order.setTotal(rs.getDouble("total"));
                }
            }

            if (order != null) {
                try (PreparedStatement psItems = conn.prepareStatement(sqlItems)) {
                    psItems.setInt(1, id);
                    ResultSet rsItems = psItems.executeQuery();
                    List<OrderItems> items = new ArrayList<>();
                    while (rsItems.next()) {
                        OrderItems item = new OrderItems();
                        item.setId_item(rsItems.getInt("id_item"));
                        item.setId_order(order);
                        Sneakers sneaker = new Sneakers();
                        sneaker.setId_sneaker(rsItems.getInt("id_sneaker"));
                        sneaker.setName(rsItems.getString("sneaker_name"));
                        item.setId_sneaker(sneaker);
                        item.setQuantity(rsItems.getInt("quantity"));
                        item.setUnit_price(rsItems.getDouble("unit_price"));
                        item.setSize(rsItems.getDouble("size"));
                        items.add(item);
                    }
                    order.setItems(items);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return order;
    }

    @Override
    public void delete(int id) {
        String sqlRestoreStock = "UPDATE stocks s JOIN order_items oi ON s.id_sneaker = oi.id_sneaker AND s.size = oi.size " +
                "SET s.quantity = s.quantity + oi.quantity WHERE oi.id_order = ?";
        String sqlDeleteItems = "DELETE FROM order_items WHERE id_order = ?";
        String sqlDeleteOrder = "DELETE FROM orders WHERE id_order = ?";

        try (Connection conn = this.getConnection()) {

            // Restaurar stock
            try (PreparedStatement psStock = conn.prepareStatement(sqlRestoreStock)) {
                psStock.setInt(1, id);
                psStock.executeUpdate();
            }

            // Borrar items
            try (PreparedStatement psItems = conn.prepareStatement(sqlDeleteItems)) {
                psItems.setInt(1, id);
                psItems.executeUpdate();
            }

            // Borrar pedido
            try (PreparedStatement psOrder = conn.prepareStatement(sqlDeleteOrder)) {
                psOrder.setInt(1, id);
                psOrder.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Orders> findByCustomerId(int id) {
        List<Orders> pedidos = new ArrayList<>();
        String sql =  "SELECT o.*, u.name AS user_name FROM orders o " +
                "JOIN users u ON o.id_user = u.id_user " +
                "WHERE o.id_user = ? ORDER BY o.date DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Orders order = new Orders();
                order.setId_order(rs.getInt("id_order"));
                order.setDate(rs.getTimestamp("date").toLocalDateTime());
                order.setTotal(rs.getDouble("total"));
                Users user = new Users();
                user.setId_user(rs.getInt("id_user"));
                user.setName(rs.getString("user_name"));
                order.setId_user(user);
                pedidos.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pedidos;
    }
}

