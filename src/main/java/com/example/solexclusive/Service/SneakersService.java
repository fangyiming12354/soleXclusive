package com.example.solexclusive.Service;

import com.example.solexclusive.Model.Brands;
import com.example.solexclusive.Model.Sneakers;
import com.example.solexclusive.Repository.SneakersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SneakersService {
    private final SneakersDAO sneakersDAO;

    @Autowired
    public SneakersService(@Qualifier("sneakersDAOJdbc") SneakersDAO sneakersDAO) {this.sneakersDAO = sneakersDAO;}

    public void save(Sneakers sneakers) {sneakersDAO.save(sneakers);}
    public List<Sneakers> findAll(){return sneakersDAO.findAll();}
    public Sneakers findById(int id){return sneakersDAO.findById(id);}
    public List<Sneakers> findByBrand(Brands id){return sneakersDAO.findByBrand(id);}
    public void delete(int id) {sneakersDAO.delete(id);}
    public void update(Sneakers sneakers) {sneakersDAO.update(sneakers);}
    public List<Sneakers> findByType(String type){return sneakersDAO.findByType(type);}
    public List<Sneakers> findByBrandType(Brands id, String type){return sneakersDAO.findByBrandType(id, type);}
}
