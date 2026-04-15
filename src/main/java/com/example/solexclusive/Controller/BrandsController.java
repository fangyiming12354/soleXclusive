package com.example.solexclusive.Controller;

import com.example.solexclusive.Model.Brands;
import com.example.solexclusive.Service.BrandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controlador CRUD para la gestión de marcas (zona de administración).
 * Permite listar, crear, editar y eliminar marcas desde el panel admin.
 */
@Controller
public class BrandsController {

    private BrandsService brandsService;

    @Autowired
    public BrandsController(BrandsService brandsService) {this.brandsService = brandsService;}

    /**
     * Lista todas las marcas disponibles.
     */
    @GetMapping({"/brands"})
    public String brands(Model model) {
        model.addAttribute("brands", brandsService.findAllBrands());
        return "Brands/index_brands";
    }

    /**
     * Muestra el formulario para crear una nueva marca.
     */
    @GetMapping({"/brands/new"})
    public String brandsNew(Model model) {
        model.addAttribute("brand", new Brands());
        return "Brands/form_brands";
    }

    /**
     * Guarda una nueva marca y redirige al listado.
     */
    @PostMapping({"/brands/save"})
    public String saveBrands(@ModelAttribute Brands brands) {
        brandsService.addBrand(brands);
        return "redirect:/brands";
    }

    /**
     * Muestra el formulario de edición con los datos actuales de la marca.
     */
    @GetMapping({"/brands/edit/{id}"})
    public String brandsEdir(@PathVariable int id, Model model) {
        Brands b = brandsService.findBrandById(id);
        System.out.println("Editing brand ID: " + b.getId_brand());
        model.addAttribute("brand", brandsService.findBrandById(id));
        return "Brands/form_brands";
    }

    /**
     * Actualiza los datos de una marca y redirige al listado.
     */
    @PostMapping({"/brands/update"})
    public String updateBrands(@ModelAttribute Brands brands) {
        brandsService.updateBrand(brands);
        return "redirect:/brands";
    }

    /**
     * Elimina una marca por su id y redirige al listado.
     */
    @GetMapping({"/brands/delete/{id}"})
    public String deleteBrands(@PathVariable int id) {
        brandsService.deleteBrand(id);
        return "redirect:/brands";
    }
}
