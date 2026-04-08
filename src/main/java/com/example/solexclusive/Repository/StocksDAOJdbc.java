package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Brands;
import com.example.solexclusive.Model.Sneakers;
import com.example.solexclusive.Model.Stocks;
import com.example.solexclusive.Model.TypeSneakers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
@Qualifier("stocksDAOJdbc")
public class StocksDAOJdbc implements StocksDAO {
    private Connection getConnection() {return Conexion.getInstancia().getConnection();}
    @Override
    public void add(Stocks stocks) {
        String sql = "insert into stocks (size,quantity,id_sneaker) values (?,?,?)";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setDouble(1, stocks.getSize());
            ps.setInt(2, stocks.getQuantity());
            ps.setInt(3, stocks.getId_sneaker().getId_sneaker());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id_stock) {
        String sql = "delete from stocks where id_stock=?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setInt(1, id_stock);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Stocks stocks) {
        String sql = "update stocks set size=?,quantity=?,id_sneaker=? where id_stock=?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setDouble(1, stocks.getSize());
            ps.setInt(2, stocks.getQuantity());
            ps.setInt(3, stocks.getId_sneaker().getId_sneaker());
            ps.setInt(4, stocks.getId_stock());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stocks findById(int id_stock) {
        String sql = "select st.id_stock,st.size,st.quantity,s.id_sneaker,s.name sneaker_name ,s.description sneaker_description,s.price sneaker_price," +
                "s.filePath sneaker_filePath,b.id_brand,b.name brand_name,t.id_type_sneaker,t.name type_name from stocks st join sneakers s on st.id_sneaker = s.id_sneaker " +
                "join brands b on s.id_brand = b.id_brand join type_sneakers t on s.id_type_sneaker = t.id_type_sneaker where st.id_stock=?";
        Stocks stocks = null;
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setInt(1, id_stock);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                stocks = mapStocks(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stocks;
    }

    @Override
    public List<Stocks> findAll() {
        String sql = "select st.id_stock,st.size,st.quantity,s.id_sneaker,s.name sneaker_name ,s.description sneaker_description,s.price sneaker_price," +
                "s.filePath sneaker_filePath,b.id_brand,b.name brand_name,t.id_type_sneaker,t.name type_name from stocks st join sneakers s on st.id_sneaker = s.id_sneaker " +
                "join brands b on s.id_brand = b.id_brand join type_sneakers t on s.id_type_sneaker = t.id_type_sneaker";
        List<Stocks> stocks = new ArrayList<>();
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stocks.add(mapStocks(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stocks;
    }

    @Override
    public List<Stocks> findByBrandId(int id_brand) {
        String sql = "select st.id_stock,st.size,st.quantity,s.id_sneaker,s.name sneaker_name ,s.description sneaker_description,s.price sneaker_price," +
                "s.filePath sneaker_filePath,b.id_brand,b.name brand_name,t.id_type_sneaker,t.name type_name from stocks st join sneakers s on st.id_sneaker = s.id_sneaker " +
                "join brands b on s.id_brand = b.id_brand join type_sneakers t on s.id_type_sneaker = t.id_type_sneaker where b.id_brand=?";
         List<Stocks> stocks = new ArrayList<>();
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setInt(1, id_brand);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stocks.add(mapStocks(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stocks;
    }

    @Override
    public List<Stocks> findByBrandType(int id_brand, int id_type_sneakers) {
        String sql = "select st.id_stock,st.size,st.quantity,s.id_sneaker,s.name sneaker_name ,s.description sneaker_description,s.price sneaker_price," +
                "s.filePath sneaker_filePath,b.id_brand,b.name brand_name,t.id_type_sneaker,t.name type_name from stocks st join sneakers s on st.id_sneaker = s.id_sneaker " +
                "join brands b on s.id_brand = b.id_brand join type_sneakers t on s.id_type_sneaker = t.id_type_sneaker where b.id_brand=? and t.id_type_sneaker=?";
        List<Stocks> stocks = new ArrayList<>();
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setInt(1, id_brand);
            ps.setInt(2, id_type_sneakers);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stocks.add(mapStocks(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return stocks;
    }

    @Override
    public List<Stocks> findByType(int id_type_sneakers) {
        String sql = "select st.id_stock,st.size,st.quantity,s.id_sneaker,s.name sneaker_name ,s.description sneaker_description,s.price sneaker_price," +
                "s.filePath sneaker_filePath,b.id_brand,b.name brand_name,t.id_type_sneaker,t.name type_name from stocks st join sneakers s on st.id_sneaker = s.id_sneaker " +
                "join brands b on s.id_brand = b.id_brand join type_sneakers t on s.id_type_sneaker = t.id_type_sneaker where b.id_brand=?";
        List<Stocks> stocks = new ArrayList<>();
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setInt(1, id_type_sneakers);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stocks.add(mapStocks(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stocks;
    }

    @Override
    public List<Stocks> findBySneakerId(int id_sneaker) {
        String sql = "select st.id_stock,st.size,st.quantity,s.id_sneaker,s.name sneaker_name,s.description sneaker_description,s.price sneaker_price," +
                "s.filePath sneaker_filePath,b.id_brand,b.name brand_name,t.id_type_sneaker,t.name type_name from stocks st join sneakers s on st.id_sneaker = s.id_sneaker " +
                "join brands b on s.id_brand = b.id_brand join type_sneakers t on s.id_type_sneaker = t.id_type_sneaker where s.id_sneaker=?";
        List<Stocks> stocks = new ArrayList<>();
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setInt(1, id_sneaker);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                stocks.add(mapStocks(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stocks;
    }

    public Stocks mapStocks(ResultSet rs) throws SQLException {
        Stocks stocks = new Stocks();
        stocks.setId_stock(rs.getInt("id_stock"));
        stocks.setSize(rs.getDouble("size"));
        stocks.setQuantity(rs.getInt("quantity"));
        Sneakers sneakers = new Sneakers();
        sneakers.setId_sneaker(rs.getInt("id_sneaker"));
        sneakers.setName(rs.getString("sneaker_name"));
        sneakers.setDescription(rs.getString("sneaker_description"));
        sneakers.setPrice(rs.getDouble("sneaker_price"));
        sneakers.setFilePath(rs.getString("sneaker_filePath"));
        stocks.setId_sneaker(sneakers);
        Brands brand = new Brands();
        brand.setId_brand(rs.getInt("id_brand"));
        brand.setName(rs.getString("brand_name"));
        sneakers.setId_brands(brand);
        TypeSneakers type_sneaker = new TypeSneakers();
        type_sneaker.setId_type_sneakers(rs.getInt("id_type_sneaker"));
        type_sneaker.setName(rs.getString("type_name"));
        sneakers.setId_type_sneakers(type_sneaker);
        return stocks;
    }
}
