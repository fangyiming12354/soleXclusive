package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Brands;

import java.util.List;

/**
 * Interfaz DAO (Data Access Object) para la entidad Brands.
 * Define las operaciones CRUD disponibles para marcas.
 */
public interface BrandsDAO {
    // Inserta una nueva marca en la base de datos
    void addBrand(Brands b);
    // Elimina una marca por su identificador
    void deleteBrand(int id);
    // Actualiza los datos de una marca existente
    void updateBrand(Brands b);
    // Busca una marca por su identificador
    Brands findById(int id);
    // Devuelve la lista completa de marcas
    List<Brands> findAllBrands();
}
