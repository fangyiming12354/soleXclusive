package com.example.solexclusive.Model;

/**
 * Modelo que representa una zapatilla del catálogo.
 * Corresponde a la tabla 'sneakers' de la base de datos.
 * Contiene referencias a su marca (Brands) y su tipo (TypeSneakers).
 */
public class Sneakers {
    // Identificador único de la zapatilla
    private int id_sneaker;
    private String name;
    private String description;
    // Ruta relativa del archivo de imagen subido
    private String filePath;
    private double price;
    // Marca a la que pertenece la zapatilla
    private Brands id_brands;
    // Tipo de zapatilla (running, casual, etc.)
    private TypeSneakers id_type_sneakers;

    public Sneakers() {
        // Inicializar objetos relacionados para evitar NullPointerException
        this.id_brands = new Brands();
        this.id_type_sneakers = new TypeSneakers();
    }

    public int getId_sneaker() {
        return id_sneaker;
    }

    public TypeSneakers getId_type_sneakers() {
        return id_type_sneakers;
    }

    public void setId_type_sneakers(TypeSneakers id_type_sneakers) {
        this.id_type_sneakers = id_type_sneakers;
    }

    public void setId_sneaker(int id_sneaker) {
        this.id_sneaker = id_sneaker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Brands getId_brands() {
        return id_brands;
    }

    public void setId_brands(Brands id_brands) {
        this.id_brands = id_brands;
    }
}
