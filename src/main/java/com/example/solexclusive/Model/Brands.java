package com.example.solexclusive.Model;

/**
 * Modelo que representa una marca de zapatillas.
 * Corresponde a la tabla 'brands' de la base de datos.
 */
public class Brands {
    // Identificador único de la marca
    private int id_brand;
    // Nombre de la marca (ej: Nike, Adidas)
    private String name;

    public Brands() {
    }

    public int getId_brand() {
        return id_brand;
    }

    public void setId_brand(int id_brand) {
        this.id_brand = id_brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
