package com.example.solexclusive.Controller;

import com.example.solexclusive.Model.Users;
import com.example.solexclusive.Service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

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
    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute Users users, HttpSession session){
        // Obtener usuario logueado
        Users loggedUser = (Users) session.getAttribute("user");

        boolean isAdminEditing = loggedUser != null && "admin".equalsIgnoreCase(loggedUser.getType_user());

        usersService.update(users, isAdminEditing);
        return "redirect:/users";
    }
    @GetMapping({"/users/delete/{id}"})
    public String deleteUser(@PathVariable int id){
        usersService.delete(id);
        return "redirect:/users";
    }
    @GetMapping("/login")
    public String mostrarLogin(Model model){
        model.addAttribute("user", new Users()); // si quieres binding con th:object
        return "Users/login"; // tu login.html
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") Users user,
                        HttpSession session,
                        Model model){
        Users u = usersService.login(user.getEmail(), user.getPassword());
        if(u != null){
            session.setAttribute("user", u);

            // Aquí chequeamos el rol
            if("admin".equalsIgnoreCase(u.getType_user())){
                return "redirect:/sneakers"; // por ejemplo, panel de admin
            } else {
                return "redirect:/home"; // por ejemplo, página de cliente
            }

        } else {
            model.addAttribute("error","Invalid email or password");
            return "Users/login";
        }
    }
}
