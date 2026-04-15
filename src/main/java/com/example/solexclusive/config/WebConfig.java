package com.example.solexclusive.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de recursos estáticos del servidor web.
 * Mapea la URL /images/** al directorio físico donde se almacenan
 * las imágenes subidas de las zapatillas, para que Thymeleaf
 * pueda servirlas correctamente en las vistas.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Directorio donde se guardan las imágenes subidas (configurado en application.properties)
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Las peticiones a /images/** se sirven desde el directorio físico de uploads
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
