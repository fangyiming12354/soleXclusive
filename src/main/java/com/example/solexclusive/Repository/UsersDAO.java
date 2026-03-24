package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Users;

import java.util.List;

public interface UsersDAO {
    void save(Users u);
    void delete(int id);
    void update(Users u);
    Users findById(int id);
    List<Users> findAll();

}
