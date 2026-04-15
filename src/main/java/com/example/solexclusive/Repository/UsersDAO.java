package com.example.solexclusive.Repository;

import com.example.solexclusive.Model.Users;

import java.util.List;

/**
 * Interfaz DAO para la entidad Users.
 * Define las operaciones CRUD y de autenticación disponibles para usuarios.
 */
public interface UsersDAO {
    // Registra un nuevo usuario en la base de datos
    void save(Users u);
    // Elimina un usuario por su identificador
    void delete(int id);
    /**
     * Actualiza los datos de un usuario.
     * @param isAdminEditing si es true, también permite cambiar el rol del usuario
     */
    void update(Users u, boolean isAdminEditing);
    // Busca un usuario por su identificador
    Users findById(int id);
    // Devuelve la lista completa de usuarios
    List<Users> findAll();
    // Verifica credenciales y devuelve el usuario si el login es correcto, null si falla
    Users login(String email, String password);
}
