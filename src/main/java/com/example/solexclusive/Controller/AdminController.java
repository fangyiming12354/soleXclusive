package com.example.solexclusive.Controller;

import com.example.solexclusive.Model.*;
import com.example.solexclusive.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;

/**
 * Controlador del panel de administración.
 * Todos los endpoints verifican que el usuario tenga el rol "admin".
 * Gestiona la visualización y eliminación de pedidos desde el panel admin.
 */
@Controller
public class AdminController {
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

    /**
     * Muestra el panel principal del administrador.
     * Redirige al login si el usuario no está autenticado o no es admin.
     */
    @GetMapping("/admin")
    public String adminPanel(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getType_user())) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "admin_profile";
    }

    /**
     * Muestra el listado de todos los pedidos para el administrador.
     */
    @GetMapping("/admin/orders")
    public String adminOrders(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getType_user())) {
            return "redirect:/login";
        }
        model.addAttribute("orders", OrdersService.findAll());
        model.addAttribute("user", user);
        return "admin_orders";
    }

    /**
     * Muestra el detalle de un pedido concreto en el panel de administración.
     */
    @GetMapping("/admin/orders/{id}")
    public String adminOrderDetail(@PathVariable int id, HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getType_user())) {
            return "redirect:/login";
        }
        model.addAttribute("order", OrdersService.findById(id));
        model.addAttribute("user", user);
        return "admin_order_detail";
    }

    /**
     * Elimina un pedido desde el panel de administración.
     * Al eliminar se restaura el stock de las zapatillas del pedido.
     */
    @GetMapping("/admin/orders/delete/{id}")
    public String adminDeleteOrder(@PathVariable int id, HttpSession session) {
        Users user = (Users) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getType_user())) {
            return "redirect:/login";
        }
        OrdersService.delete(id);
        return "redirect:/admin/orders";
    }
}
