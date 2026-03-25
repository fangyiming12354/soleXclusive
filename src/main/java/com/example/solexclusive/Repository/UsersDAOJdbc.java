package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Users;
import java.sql.Connection;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
@Qualifier("usersDAOJdbc")
public class UsersDAOJdbc implements UsersDAO {
    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    @Override
    public void save(Users u) {
        String sql = "INSERT INTO users (name, password, email) VALUES (?,?,?)";
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setString(1, u.getName());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id_user=?";
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Users u, boolean isAdminEditing) {
        String sql;
        if (isAdminEditing) {
            // Admin puede actualizar también el rol
            sql = "UPDATE users SET name=?, password=?, email=?, id_type=? WHERE id_user=?";
        } else {
            // Customer solo puede actualizar nombre, email y password
            sql = "UPDATE users SET name=?, password=?, email=? WHERE id_user=?";
        }

        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setString(1, u.getName());
            ps.setString(2, u.getPassword()); // aquí va texto plano, o hash si quieres seguridad
            ps.setString(3, u.getEmail());

            if (isAdminEditing) {
                int idType = u.getType_user().equalsIgnoreCase("admin") ? 2 : 1;
                ps.setInt(4, idType);
                ps.setInt(5, u.getId_user());
            } else {
                ps.setInt(4, u.getId_user());
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Users findById(int id) {
        String sql = "SELECT u.id_user, u.name, u.email, u.password, t.name AS type_user " +
                "FROM users u " +
                "JOIN type_users t ON u.id_type = t.id_type " +
                "WHERE u.id_user = ?";

        Users u = null;
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u = this.mapUsers(rs); // mapUsers ahora también asigna type_user
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return u;
    }

    @Override
    public List<Users> findAll() {
        List<Users> list = new ArrayList<>();
        String sql = "SELECT u.id_user, u.name, u.email, u.password, t.name AS type_user " +
                "FROM users u " +
                "JOIN type_users t ON u.id_type = t.id_type";

        Conexion conexion = Conexion.getInstancia();
        try {
            PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(this.mapUsers(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Users mapUsers(ResultSet rs) throws SQLException {
        Users u = new Users();
        u.setId_user(rs.getInt("id_user"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password"));
        u.setType_user(rs.getString("type_user")); // guardamos el nombre del rol como String
        return u;
    }

    @Override
    public Users login(String email, String password) {
        String sql = "SELECT u.id_user, u.name, u.email, u.password, t.name AS type_user " +
                "FROM users u " +
                "JOIN type_users t ON u.id_type = t.id_type " +
                "WHERE u.email = ? AND u.password = ?";

        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password); // directamente texto plano
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Users u = new Users();
                u.setId_user(rs.getInt("id_user"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setType_user(rs.getString("type_user")); // el rol real
                return u;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // login fallido
    }
}
