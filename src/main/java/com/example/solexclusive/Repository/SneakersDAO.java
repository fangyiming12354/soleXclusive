package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Brands;
import com.example.solexclusive.Model.Sneakers;

import java.util.List;

/**
 * Interfaz DAO para la entidad Sneakers.
 * Define las operaciones CRUD y de filtrado disponibles para zapatillas.
 */
public interface SneakersDAO {
    // Inserta una nueva zapatilla en la base de datos
    void save(Sneakers sneaker);
    // Actualiza los datos de una zapatilla existente
    void update(Sneakers sneaker);
    // Elimina una zapatilla por su identificador
    void delete(int id);
    // Busca una zapatilla por su identificador
    Sneakers findById(int id);
    // Devuelve la lista completa de zapatillas
    List<Sneakers> findAll();
    // Filtra zapatillas por marca
    List<Sneakers> findByBrand(int id_brand);
    // Filtra zapatillas por tipo
    List<Sneakers> findByType(int id_type_sneakers);
    // Filtra zapatillas por marca y tipo a la vez
    List<Sneakers> findByBrandType(int id_brand, int id_type_sneakers);
}
