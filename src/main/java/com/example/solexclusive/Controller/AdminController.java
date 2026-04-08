package com.example.solexclusive.Controller;

import com.example.solexclusive.Model.*;
import com.example.solexclusive.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

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
    @GetMapping("/admin")
    public String adminPanel(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getType_user())) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "admin_profile";
    }

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
}
