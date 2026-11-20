package com.ejemplo.empresa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.empresa.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //buscar un usuario por su username (lo usa el login)
    Optional<Usuario> findByUsername(String username);

    //ya existe un usuario con ese username?
    boolean existsByUsername(String username);
}