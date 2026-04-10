package com.example.solexclusive.Controller;

import com.example.solexclusive.Model.Sneakers;
import com.example.solexclusive.Model.Stocks;
import org.springframework.ui.Model;
import com.example.solexclusive.Service.BrandsService;
import com.example.solexclusive.Service.SneakersService;
import com.example.solexclusive.Service.StocksService;
import com.example.solexclusive.Service.TypeSneakersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StocksController {
    private StocksService stocksService;
    private SneakersService sneakersService;
    private BrandsService brandsService;
    private TypeSneakersService typeSneakersService;

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

    @GetMapping("/stocks")
    public String stocks(Model model) {
        model.addAttribute("stocks", stocksService.findAll());
        model.addAttribute("sneakers", sneakersService.findAll());
        model.addAttribute("brands", brandsService.findAllBrands());
        model.addAttribute("typeSneakers", typeSneakersService.findAll());
        return "Stocks/index_stocks";
    }

    @GetMapping("/stocks/filter")
    public String filterStocks(@RequestParam int id_brand,
                               @RequestParam int id_type_sneakers,
                               Model model) {
        if (id_brand == 0 && id_type_sneakers == 0) {
            model.addAttribute("stocks", stocksService.findAll());
        } else if (id_brand == 0) {
            model.addAttribute("stocks", stocksService.findByType(id_type_sneakers));
        } else if (id_type_sneakers == 0) {
            model.addAttribute("stocks", stocksService.findByBrandId(id_brand));
        } else {
            model.addAttribute("stocks", stocksService.findByBrandType(id_brand, id_type_sneakers));
        }
        model.addAttribute("sneakers", sneakersService.findAll());
        model.addAttribute("brands", brandsService.findAllBrands());
        model.addAttribute("typeSneakers", typeSneakersService.findAll());
        return "Stocks/index_stocks";
    }

    @GetMapping({"/stocks/new"})
    public String newStocks(Model model) {
        model.addAttribute("stock", new Stocks());
        model.addAttribute("sneakers", sneakersService.findAll());
        return "Stocks/form_stocks";
    }

    @PostMapping({"/stocks/save"})
    public String saveStocks(@ModelAttribute("stocks") Stocks stocks, Model model) {
        if (stocks.getId_sneaker() == null || stocks.getId_sneaker().getId_sneaker() == 0) {
            model.addAttribute("error1", "Debes seleccionar una sneaker");
            model.addAttribute("sneakers", sneakersService.findAll());
            return "Stocks/form_stocks";
        }
        if (stocks.getSize() <= 0) {
            model.addAttribute("error2", "El tamaño debe ser mayor que 0");
            return "Stocks/form_stocks";
        }

        Sneakers sneakers = sneakersService.findById(stocks.getId_sneaker().getId_sneaker());
        stocks.setId_sneaker(sneakers);

        stocksService.add(stocks);
        return "redirect:/stocks";
    }

    @GetMapping({"/stocks/edit/{id}"})
    public String editStocks(@PathVariable("id") int id, Model model) {
        Stocks stocks = stocksService.findById(id);
        model.addAttribute("stock", stocks);
        model.addAttribute("sneakers", sneakersService.findAll());
        return "Stocks/form_stocks";
    }

    @PostMapping({"/stocks/update"})
    public String updateStocks(@ModelAttribute("stocks") Stocks stocks, Model model) {
        if (stocks.getId_sneaker() == null || stocks.getId_sneaker().getId_sneaker() == 0) {
            model.addAttribute("error1", "Debes seleccionar una sneaker");
            model.addAttribute("sneakers", sneakersService.findAll());
            return "Stocks/form_stocks";
        }
        if (stocks.getSize() <= 0) {
            model.addAttribute("error2", "El tamaño debe ser mayor que 0");
            return "Stocks/form_stocks";
        }
        Sneakers sneakers = sneakersService.findById(stocks.getId_sneaker().getId_sneaker());
        stocks.setId_sneaker(sneakers);

        stocksService.update(stocks);
        return "redirect:/stocks";
    }
    @GetMapping({"/stocks/delete/{id}"})
    public String deleteStocks(@PathVariable("id") int id) {
        stocksService.delete(id);
        return "redirect:/stocks";
    }
}
