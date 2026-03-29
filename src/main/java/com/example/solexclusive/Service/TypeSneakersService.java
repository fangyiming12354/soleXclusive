package com.example.solexclusive.Service;

import com.example.solexclusive.Model.TypeSneakers;
import com.example.solexclusive.Repository.TypeSneakersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeSneakersService {
    private final TypeSneakersDAO  typeSneakersDAO;

    @Autowired
    public TypeSneakersService(@Qualifier("typeSneakersDAOJdbc") TypeSneakersDAO typeSneakersDAO) {this.typeSneakersDAO = typeSneakersDAO;}

    public List<TypeSneakers> findAll() {
        return typeSneakersDAO.findAll();
    }
    public TypeSneakers findById(int id) {return typeSneakersDAO.findById(id);}
}
