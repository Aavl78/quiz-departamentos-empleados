package com.ejemplo.empresa.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ejemplo.empresa.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    //SELECT * FROM EMPLEADOS (trae tambien el departamento en la misma consulta)
    @Override
    @EntityGraph(attributePaths = "departamento")
    List<Empleado> findAll();

    //SELECT * FROM EMPLEADOS WHERE departamento_id = ***
    @EntityGraph(attributePaths = "departamento")
    List<Empleado> findByDepartamentoId(Long departamentoId);

    //al crear: ya existe un empleado con esa cedula?
    boolean existsByCedula(String cedula);

    //similar, pero para editar: ignora el empleado que se esta editando
    boolean existsByCedulaAndIdNot(String cedula, Long id);

    //el departamento tiene al menos un empleado?
    boolean existsByDepartamentoId(Long departamentoId);

    //suma de salarios de un departamento (si no tiene empleados devuelve 0)
    @Query("SELECT COALESCE(SUM(e.salario), 0) FROM Empleado e WHERE e.departamento.id = :departamentoId")
    BigDecimal sumarSalarios(@Param("departamentoId") Long departamentoId);

    //suma de salarios de un departamento sin contar a un empleado (para editar)
    @Query("SELECT COALESCE(SUM(e.salario), 0) FROM Empleado e " +
           "WHERE e.departamento.id = :departamentoId AND e.id <> :empleadoId")
    BigDecimal sumarSalariosSinEmpleado(@Param("departamentoId") Long departamentoId,
                                        @Param("empleadoId") Long empleadoId);
}