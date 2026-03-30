package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Brands;
import com.example.solexclusive.Model.Sneakers;
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
@Qualifier("sneakersDAOJdbc")
public class SneakersDAOJdbc implements SneakersDAO {

    private Connection getConnection() {return Conexion.getInstancia().getConnection();}

    @Override
    public void save(Sneakers sneaker) {
        String sql="INSERT INTO sneakers(name,description,price,filePath,id_brand,id_type_sneaker) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement ps=this.getConnection().prepareStatement(sql);
            ps.setString(1,sneaker.getName());
            ps.setString(2,sneaker.getDescription());
            ps.setDouble(3,sneaker.getPrice());
            ps.setString(4,sneaker.getFilePath());
            ps.setInt(5,sneaker.getId_brands().getId_brand());
            ps.setInt(6,sneaker.getId_type_sneakers().getId_type_sneakers());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Sneakers sneaker) {
        String sql="UPDATE sneakers set name=?,description=?,price=?,filePath=?,id_brand=?,id_type_sneaker=? where id_sneaker=?";
        try {
            PreparedStatement ps=this.getConnection().prepareStatement(sql);
            ps.setString(1,sneaker.getName());
            ps.setString(2,sneaker.getDescription());
            ps.setDouble(3,sneaker.getPrice());
            ps.setString(4,sneaker.getFilePath());
            ps.setInt(5,sneaker.getId_brands().getId_brand());
            ps.setInt(6,sneaker.getId_type_sneakers().getId_type_sneakers());
            ps.setInt(7,sneaker.getId_sneaker());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(int id) {
        String sql="DELETE FROM sneakers where id_sneaker=?";
        try {
            PreparedStatement ps=this.getConnection().prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Sneakers findById(int id) {
        String sql="SELECT s.id_sneaker, s.name, s.description, s.price, s.filePath, b.id_brand, b.name AS brand_name, t.id_type_sneaker, t.name AS type_name FROM sneakers s JOIN brands b ON s.id_brand = b.id_brand JOIN type_sneakers t ON s.id_type_sneaker = t.id_type_sneaker WHERE s.id_sneaker = ?";
        Sneakers sneaker=null;
        try {
            PreparedStatement ps=this.getConnection().prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                sneaker=this.mapSneakers(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sneaker;
    }

    @Override
    public List<Sneakers> findAll() {
        List<Sneakers> sneaker=new ArrayList<Sneakers>();
        String sql="SELECT s.id_sneaker,s.name,s.description,s.price,s.filePath,b.id_brand,b.name brand_name,t.id_type_sneaker,t.name type_name FROM sneakers s JOIN brands b ON s.id_brand=b.id_brand JOIN type_sneakers t on s.id_type_sneaker=t.id_type_sneaker";
        try {
            PreparedStatement ps=this.getConnection().prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                sneaker.add(this.mapSneakers(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sneaker;
    }

    @Override
    public List<Sneakers> findByBrand(int id_brand) {
        List<Sneakers> sneaker=new ArrayList<Sneakers>();
        String sql = "SELECT s.id_sneaker,s.name,s.description,s.price,s.filePath,b.id_brand,b.name brand_name,t.id_type_sneaker,t.name type_name FROM sneakers s JOIN brands b ON s.id_brand=b.id_brand JOIN type_sneakers t on s.id_type_sneaker=t.id_type_sneaker where s.id_brand=?";
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setInt(1, id_brand);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sneaker.add(this.mapSneakers(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sneaker;
    }

    @Override
    public List<Sneakers> findByType(int id_type_sneakers) {
        List<Sneakers> sneaker=new ArrayList<Sneakers>();
        String sql = "SELECT s.id_sneaker,s.name,s.description,s.price,s.filePath,b.id_brand,b.name brand_name,t.id_type_sneaker,t.name type_name FROM sneakers s JOIN brands b ON s.id_brand=b.id_brand JOIN type_sneakers t on s.id_type_sneaker=t.id_type_sneaker where t.id_type_sneaker=?";
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setInt(1,id_type_sneakers );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sneaker.add(this.mapSneakers(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return sneaker;
    }

    @Override
    public List<Sneakers> findByBrandType(int id_brand,int id_type_sneakers) {
        List<Sneakers> sneaker=new ArrayList<Sneakers>();
        String sql = "SELECT s.id_sneaker,s.name,s.description,s.price,s.filePath,b.id_brand,b.name brand_name,t.id_type_sneaker,t.name type_name FROM sneakers s JOIN brands b ON s.id_brand=b.id_brand JOIN type_sneakers t on s.id_type_sneaker=t.id_type_sneaker where s.id_brand=? and t.id_type_sneaker=?";
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setInt(1, id_brand);
            ps.setInt(2, id_type_sneakers);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sneaker.add(this.mapSneakers(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return sneaker;
    }

    private Sneakers mapSneakers(ResultSet rs) throws SQLException {
        Sneakers sneakers = new Sneakers();
        sneakers.setId_sneaker(rs.getInt("id_sneaker"));
        sneakers.setName(rs.getString("name"));
        sneakers.setDescription(rs.getString("description"));
        sneakers.setPrice(rs.getDouble("price"));
        sneakers.setFilePath(rs.getString("filePath"));
        Brands brand = new Brands();
        brand.setId_brand(rs.getInt("id_brand"));
        brand.setName(rs.getString("brand_name"));
        sneakers.setId_brands(brand);
        TypeSneakers type_sneaker = new TypeSneakers();
        type_sneaker.setId_type_sneakers(rs.getInt("id_type_sneaker"));
        type_sneaker.setName(rs.getString("type_name"));
        sneakers.setId_type_sneakers(type_sneaker);
        return sneakers;
    }

}
