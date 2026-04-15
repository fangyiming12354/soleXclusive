package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.TypeSneakers;

import java.util.List;

/**
 * Interfaz DAO para la entidad TypeSneakers.
 * Solo permite consultar los tipos (no se crean ni eliminan desde la app).
 */
public interface TypeSneakersDAO {
    // Devuelve todos los tipos de zapatilla disponibles
    List<TypeSneakers> findAll();
    // Busca un tipo de zapatilla por su identificador
    TypeSneakers findById(int id);
}
