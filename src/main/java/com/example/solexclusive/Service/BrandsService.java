package com.example.solexclusive.Service;

import com.example.solexclusive.Model.Brands;
import com.example.solexclusive.Repository.BrandsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandsService {

    private final BrandsDAO  brandsDAO;

    @Autowired
    public BrandsService(@Qualifier("brandsDAOJdbc")  BrandsDAO brandsDAO) {this.brandsDAO = brandsDAO;}

    public void addBrand(Brands brand) {brandsDAO.addBrand(brand);}
    public void deleteBrand(int id) {brandsDAO.deleteBrand(id);}
    public void updateBrand(Brands brand) {brandsDAO.updateBrand(brand);}
    public Brands findBrandById(int id) {return brandsDAO.findById(id);}
    public List<Brands> findAllBrands() {return brandsDAO.findAllBrands();}

}
