package com.example.solexclusive.Service;

import com.example.solexclusive.Model.TypeSneakers;
import com.example.solexclusive.Repository.TypeSneakersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para consultar los tipos de zapatilla.
 * Actúa como capa intermedia entre el controlador y el repositorio,
 * inyectando la implementación JDBC de TypeSneakersDAO.
 */
@Service
public class TypeSneakersService {
    private final TypeSneakersDAO typeSneakersDAO;

    @Autowired
    public TypeSneakersService(@Qualifier("typeSneakersDAOJdbc") TypeSneakersDAO typeSneakersDAO) {this.typeSneakersDAO = typeSneakersDAO;}

    // Devuelve todos los tipos disponibles (usado en los filtros y formularios)
    public List<TypeSneakers> findAll() {
        return typeSneakersDAO.findAll();
    }

    public TypeSneakers findById(int id) {return typeSneakersDAO.findById(id);}
}
