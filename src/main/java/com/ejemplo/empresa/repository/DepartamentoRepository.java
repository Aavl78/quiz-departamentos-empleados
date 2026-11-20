package com.ejemplo.empresa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.empresa.model.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    //al crear: existe un departamento con ese nombre? (sin importar mayusculas)
    boolean existsByNombreIgnoreCase(String nombre);

    //similar, pero para editar: ignora el departamento que se esta editando
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}