package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.TypeSneakers;

import java.util.List;

public interface TypeSneakersDAO {
    List<TypeSneakers> findAll();
    TypeSneakers findById(int id);
}
