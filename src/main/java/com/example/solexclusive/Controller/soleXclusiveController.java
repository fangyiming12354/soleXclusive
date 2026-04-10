package com.example.solexclusive.Controller;

import com.example.solexclusive.Model.*;
import com.example.solexclusive.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class soleXclusiveController {


    private StocksService stocksService;
    private SneakersService sneakersService;
    private BrandsService brandsService;
    private TypeSneakersService typeSneakersService;
    private OrdersService OrdersService;
    private UsersService usersService;

    @Autowired
    public void StocksService(StocksService stocksService) {
        this.stocksService = stocksService;
    }

    @Autowired
    public void SneakersService(SneakersService sneakersService) {
        this.sneakersService = sneakersService;
    }

    @Autowired
    public void BrandsService(BrandsService brandsService) {
        this.brandsService = brandsService;
    }

    @Autowired
    public void TypeSneakersService(TypeSneakersService typeSneakersService) {this.typeSneakersService = typeSneakersService;}
    @Autowired
    public void OrdersService(OrdersService OrdersService) {this.OrdersService = OrdersService;}

    @Autowired
    public void UsersService(UsersService usersService) {this.usersService = usersService;}

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("stocks", stocksService.findAll());
        model.addAttribute("sneakers", sneakersService.findAll());
        model.addAttribute("brands", brandsService.findAllBrands());
        model.addAttribute("typeSneakers", typeSneakersService.findAll());
        return "home";
    }

    @PostMapping("/home/filter")
    public String filterByBrandAndType(@RequestParam int id_brand,
                                       @RequestParam int id_type_sneakers,
                                       Model model) {

        if (id_brand == 0 && id_type_sneakers == 0) {
            model.addAttribute("sneakers", sneakersService.findAll());

        } else if (id_brand == 0) {
            model.addAttribute("sneakers", sneakersService.findByType(id_type_sneakers));

        } else if (id_type_sneakers == 0) {
            model.addAttribute("sneakers", sneakersService.findByBrand(id_brand));

        } else {
            model.addAttribute("sneakers", sneakersService.findByBrandType(id_brand, id_type_sneakers));
        }

        // Esto siempre igual
        model.addAttribute("brands", brandsService.findAllBrands());
        model.addAttribute("typeSneakers", typeSneakersService.findAll());

        return "home";
    }

    @GetMapping("/home/sneakers/{id}")
    public String viewSneaker(@PathVariable int id, Model model) {

        Sneakers sneaker = sneakersService.findById(id);
        model.addAttribute("sneaker", sneaker);
        model.addAttribute("typeSneakers", typeSneakersService.findAll());
        model.addAttribute("brands", brandsService.findAllBrands());
        model.addAttribute("stocks", stocksService.findBySneakerId(id));

        // productos relacionados (ejemplo: misma marca)
        List<Sneakers> related = sneakersService.findByBrand(sneaker.getId_brands().getId_brand());
        model.addAttribute("related", related);

        return "product";
    }

    @PostMapping({"/home/cart/add"})
    public String addToCart(@RequestParam int id_sneaker,
                            @RequestParam(defaultValue = "0") double size,
                            @RequestParam int quantity,
                            HttpSession session,
                            Model model) {

        // Validar que se ha seleccionado una talla
        if (size <= 0) {
            return "redirect:/home/sneakers/" + id_sneaker + "?error=nosize";
        }

        // Obtener usuario de la sesión
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        // Obtener el sneaker
        Sneakers sneaker = sneakersService.findById(id_sneaker);

        // Buscar el stock disponible para este sneaker y talla
        List<Stocks> allStocks = stocksService.findAll();
        int availableQuantity = 0;

        for (Stocks stock : allStocks) {
            if (stock.getId_sneaker().getId_sneaker() == id_sneaker && stock.getSize() == size) {
                availableQuantity = stock.getQuantity();
                break;
            }
        }

        // Validar que la cantidad solicitada no exceda el stock disponible
        if (quantity > availableQuantity) {
            // Redirigir de vuelta al producto con error
            model.addAttribute("error", "La cantidad solicitada supera el stock disponible. Stock: " + availableQuantity);
            model.addAttribute("sneaker", sneaker);
            return "redirect:/home/sneakers/" + id_sneaker + "?error=stock";
        }

        // Obtener o crear el carrito temporal (Order) en sesión
        Orders cart = (Orders) session.getAttribute("cart");
        if (cart == null) {
            cart = new Orders();
            cart.setId_user(user);
        }

        // Crear un item del pedido
        OrderItems item = new OrderItems();
        item.setId_sneaker(sneaker);
        item.setQuantity(quantity);
        item.setSize(size);
        item.setUnit_price(sneaker.getPrice());

        // Verificar si el item ya existe en el carrito
        boolean itemExists = false;
        for (OrderItems existingItem : cart.getItems()) {
            if (existingItem.getId_sneaker().getId_sneaker() == id_sneaker &&
                    existingItem.getSize() == size) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                itemExists = true;
                break;
            }
        }

        // Si no existe, agregarlo
        if (!itemExists) {
            cart.getItems().add(item);
        }

        // Actualizar el total
        cart.setTotal(cart.calculateTotal());

        // Guardar el carrito en sesión
        session.setAttribute("cart", cart);

        // Redirigir al producto o al carrito
        return "redirect:/home";
    }
    @GetMapping("/home/cart")
    public String viewCart(HttpSession session, Model model) {
        Orders cart = (Orders) session.getAttribute("cart");

        if (cart == null) {
            cart = new Orders();
        }

        model.addAttribute("cart", cart);
        model.addAttribute("total", cart.calculateTotal());

        return "cart";
    }

    @PostMapping("/home/cart/checkout")
    public String checkout(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        Orders cart = (Orders) session.getAttribute("cart");

        if (user == null || cart == null || cart.getItems().isEmpty()) {
            return "redirect:/home/cart";
        }

        // Guardar el pedido en la base de datos
        OrdersService.save(cart);

        // Limpiar el carrito
        session.removeAttribute("cart");

        return "redirect:/home"; // o a una página de confirmación
    }
    @GetMapping({"/home/profile"})
    public String viewProfile(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        user = usersService.findById(user.getId_user());
        model.addAttribute("user", user);
        return "profile";
    }
    @GetMapping({"/home/purchase-history"})
    public String viewPurchaseHistory(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        user = usersService.findById(user.getId_user());
        List<Orders> orders = OrdersService.findByCustomerId(user.getId_user());

        // Calcular gasto total
        double totalSpent = 0;
        if (orders != null && !orders.isEmpty()) {
            for (Orders order : orders) {
                totalSpent += order.getTotal();
            }
        }

        model.addAttribute("orders", orders);
        model.addAttribute("user", user);
        model.addAttribute("totalSpent", String.format("%.2f", totalSpent));

        return "purchase_history";
    }
    @GetMapping("/home/order-detail/{id}")
    public String viewOrderDetail(@PathVariable int id, HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Orders order = OrdersService.findById(id);

        // Verificar que el pedido pertenece al usuario autenticado
        if (order == null || order.getId_user().getId_user() != user.getId_user()) {
            return "redirect:/home/purchase-history";
        }

        model.addAttribute("order", order);
        return "order_detail";
    }
    @GetMapping("/home/logout")
    public String logout(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        user = usersService.findById(user.getId_user());
        session.removeAttribute("user");
        return "redirect:/home";
    }
    @GetMapping("/home/create-account")
    public String viewCreateAccount(Model model) {
        model.addAttribute("user", new Users());
        return "create_account";
    }
    @PostMapping("/home/create-account/save")
    public String createAccount(@ModelAttribute Users users) {
        usersService.save(users);
        return "redirect:/login";
    }

    // ==================== EDITAR PERFIL ====================
    @GetMapping("/profile/edit")
    public String showEditProfile(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        user = usersService.findById(user.getId_user());
        model.addAttribute("user", user);
        return "edit_profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute Users user, HttpSession session, Model model) {
        Users sessionUser = (Users) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        // Mantener el tipo de usuario original
        Users currentUser = usersService.findById(sessionUser.getId_user());
        user.setType_user(currentUser.getType_user());
        usersService.update(user, false);
        // Actualizar sesión con datos nuevos
        session.setAttribute("user", usersService.findById(user.getId_user()));
        model.addAttribute("user", usersService.findById(user.getId_user()));
        model.addAttribute("success", "Perfil actualizado correctamente");
        return "edit_profile";
    }

    // ==================== CAMBIAR CONTRASEÑA ====================
    @GetMapping("/profile/change-password")
    public String showChangePassword(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "change_password";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Users currentUser = usersService.findById(user.getId_user());

        // Verificar contraseña actual
        if (!currentUser.getPassword().equals(currentPassword)) {
            model.addAttribute("error", "La contraseña actual no es correcta");
            return "change_password";
        }

        // Verificar que las contraseñas nuevas coinciden
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas nuevas no coinciden");
            return "change_password";
        }

        // Actualizar contraseña
        currentUser.setPassword(newPassword);
        usersService.update(currentUser, false);
        session.setAttribute("user", usersService.findById(currentUser.getId_user()));
        model.addAttribute("success", "Contraseña cambiada correctamente");
        return "change_password";
    }

    // ==================== CANCELAR PEDIDO ====================
    @GetMapping("/home/order/cancel/{id}")
    public String cancelOrder(@PathVariable int id, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Orders order = OrdersService.findById(id);
        // Verificar que el pedido pertenece al usuario
        if (order != null && order.getId_user().getId_user() == user.getId_user()) {
            OrdersService.delete(id);
        }
        return "redirect:/home/purchase-history";
    }
}
