package com.example.solexclusive.Model;

/**
 * Modelo que representa el tipo de zapatilla (ej: running, casual, baloncesto).
 * Corresponde a la tabla 'type_sneakers' de la base de datos.
 */
public class TypeSneakers {
    // Identificador único del tipo de zapatilla
    private int id_type_sneakers;
    // Nombre del tipo (ej: Running, Casual)
    private String name;

    public TypeSneakers() {
    }

    public int getId_type_sneakers() {
        return id_type_sneakers;
    }

    public void setId_type_sneakers(int id_type_sneakers) {
        this.id_type_sneakers = id_type_sneakers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
