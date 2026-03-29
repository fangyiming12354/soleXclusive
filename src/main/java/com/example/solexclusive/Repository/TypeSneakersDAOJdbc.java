package com.example.solexclusive.Repository;

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
@Qualifier("typeSneakersDAOJdbc")
public class TypeSneakersDAOJdbc implements TypeSneakersDAO {

    private Connection getConnection() {return Conexion.getInstancia().getConnection();}

    @Override
    public List<TypeSneakers> findAll() {
        List<TypeSneakers> list = new ArrayList<>();
        String sql = "SELECT * FROM type_sneakers";
        try {
            PreparedStatement ps =this.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapTypeSneakers(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public TypeSneakers findById(int id) {
        String sql = "SELECT * FROM type_sneakers WHERE id_type_sneaker = ?";
        TypeSneakers typeSneakers = null;
        try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                typeSneakers = mapTypeSneakers(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return typeSneakers;
    }


    public TypeSneakers mapTypeSneakers(ResultSet rs) throws SQLException {
        TypeSneakers sneakers = new TypeSneakers();
        sneakers.setId_type_sneakers(rs.getInt("id_type_sneaker"));
        sneakers.setName(rs.getString("name"));
        return sneakers;
    }

}
