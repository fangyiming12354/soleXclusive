package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Stocks;

import java.util.List;

/**
 * Interfaz DAO para la entidad Stocks.
 * Define las operaciones CRUD y de filtrado disponibles para el stock de zapatillas.
 */
public interface StocksDAO {
    // Inserta un nuevo registro de stock
    void add(Stocks stocks);
    // Elimina un registro de stock por su identificador
    void delete(int id_stock);
    // Actualiza un registro de stock existente
    void update(Stocks stocks);
    // Busca un registro de stock por su identificador
    Stocks findById(int id_stock);
    // Devuelve todo el stock disponible
    List<Stocks> findAll();
    // Filtra el stock por marca
    List<Stocks> findByBrandId(int id_brand);
    // Filtra el stock por marca y tipo de zapatilla
    List<Stocks> findByBrandType(int id_brand, int id_type_sneakers);
    // Filtra el stock por tipo de zapatilla
    List<Stocks> findByType(int id_type_sneakers);
    // Filtra el stock por zapatilla específica (útil para ver tallas disponibles de un modelo)
    List<Stocks> findBySneakerId(int id_sneaker);
}
