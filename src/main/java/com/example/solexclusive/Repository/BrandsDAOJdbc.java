package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Brands;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de BrandsDAO.
 * Realiza las operaciones CRUD de marcas directamente contra la base de datos
 * usando PreparedStatements para evitar inyección SQL.
 */
@Repository
@Qualifier("brandsDAOJdbc")
public class BrandsDAOJdbc implements BrandsDAO {

    // Obtiene la conexión activa desde el Singleton
    private Connection getConnection() {return Conexion.getInstancia().getConnection();}

    @Override
    public void addBrand(Brands b) {
        String sql = "INSERT INTO brands (name) VALUES (?)";
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setString(1, b.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteBrand(int id) {
        String sql = "DELETE FROM brands WHERE id_brand = ?";
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateBrand(Brands b) {
        String sql = "UPDATE brands SET name=? WHERE id_brand= ?";
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setString(1, b.getName());
            ps.setInt(2, b.getId_brand());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Brands findById(int id) {
        String sql = "SELECT * FROM brands WHERE id_brand = ?";
        Brands b = null;
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                b = this.mapBrands(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return b;
    }

    @Override
    public List<Brands> findAllBrands() {
        List<Brands> list = new ArrayList<>();
        String sql = "SELECT * FROM brands";
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(this.mapBrands(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    /**
     * Convierte una fila del ResultSet en un objeto Brands.
     */
    public Brands mapBrands(ResultSet rs) throws SQLException {
        Brands b = new Brands();
        b.setId_brand(rs.getInt("id_brand"));
        b.setName(rs.getString("name"));
        return b;
    }
}
