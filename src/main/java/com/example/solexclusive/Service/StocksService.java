package com.example.solexclusive.Service;

import com.example.solexclusive.Model.Stocks;
import com.example.solexclusive.Repository.StocksDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión del stock de zapatillas.
 * Actúa como capa intermedia entre el controlador y el repositorio,
 * inyectando la implementación JDBC de StocksDAO.
 */
@Service
public class StocksService {
    private final StocksDAO stocksDAO;

    @Autowired
    public StocksService(@Qualifier("stocksDAOJdbc") StocksDAO stocksDAO) {this.stocksDAO = stocksDAO;}

    public void add(Stocks stocks) {stocksDAO.add(stocks);}
    public void update(Stocks stocks) {stocksDAO.update(stocks);}
    public void delete(int id) {stocksDAO.delete(id);}
    public Stocks findById(int id) {return stocksDAO.findById(id);}
    public List<Stocks> findAll() {return stocksDAO.findAll();}
    // Filtra stock por marca
    public List<Stocks> findByBrandId(int id_brand) {return stocksDAO.findByBrandId(id_brand);}
    // Filtra stock por marca y tipo
    public List<Stocks> findByBrandType(int id_brand, int id_type_sneakers) {return stocksDAO.findByBrandType(id_brand, id_type_sneakers);}
    // Filtra stock por tipo
    public List<Stocks> findByType(int id_type_sneakers) {return stocksDAO.findByType(id_type_sneakers);}
    // Devuelve el stock de un modelo concreto (para mostrar tallas disponibles en la ficha del producto)
    public List<Stocks> findBySneakerId(int id_sneaker) {return stocksDAO.findBySneakerId(id_sneaker);}
}
