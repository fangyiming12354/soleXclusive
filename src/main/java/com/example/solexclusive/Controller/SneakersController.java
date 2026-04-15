package com.example.solexclusive.Controller;

import com.example.solexclusive.Model.Brands;
import com.example.solexclusive.Model.Sneakers;
import com.example.solexclusive.Model.TypeSneakers;
import com.example.solexclusive.Service.BrandsService;
import com.example.solexclusive.Service.SneakersService;
import com.example.solexclusive.Service.TypeSneakersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Controlador CRUD para la gestión de zapatillas (zona admin).
 * Gestiona el listado, filtrado, alta, edición y eliminación de zapatillas.
 * Las imágenes se guardan en el directorio configurado en app.upload.dir.
 */
@Controller
public class SneakersController {
    private SneakersService sneakersService;
    private BrandsService brandsService;
    private TypeSneakersService typeSneakersService;

    @Autowired
    public void SneakersService(SneakersService sneakersService) {this.sneakersService = sneakersService;}

    @Autowired
    public void BrandsService(BrandsService brandsService) {this.brandsService = brandsService;}

    @Autowired
    public void TypeSneakersService(TypeSneakersService typeSneakersService) {this.typeSneakersService = typeSneakersService;}

    /**
     * Lista todas las zapatillas con sus marcas y tipos para el panel admin.
     */
    @GetMapping({"/sneakers"})
    public String sneakers(Model model) {
        model.addAttribute("sneakers", sneakersService.findAll());
        model.addAttribute("brands", brandsService.findAllBrands());
        model.addAttribute("typeSneakers", typeSneakersService.findAll());
        return "Sneakers/index_sneakers";
    }

    /**
     * Filtra las zapatillas por marca y/o tipo en el panel admin.
     * Si ambos filtros son 0, muestra todas las zapatillas.
     */
    @GetMapping("/sneakers/filter")
    public String filterSneakers(@RequestParam int id_brand,
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
        model.addAttribute("brands", brandsService.findAllBrands());
        model.addAttribute("typeSneakers", typeSneakersService.findAll());
        return "Sneakers/index_sneakers";
    }

    /**
     * Muestra el formulario para crear una nueva zapatilla.
     */
    @GetMapping({"/sneakers/new"})
    public String newSneakers(Model model) {
        model.addAttribute("sneaker", new Sneakers());
        model.addAttribute("brands", brandsService.findAllBrands());
        model.addAttribute("typeSneakers", typeSneakersService.findAll());
        return "Sneakers/form_sneakers";
    }

    // Directorio donde se guardan las imágenes subidas, configurado en application.properties
    @Value("${app.upload.dir}")
    private String uploadDir;

    /**
     * Guarda una nueva zapatilla con su imagen.
     * La imagen se almacena en el directorio de uploads con un nombre único basado en timestamp.
     */
    @PostMapping({"/sneakers/save"})
    public String saveSneakers(@ModelAttribute Sneakers sneakers, @RequestParam("images") MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            // Nombre único para evitar colisiones entre archivos
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path path = Paths.get(uploadDir);
            Files.createDirectories(path);
            Files.copy(image.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            sneakers.setFilePath(fileName);
        }
        // Cargar el objeto Brands completo desde el id seleccionado en el formulario
        if (sneakers.getId_brands() != null && sneakers.getId_brands().getId_brand() != 0) {
            Brands brands = brandsService.findBrandById(sneakers.getId_brands().getId_brand());
            sneakers.setId_brands(brands);
        }
        // Cargar el objeto TypeSneakers completo desde el id seleccionado en el formulario
        if (sneakers.getId_type_sneakers() != null && sneakers.getId_type_sneakers().getId_type_sneakers() != 0) {
            TypeSneakers typeSneakers = typeSneakersService.findById(sneakers.getId_type_sneakers().getId_type_sneakers());
            sneakers.setId_type_sneakers(typeSneakers);
        }

        sneakersService.save(sneakers);
        return "redirect:/sneakers";
    }

    /**
     * Muestra el formulario de edición con los datos actuales de la zapatilla.
     */
    @GetMapping({"/sneakers/edit/{id}"})
    public String editSneakers(@PathVariable int id, Model model) {
        Sneakers sneaker = sneakersService.findById(id);
        model.addAttribute("sneaker", sneaker);
        model.addAttribute("brands", brandsService.findAllBrands());
        model.addAttribute("typeSneakers", typeSneakersService.findAll());
        return "Sneakers/form_sneakers";
    }

    /**
     * Actualiza los datos de una zapatilla.
     * Si se sube una nueva imagen, borra la anterior del disco antes de guardar la nueva.
     */
    @PostMapping({"/sneakers/update"})
    public String updateSneakers(@ModelAttribute Sneakers sneakers, @RequestParam("images") MultipartFile image) throws IOException {
        Sneakers sneaker = sneakersService.findById(sneakers.getId_sneaker());

        if (image != null && !image.isEmpty()) {
            // Eliminar la imagen anterior si existe
            if (sneaker.getFilePath() != null) {
                Path path = Paths.get(uploadDir).resolve(sneaker.getFilePath());
                Files.deleteIfExists(path);
            }
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path path = Paths.get(uploadDir);
            Files.createDirectories(path);
            Files.copy(image.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            sneaker.setFilePath(fileName);
        }
        sneaker.setName(sneakers.getName());
        sneaker.setDescription(sneakers.getDescription());
        sneaker.setPrice(sneakers.getPrice());
        if (sneakers.getId_brands() != null && sneakers.getId_brands().getId_brand() != 0) {
            Brands brand = brandsService.findBrandById(sneakers.getId_brands().getId_brand());
            sneaker.setId_brands(brand);
        }
        if (sneakers.getId_type_sneakers() != null && sneakers.getId_type_sneakers().getId_type_sneakers() != 0) {
            TypeSneakers typeSneakers = typeSneakersService.findById(sneakers.getId_type_sneakers().getId_type_sneakers());
            sneaker.setId_type_sneakers(typeSneakers);
        }
        sneakersService.update(sneaker);

        return "redirect:/sneakers";
    }

    /**
     * Elimina una zapatilla y su imagen del disco, luego redirige al listado.
     */
    @GetMapping({"/sneakers/delete/{id}"})
    public String deleteSneakers(@PathVariable int id) throws IOException {
        Sneakers sneaker = sneakersService.findById(id);
        // Eliminar el archivo de imagen del disco si existe
        if (sneaker != null && sneaker.getFilePath() != null) {
            Path path = Paths.get(uploadDir).resolve(sneaker.getFilePath());
            Files.deleteIfExists(path);
        }
        sneakersService.delete(id);
        return "redirect:/sneakers";
    }
}
