package com.example.solexclusive.Controller;

import com.example.solexclusive.Model.*;
import com.example.solexclusive.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador principal de la tienda (zona pública y cliente).
 * Gestiona: home, ficha de producto, carrito, checkout, perfil,
 * historial de pedidos, edición de perfil, cambio de contraseña
 * y cancelación de pedidos por parte del cliente.
 */
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

    // ==================== HOME ====================

    /**
     * Muestra la página principal con todas las zapatillas, marcas y tipos
     * disponibles para filtrar.
     */
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("stocks", stocksService.findAll());
        model.addAttribute("sneakers", sneakersService.findAll());
        model.addAttribute("brands", brandsService.findAllBrands());
        model.addAttribute("typeSneakers", typeSneakersService.findAll());
        return "home";
    }

    /**
     * Filtra las zapatillas del home por marca y/o tipo.
     * Si ambos filtros son 0, muestra todos los productos.
     */
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

        // Siempre se envían marcas y tipos para rellenar los desplegables del filtro
        model.addAttribute("brands", brandsService.findAllBrands());
        model.addAttribute("typeSneakers", typeSneakersService.findAll());

        return "home";
    }

    // ==================== DETALLE DE PRODUCTO ====================

    /**
     * Muestra la ficha de una zapatilla con su stock por tallas
     * y productos relacionados de la misma marca.
     */
    @GetMapping("/home/sneakers/{id}")
    public String viewSneaker(@PathVariable int id, Model model) {

        Sneakers sneaker = sneakersService.findById(id);
        model.addAttribute("sneaker", sneaker);
        model.addAttribute("typeSneakers", typeSneakersService.findAll());
        model.addAttribute("brands", brandsService.findAllBrands());
        // Stock por tallas del modelo concreto
        model.addAttribute("stocks", stocksService.findBySneakerId(id));

        // Productos relacionados: zapatillas de la misma marca
        List<Sneakers> related = sneakersService.findByBrand(sneaker.getId_brands().getId_brand());
        model.addAttribute("related", related);

        return "product";
    }

    // ==================== CARRITO ====================

    /**
     * Añade una zapatilla al carrito de la sesión.
     * Valida que se haya seleccionado talla y que la cantidad no supere el stock.
     * El carrito se almacena como objeto Orders en la sesión HTTP.
     */
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

        // Redirigir al login si el usuario no está autenticado
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

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
            model.addAttribute("error", "La cantidad solicitada supera el stock disponible. Stock: " + availableQuantity);
            model.addAttribute("sneaker", sneaker);
            return "redirect:/home/sneakers/" + id_sneaker + "?error=stock";
        }

        // Obtener o crear el carrito temporal (Orders) almacenado en sesión
        Orders cart = (Orders) session.getAttribute("cart");
        if (cart == null) {
            cart = new Orders();
            cart.setId_user(user);
        }

        // Crear la línea del pedido
        OrderItems item = new OrderItems();
        item.setId_sneaker(sneaker);
        item.setQuantity(quantity);
        item.setSize(size);
        item.setUnit_price(sneaker.getPrice());

        // Si ya existe el mismo modelo y talla en el carrito, sumar la cantidad
        boolean itemExists = false;
        for (OrderItems existingItem : cart.getItems()) {
            if (existingItem.getId_sneaker().getId_sneaker() == id_sneaker &&
                    existingItem.getSize() == size) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                itemExists = true;
                break;
            }
        }

        // Si no existe, añadir como nueva línea
        if (!itemExists) {
            cart.getItems().add(item);
        }

        // Recalcular el total del carrito
        cart.setTotal(cart.calculateTotal());

        // Guardar el carrito actualizado en sesión
        session.setAttribute("cart", cart);

        return "redirect:/home";
    }

    /**
     * Muestra el contenido actual del carrito de la sesión.
     */
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

    /**
     * Finaliza la compra: guarda el pedido en la base de datos
     * (descuenta stock automáticamente) y vacía el carrito de la sesión.
     */
    @PostMapping("/home/cart/checkout")
    public String checkout(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        Orders cart = (Orders) session.getAttribute("cart");

        if (user == null || cart == null || cart.getItems().isEmpty()) {
            return "redirect:/home/cart";
        }

        // Persistir el pedido y descontar stock
        OrdersService.save(cart);

        // Limpiar el carrito de la sesión
        session.removeAttribute("cart");

        return "redirect:/home";
    }

    // ==================== PERFIL DE USUARIO ====================

    /**
     * Muestra la página de perfil del usuario autenticado.
     */
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

    /**
     * Muestra el historial de compras del usuario autenticado
     * junto con el gasto total acumulado.
     */
    @GetMapping({"/home/purchase-history"})
    public String viewPurchaseHistory(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        user = usersService.findById(user.getId_user());
        List<Orders> orders = OrdersService.findByCustomerId(user.getId_user());

        // Calcular el gasto total acumulado del usuario
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

    /**
     * Muestra el detalle de un pedido concreto del usuario.
     * Verifica que el pedido pertenece al usuario autenticado antes de mostrarlo.
     */
    @GetMapping("/home/order-detail/{id}")
    public String viewOrderDetail(@PathVariable int id, HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Orders order = OrdersService.findById(id);

        // Seguridad: verificar que el pedido pertenece al usuario autenticado
        if (order == null || order.getId_user().getId_user() != user.getId_user()) {
            return "redirect:/home/purchase-history";
        }

        model.addAttribute("order", order);
        return "order_detail";
    }

    /**
     * Cierra la sesión del usuario y redirige al home.
     */
    @GetMapping("/home/logout")
    public String logout(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        user = usersService.findById(user.getId_user());
        session.removeAttribute("user");
        return "redirect:/home";
    }

    /**
     * Muestra el formulario de creación de cuenta.
     */
    @GetMapping("/home/create-account")
    public String viewCreateAccount(Model model) {
        model.addAttribute("user", new Users());
        return "create_account";
    }

    /**
     * Registra un nuevo usuario y redirige al login.
     */
    @PostMapping("/home/create-account/save")
    public String createAccount(@ModelAttribute Users users) {
        usersService.save(users);
        return "redirect:/login";
    }

    // ==================== EDITAR PERFIL ====================

    /**
     * Muestra el formulario de edición de perfil del usuario autenticado.
     */
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

    /**
     * Guarda los cambios del perfil del usuario.
     * Conserva el tipo de usuario original (el cliente no puede cambiar su propio rol).
     */
    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute Users user, HttpSession session, Model model) {
        Users sessionUser = (Users) session.getAttribute("user");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        // Mantener el tipo de usuario original para evitar escalada de privilegios
        Users currentUser = usersService.findById(sessionUser.getId_user());
        user.setType_user(currentUser.getType_user());
        usersService.update(user, false);
        // Actualizar la sesión con los nuevos datos
        session.setAttribute("user", usersService.findById(user.getId_user()));
        model.addAttribute("user", usersService.findById(user.getId_user()));
        model.addAttribute("success", "Perfil actualizado correctamente");
        return "edit_profile";
    }

    // ==================== CAMBIAR CONTRASEÑA ====================

    /**
     * Muestra el formulario para cambiar la contraseña.
     */
    @GetMapping("/profile/change-password")
    public String showChangePassword(HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "change_password";
    }

    /**
     * Procesa el cambio de contraseña.
     * Verifica que la contraseña actual sea correcta y que las nuevas coincidan.
     */
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

        // Verificar que la contraseña actual introducida es correcta
        if (!currentUser.getPassword().equals(currentPassword)) {
            model.addAttribute("error", "La contraseña actual no es correcta");
            return "change_password";
        }

        // Verificar que las dos contraseñas nuevas coinciden
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas nuevas no coinciden");
            return "change_password";
        }

        // Actualizar la contraseña y refrescar la sesión
        currentUser.setPassword(newPassword);
        usersService.update(currentUser, false);
        session.setAttribute("user", usersService.findById(currentUser.getId_user()));
        model.addAttribute("success", "Contraseña cambiada correctamente");
        return "change_password";
    }

    // ==================== CANCELAR PEDIDO ====================

    /**
     * Cancela un pedido del usuario autenticado.
     * Verifica que el pedido pertenece al usuario antes de eliminarlo.
     * Al eliminar el pedido se restaura automáticamente el stock.
     */
    @GetMapping("/home/order/cancel/{id}")
    public String cancelOrder(@PathVariable int id, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Orders order = OrdersService.findById(id);
        // Seguridad: solo el dueño del pedido puede cancelarlo
        if (order != null && order.getId_user().getId_user() == user.getId_user()) {
            OrdersService.delete(id);
        }
        return "redirect:/home/purchase-history";
    }
}
