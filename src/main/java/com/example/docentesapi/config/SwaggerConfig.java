package com.example.docentesapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Docentes")
                        .description("""
                                API RESTful completa para la gestión de docentes universitarios.
                                
                                ## Características principales:
                                - ✅ Operaciones CRUD completas
                                - 🏙️ Filtrado por ciudad
                                - 📚 Filtrado por experiencia                            
                                - 📄 Paginación de resultados                         
                                
                                ## Tecnologías utilizadas:
                                - Java 24
                                - Spring Boot 
                                - Jakarta EE
                                - MySQL 8.0
                                - Spring Data JPA
                                
                                ## Elaborado por:
                                - Atencio Espiritu Morris Jesus
                                - Flores Ildefonso Carlos Andre Johan
                                
                                """

                        )
                        .version("1.0.0")

                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8084")
                                .description("Servidor de desarrollo")
                ));
    }
}