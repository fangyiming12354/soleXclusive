package com.example.solexclusive.Controller;

import com.example.solexclusive.Model.Users;
import com.example.solexclusive.Service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

/**
 * Controlador CRUD para la gestión de usuarios y autenticación.
 * Maneja el listado, alta, edición y eliminación de usuarios (zona admin),
 * así como el flujo de login con redirección según el rol del usuario.
 */
@Controller
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {this.usersService = usersService;}

    /**
     * Lista todos los usuarios registrados (zona admin).
     */
    @GetMapping({"/users"})
    public String findAll(Model model) {
        model.addAttribute("users", usersService.findAll());
        return "Users/index_users";
    }

    /**
     * Muestra el formulario para crear un nuevo usuario.
     */
    @GetMapping({"/users/new"})
    public String newUser(Model model) {
        model.addAttribute("user", new Users());
        return "Users/form_users";
    }

    /**
     * Guarda un nuevo usuario y redirige al listado.
     */
    @PostMapping({"/users/save"})
    public String saveUser(@ModelAttribute Users users) {
        usersService.save(users);
        return "redirect:/users";
    }

    /**
     * Muestra el formulario de edición con los datos actuales del usuario.
     */
    @GetMapping({"/users/edit/{id}"})
    public String editUser(Model model, @PathVariable int id) {
        model.addAttribute("user", usersService.findById(id));
        return "Users/form_users";
    }

    /**
     * Actualiza los datos de un usuario.
     * Si quien edita es un admin, también puede cambiar el rol del usuario.
     */
    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute Users users, HttpSession session) {
        Users loggedUser = (Users) session.getAttribute("user");
        boolean isAdminEditing = loggedUser != null && "admin".equalsIgnoreCase(loggedUser.getType_user());
        usersService.update(users, isAdminEditing);
        return "redirect:/users";
    }

    /**
     * Elimina un usuario por su id y redirige al listado.
     */
    @GetMapping({"/users/delete/{id}"})
    public String deleteUser(@PathVariable int id) {
        usersService.delete(id);
        return "redirect:/users";
    }

    /**
     * Muestra el formulario de login.
     */
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("user", new Users());
        return "Users/login";
    }

    /**
     * Procesa el login. Si las credenciales son correctas:
     * - Redirige a /admin si el rol es "admin"
     * - Redirige a /home si el rol es "customer"
     * Si las credenciales son incorrectas, muestra un mensaje de error.
     */
    @PostMapping("/login")
    public String login(@ModelAttribute("user") Users user,
                        HttpSession session,
                        Model model) {
        Users u = usersService.login(user.getEmail(), user.getPassword());
        if (u != null) {
            session.setAttribute("user", u);

            // Redirigir según el rol del usuario
            if ("admin".equalsIgnoreCase(u.getType_user())) {
                return "redirect:/admin";
            } else {
                return "redirect:/home";
            }
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "Users/login";
        }
    }
}
