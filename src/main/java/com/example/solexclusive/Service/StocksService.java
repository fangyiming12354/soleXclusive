package com.example.solexclusive.Service;

import com.example.solexclusive.Model.Stocks;
import com.example.solexclusive.Repository.StocksDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Stocks> findByBrandId(int id_brand) {return stocksDAO.findByBrandId(id_brand);}
    public List<Stocks> findByBrandType(int id_brand,int id_type_sneakers) {return stocksDAO.findByBrandType(id_brand,id_type_sneakers);}
    public List<Stocks> findByType(int id_type_sneakers) {return stocksDAO.findByType(id_type_sneakers);}
    public List<Stocks> findBySneakerId(int id_sneaker) {return stocksDAO.findBySneakerId(id_sneaker);}
}
