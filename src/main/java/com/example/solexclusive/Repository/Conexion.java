package com.example.solexclusive.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase Singleton que gestiona la conexión a la base de datos MySQL.
 * Solo existe una instancia de esta clase en toda la aplicación.
 * Usa el patrón Singleton con sincronización para entornos multihilo.
 */
public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/solexclusive";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Instancia única de la clase (Singleton)
    private static Conexion instancia;
    private Connection connection;

    // Constructor privado para evitar la creación externa de instancias
    private Conexion() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión a la base de datos establecida.");
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos.");
            e.printStackTrace();
        }
    }

    /**
     * Devuelve la instancia única de Conexion.
     * Si no existe o la conexión fue cerrada, crea una nueva.
     * Sincronizado para ser seguro en entornos multihilo.
     */
    public static synchronized Conexion getInstancia() {
        if (instancia == null || instancia.connection == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    /**
     * Devuelve la conexión activa a la base de datos.
     * Si la conexión está cerrada o es nula, la reabre automáticamente.
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos manualmente.
     * Útil para liberar recursos al cerrar la aplicación.
     */
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar la conexión.");
            e.printStackTrace();
        }
    }
}
