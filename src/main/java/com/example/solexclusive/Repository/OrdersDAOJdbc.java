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

/**
 * Implementación JDBC de OrdersDAO.
 * El método save realiza tres operaciones en secuencia:
 *   1. Inserta el pedido en 'orders' y obtiene el id generado.
 *   2. Inserta cada línea en 'order_items'.
 *   3. Descuenta la cantidad del stock correspondiente.
 *
 * El método delete hace lo inverso: restaura el stock antes de borrar.
 */
@Repository
@Qualifier("ordersDAOJdbc")
public class OrdersDAOJdbc implements OrdersDAO {

    private Connection getConnection() {return Conexion.getInstancia().getConnection();}

    /**
     * Guarda un pedido completo en la base de datos.
     * Inserta el pedido, sus líneas y descuenta el stock en una misma conexión.
     */
    @Override
    public void save(Orders orders) {
        String sqlOrder = "INSERT INTO orders (id_user, total) VALUES (?, ?)";
        String sqlItem = "INSERT INTO order_items (id_order, id_sneaker, quantity, unit_price, size) VALUES (?, ?, ?, ?, ?)";
        String sqlStock = "UPDATE stocks SET quantity = quantity - ? WHERE id_sneaker = ? AND size = ? AND quantity >= ?";

        try (Connection conn = this.getConnection()) {

            // ------------------- Insertar pedido y obtener su id generado -------------------
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

            // ------------------- Insertar las líneas del pedido -------------------
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

            // ------------------- Descontar el stock de cada zapatilla comprada -------------------
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

    /**
     * Devuelve todos los pedidos con el nombre del usuario.
     * No carga las líneas (order_items) para optimizar el rendimiento en el listado.
     */
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

                // No cargamos los items aquí para no sobrecargar el listado
                pedidos.add(order);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pedidos;
    }

    /**
     * Devuelve un pedido completo con todas sus líneas de productos.
     * Realiza dos consultas: una para el pedido y otra para sus items.
     */
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
            // Primera consulta: datos del pedido
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

            // Segunda consulta: líneas del pedido (solo si el pedido existe)
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

    /**
     * Elimina un pedido restaurando primero el stock de las zapatillas,
     * luego borrando las líneas y finalmente el pedido.
     */
    @Override
    public void delete(int id) {
        String sqlRestoreStock = "UPDATE stocks s JOIN order_items oi ON s.id_sneaker = oi.id_sneaker AND s.size = oi.size " +
                "SET s.quantity = s.quantity + oi.quantity WHERE oi.id_order = ?";
        String sqlDeleteItems = "DELETE FROM order_items WHERE id_order = ?";
        String sqlDeleteOrder = "DELETE FROM orders WHERE id_order = ?";

        try (Connection conn = this.getConnection()) {

            // Paso 1: Restaurar el stock de las zapatillas del pedido
            try (PreparedStatement psStock = conn.prepareStatement(sqlRestoreStock)) {
                psStock.setInt(1, id);
                psStock.executeUpdate();
            }

            // Paso 2: Borrar las líneas del pedido
            try (PreparedStatement psItems = conn.prepareStatement(sqlDeleteItems)) {
                psItems.setInt(1, id);
                psItems.executeUpdate();
            }

            // Paso 3: Borrar el pedido principal
            try (PreparedStatement psOrder = conn.prepareStatement(sqlDeleteOrder)) {
                psOrder.setInt(1, id);
                psOrder.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Devuelve los pedidos de un cliente concreto, ordenados del más reciente al más antiguo.
     * No carga las líneas para optimizar el rendimiento en el historial.
     */
    @Override
    public List<Orders> findByCustomerId(int id) {
        List<Orders> pedidos = new ArrayList<>();
        String sql = "SELECT o.*, u.name AS user_name FROM orders o " +
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
