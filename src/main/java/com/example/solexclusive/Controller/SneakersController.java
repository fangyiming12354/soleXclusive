package com.example.solexclusive.Controller;

import com.example.solexclusive.Model.Brands;
import com.example.solexclusive.Model.Sneakers;
import com.example.solexclusive.Model.TypeSneakers;
import com.example.solexclusive.Repository.SneakersDAO;
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

@Controller
public class SneakersController {
    private SneakersService  sneakersService;
    private BrandsService  brandsService;
    private TypeSneakersService  typeSneakersService;

    @Autowired
    public void SneakersService(SneakersService sneakersService) {this.sneakersService=sneakersService;}

    @Autowired
    public void BrandsService(BrandsService brandsService) {this.brandsService = brandsService;}
    @Autowired
    public void TypeSneakersService(TypeSneakersService typeSneakersService) {this.typeSneakersService = typeSneakersService; }

    @GetMapping({"/sneakers"})
    public String sneakers(Model model){
        model.addAttribute("sneakers",sneakersService.findAll());
        model.addAttribute("brands", brandsService.findAllBrands());
        return "Sneakers/index_sneakers";
    }
    @GetMapping("/sneakers/filter")
    public String filterByBrand(@RequestParam int id_brand, Model model){
        model.addAttribute("sneakers", sneakersService.findByBrand(id_brand));
        model.addAttribute("brands", brandsService.findAllBrands());
        return "Sneakers/index_sneakers";
    }

    @GetMapping({"/sneakers/new"})
    public String newSneakers(Model model){
        model.addAttribute("sneaker",new Sneakers());
        model.addAttribute("brands",brandsService.findAllBrands());
        model.addAttribute("typeSneakers",typeSneakersService.findAll());
        return "Sneakers/form_sneakers";
    }

    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping({"/sneakers/save"})
    public String saveSneakers(@ModelAttribute Sneakers sneakers, @RequestParam("images")MultipartFile image) throws IOException {
        if(image!=null && !image.isEmpty()){
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path path = Paths.get(uploadDir);
            Files.createDirectories(path);
            Files.copy(image.getInputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            sneakers.setFilePath(fileName);
        }
        if(sneakers.getId_brands() !=null && sneakers.getId_brands().getId_brand() != 0){
            Brands brands = brandsService.findBrandById(sneakers.getId_brands().getId_brand());
            sneakers.setId_brands(brands);
        }

        if(sneakers.getId_type_sneakers() !=null && sneakers.getId_type_sneakers().getId_type_sneakers() !=0){
            TypeSneakers typeSneakers = typeSneakersService.findById(sneakers.getId_type_sneakers().getId_type_sneakers());
            sneakers.setId_type_sneakers(typeSneakers);
        }

        sneakersService.save(sneakers);
        return "redirect:/sneakers";
    }

    @GetMapping({"/sneakers/edit/{id}"})
    public String editSneakers(@PathVariable int id, Model model){
        Sneakers sneaker = sneakersService.findById(id);
        model.addAttribute("sneaker",sneaker);
        model.addAttribute("brands",brandsService.findAllBrands());
        model.addAttribute("typeSneakers",typeSneakersService.findAll());
        return "Sneakers/form_sneakers";
    }

    @PostMapping({"/sneakers/update"})
    public String updateSneakers(@ModelAttribute Sneakers sneakers, @RequestParam("images")MultipartFile image) throws IOException {
        Sneakers sneaker = sneakersService.findById(sneakers.getId_sneaker());

        if (image != null && !image.isEmpty()) {
            if (sneaker.getFilePath() !=null) {
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
        if(sneakers.getId_type_sneakers() !=null && sneakers.getId_type_sneakers().getId_type_sneakers() !=0){
            TypeSneakers typeSneakers = typeSneakersService.findById(sneakers.getId_type_sneakers().getId_type_sneakers());
            sneakers.setId_type_sneakers(typeSneakers);
        }
        sneakersService.update(sneaker);

        return "redirect:/sneakers";
    }

    @GetMapping({"/sneakers/delete/{id}"})
    public String deleteSneakers(@PathVariable int id) throws IOException {
        Sneakers sneaker = sneakersService.findById(id);
        if(sneaker!=null && sneaker.getFilePath()!=null){
            Path path = Paths.get(uploadDir).resolve(sneaker.getFilePath());
            Files.deleteIfExists(path);
        }
        sneakersService.delete(id);
        return "redirect:/sneakers";
    }
}
