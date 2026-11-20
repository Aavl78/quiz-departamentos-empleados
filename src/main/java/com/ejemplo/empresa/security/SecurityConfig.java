package com.ejemplo.empresa.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Cadena 1: solo para la consola H2 (se revisa primero)
    @Bean
    @Order(1)
    public SecurityFilterChain h2ConsoleChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher(request ->
                request.getRequestURI().startsWith(request.getContextPath() + "/h2-console"))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable())                         // H2 no envia token CSRF
            .headers(h -> h.frameOptions(f -> f.sameOrigin()));   // H2 usa frames
        return http.build();
    }

    // Cadena 2: el resto de la aplicacion
    @Bean
    @Order(2)
    public SecurityFilterChain appChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/error").permitAll()
                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/departamentos/nuevo", "/departamentos/guardar",
                                 "/departamentos/*/editar", "/departamentos/*/actualizar",
                                 "/departamentos/*/eliminar").hasRole("ADMIN")
                .requestMatchers("/empleados/nuevo", "/empleados/guardar",
                                 "/empleados/*/editar", "/empleados/*/actualizar",
                                 "/empleados/*/eliminar").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/departamentos", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    // Encripta las contrasenas con BCrypt
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}