package com.example.solexclusive.Model;

/**
 * Modelo que representa un usuario de la aplicación.
 * Corresponde a la tabla 'users' de la base de datos.
 * El campo type_user indica el rol: "admin" o "customer".
 */
public class Users {
    private String name;
    private String password;
    private String email;
    // Rol del usuario como texto: "admin" o "customer"
    private String type_user;
    private int id_user;

    public Users() {

    }

    public String getName() {
        return name;
    }

    public String getType_user() {
        return type_user;
    }

    public void setType_user(String type_user) {
        this.type_user = type_user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
