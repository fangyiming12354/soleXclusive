package com.example.solexclusive.Service;

import com.example.solexclusive.Model.Users;
import com.example.solexclusive.Repository.UsersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    private final UsersDAO usersDAO;

    @Autowired
    public UsersService(@Qualifier("usersDAOJdbc")  UsersDAO usersDAO) {this.usersDAO = usersDAO;}

    public void save(Users u) {usersDAO.save(u);}
    public void delete(int id) {usersDAO.delete(id);}
    public void update(Users u,boolean isAdminEditing){usersDAO.update(u,isAdminEditing);}
    public Users findById(int id){return usersDAO.findById(id);}
    public List<Users> findAll(){return usersDAO.findAll();}
    public Users login(String email,String password){return usersDAO.login(email,password);}

}
