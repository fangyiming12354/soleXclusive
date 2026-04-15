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

/**
 * Controlador CRUD para la gestión del stock de zapatillas (zona admin).
 * Permite listar, filtrar, añadir, editar y eliminar registros de stock.
 * Cada registro de stock representa la cantidad disponible de un modelo en una talla concreta.
 */
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

    /**
     * Lista todo el stock disponible con sus zapatillas, marcas y tipos.
     */
    @GetMapping("/stocks")
    public String stocks(Model model) {
        model.addAttribute("stocks", stocksService.findAll());
        model.addAttribute("sneakers", sneakersService.findAll());
        model.addAttribute("brands", brandsService.findAllBrands());
        model.addAttribute("typeSneakers", typeSneakersService.findAll());
        return "Stocks/index_stocks";
    }

    /**
     * Filtra el stock por marca y/o tipo de zapatilla.
     * Si ambos filtros son 0, muestra todo el stock.
     */
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

    /**
     * Muestra el formulario para añadir un nuevo registro de stock.
     */
    @GetMapping({"/stocks/new"})
    public String newStocks(Model model) {
        model.addAttribute("stock", new Stocks());
        model.addAttribute("sneakers", sneakersService.findAll());
        return "Stocks/form_stocks";
    }

    /**
     * Guarda un nuevo registro de stock.
     * Valida que se haya seleccionado una zapatilla y que la talla sea mayor que 0.
     */
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

        // Cargar el objeto Sneakers completo para asegurar que todos sus datos están disponibles
        Sneakers sneakers = sneakersService.findById(stocks.getId_sneaker().getId_sneaker());
        stocks.setId_sneaker(sneakers);

        stocksService.add(stocks);
        return "redirect:/stocks";
    }

    /**
     * Muestra el formulario de edición con los datos actuales del registro de stock.
     */
    @GetMapping({"/stocks/edit/{id}"})
    public String editStocks(@PathVariable("id") int id, Model model) {
        Stocks stocks = stocksService.findById(id);
        model.addAttribute("stock", stocks);
        model.addAttribute("sneakers", sneakersService.findAll());
        return "Stocks/form_stocks";
    }

    /**
     * Actualiza un registro de stock existente.
     * Valida que se haya seleccionado una zapatilla y que la talla sea mayor que 0.
     */
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
        // Cargar el objeto Sneakers completo antes de actualizar
        Sneakers sneakers = sneakersService.findById(stocks.getId_sneaker().getId_sneaker());
        stocks.setId_sneaker(sneakers);

        stocksService.update(stocks);
        return "redirect:/stocks";
    }

    /**
     * Elimina un registro de stock por su id y redirige al listado.
     */
    @GetMapping({"/stocks/delete/{id}"})
    public String deleteStocks(@PathVariable("id") int id) {
        stocksService.delete(id);
        return "redirect:/stocks";
    }
}
