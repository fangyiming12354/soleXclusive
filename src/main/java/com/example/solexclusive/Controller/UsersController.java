package com.example.solexclusive.Controller;

import com.example.solexclusive.Model.Users;
import com.example.solexclusive.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsersController {

    private final UsersService  usersService;

    @Autowired
    public UsersController(UsersService usersService){this.usersService = usersService;}

    @GetMapping({"/users"})
    public String findAll(Model model){
        model.addAttribute("users",usersService.findAll());
        return "Users/index_users";
    }

    @GetMapping({"/users/new"})
    public String newUser(Model model){
        model.addAttribute("user",new Users());
        return "Users/form_users";
    }

    @PostMapping({"/users/save"})
    public String saveUser(@ModelAttribute Users users){
        usersService.save(users);
        return "redirect:/users";
    }

    @GetMapping({"/users/edit/{id}"})
    public String editUser(Model model, @PathVariable int id){
        model.addAttribute("user",usersService.findById(id));
        return "Users/form_users";
    }
    @PostMapping({"/users/update"})
    public String updateUser(@ModelAttribute Users users){
        usersService.update(users);
        return "redirect:/users";
    }
    @GetMapping({"/users/delete/{id}"})
    public String deleteUser(@PathVariable int id){
        usersService.delete(id);
        return "redirect:/users";
    }
}
