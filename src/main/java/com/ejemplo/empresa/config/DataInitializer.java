package com.ejemplo.empresa.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejemplo.empresa.model.Usuario;
import com.ejemplo.empresa.repository.UsuarioRepository;
import com.ejemplo.empresa.service.UsuarioService;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarDatos(UsuarioRepository repo, UsuarioService usuarioService) {
        return args -> {
            if (repo.count() == 0) {
                // Usuarios de prueba
                usuarioService.crear(
                        new Usuario("Administrador", "admin", "admin@ejemplo.com", "admin123", "ADMIN"));
                usuarioService.crear(
                        new Usuario("María Pérez", "maria", "maria@ejemplo.com", "maria123", "USER"));
            }
        };
    }
}