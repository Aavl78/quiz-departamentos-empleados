package com.ejemplo.empresa.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ejemplo.empresa.model.Departamento;
import com.ejemplo.empresa.model.Empleado;
import com.ejemplo.empresa.model.Puesto;
import com.ejemplo.empresa.model.Usuario;
import com.ejemplo.empresa.repository.UsuarioRepository;
import com.ejemplo.empresa.service.DepartamentoService;
import com.ejemplo.empresa.service.EmpleadoService;
import com.ejemplo.empresa.service.UsuarioService;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner cargarDatos(UsuarioRepository repo,
                                  UsuarioService usuarioService,
                                  DepartamentoService departamentoService,
                                  EmpleadoService empleadoService) {
        return args -> {
            if (repo.count() == 0) {
                // Usuarios de prueba
                usuarioService.crear(
                        new Usuario("Administrador", "admin", "admin@ejemplo.com", "admin123", "ADMIN"));
                usuarioService.crear(
                        new Usuario("María Pérez", "maria", "maria@ejemplo.com", "maria123", "USER"));

                // Departamentos
                Departamento ti = departamentoService.crear(
                        new Departamento("Tecnologia", "San Jose, Edificio Central", new BigDecimal("5000000")));
                Departamento rrhh = departamentoService.crear(
                        new Departamento("Recursos Humanos", "Heredia, Oficina 2", new BigDecimal("3500000")));

                // Empleados de Tecnologia: planilla 4 750 000 de 5 000 000
                empleadoService.crear(new Empleado("Carlos Mora", "101110111", "carlos@empresa.com",
                        Puesto.JEFATURA, new BigDecimal("1800000"), LocalDate.of(2018, 2, 5)), ti.getId());
                empleadoService.crear(new Empleado("Ana Solano", "204440555", "ana@empresa.com",
                        Puesto.DESARROLLADOR, new BigDecimal("1200000"), LocalDate.of(2021, 6, 14)), ti.getId());
                empleadoService.crear(new Empleado("Luis Rojas", "305550666", "luis@empresa.com",
                        Puesto.ANALISTA, new BigDecimal("950000"), LocalDate.of(2022, 9, 1)), ti.getId());
                empleadoService.crear(new Empleado("Sofia Vargas", "112340987", "sofia@empresa.com",
                        Puesto.SOPORTE, new BigDecimal("800000"), LocalDate.of(2023, 3, 20)), ti.getId());

                // Empleados de Recursos Humanos: planilla 2 000 000 de 3 500 000
                empleadoService.crear(new Empleado("Daniela Castro", "402220333", "daniela@empresa.com",
                        Puesto.JEFATURA, new BigDecimal("1100000"), LocalDate.of(2019, 1, 10)), rrhh.getId());
                empleadoService.crear(new Empleado("Jorge Araya", "601230456", "jorge@empresa.com",
                        Puesto.ANALISTA, new BigDecimal("900000"), LocalDate.of(2024, 5, 6)), rrhh.getId());
            }
        };
    }
}