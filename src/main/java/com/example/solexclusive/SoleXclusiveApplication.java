package com.example.solexclusive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación SoleXclusive.
 * Punto de entrada de Spring Boot: arranca el contexto de la aplicación
 * y lanza el servidor embebido.
 */
@SpringBootApplication
public class SoleXclusiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoleXclusiveApplication.class, args);
    }
}
