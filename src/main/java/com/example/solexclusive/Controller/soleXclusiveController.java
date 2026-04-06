package com.example.solexclusive.Controller;

import com.example.solexclusive.Model.OrderItems;
import com.example.solexclusive.Model.Orders;
import com.example.solexclusive.Model.Sneakers;
import com.example.solexclusive.Model.Users;
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
        model.addAttribute("stocks", stocksService.findAll());

        // productos relacionados (ejemplo: misma marca)
        List<Sneakers> related = sneakersService.findByBrand(sneaker.getId_brands().getId_brand());
        model.addAttribute("related", related);

        return "product";
    }

    @PostMapping({"/home/cart/add"})

    public String addToCart(@RequestParam int id_sneaker,
                            @RequestParam double size,
                            @RequestParam int quantity,
                            HttpSession session,
                            Model model) {

        // Obtener usuario de la sesión
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            // Redirigir al login si no está autenticado
            return "redirect:/login";
        }

        // Obtener o crear el carrito temporal (Order) en sesión
        Orders cart = (Orders) session.getAttribute("cart");
        if (cart == null) {
            cart = new Orders();
            cart.setId_user(user);
        }

        // Obtener el sneaker
        Sneakers sneaker = sneakersService.findById(id_sneaker);

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
        return "redirect:/home/sneakers/" + id_sneaker;
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
        if (user == null) {
            return "redirect:/login";
        }
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
}
