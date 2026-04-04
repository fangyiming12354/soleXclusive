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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class soleXclusiveController {


    private StocksService stocksService;
    private SneakersService sneakersService;
    private BrandsService brandsService;
    private TypeSneakersService typeSneakersService;
    private OrdersService OrdersService;

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
    public void TypeSneakersService(TypeSneakersService typeSneakersService) {
        this.typeSneakersService = typeSneakersService;
    }
    @Autowired
    public void OrdersService(OrdersService OrdersService) {this.OrdersService = OrdersService;}


    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("stocks", stocksService.findAll());
        model.addAttribute("sneakers", sneakersService.findAll());
        model.addAttribute("brands", brandsService.findAllBrands());
        model.addAttribute("typeSneakers", typeSneakersService.findAll());
        return "home";
    }

    @PostMapping("/home/filter")
    public String filterByBrandAndType(@RequestParam int id_brand, @RequestParam int id_type_sneakers, Model model) {
        if (id_brand == 0) {
            model.addAttribute("stocks", stocksService.findByType(id_type_sneakers));
            model.addAttribute("sneakers", sneakersService.findAll());
            model.addAttribute("brands", brandsService.findAllBrands());
            model.addAttribute("typeSneakers", typeSneakersService.findAll());
            return "home";
        } else if (id_type_sneakers == 0) {
            model.addAttribute("stocks", stocksService.findByBrandId(id_brand));
            model.addAttribute("sneakers", sneakersService.findAll());
            model.addAttribute("brands", brandsService.findAllBrands());
            model.addAttribute("typeSneakers", typeSneakersService.findAll());
            return "home";
        } else {
            model.addAttribute("stocks", stocksService.findByBrandType(id_brand, id_type_sneakers));
            model.addAttribute("sneakers", sneakersService.findAll());
            model.addAttribute("brands", brandsService.findAllBrands());
            model.addAttribute("typeSneakers", typeSneakersService.findAll());
        }

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
}
