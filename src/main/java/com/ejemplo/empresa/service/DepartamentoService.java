package com.ejemplo.empresa.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ejemplo.empresa.model.Departamento;
import com.ejemplo.empresa.repository.DepartamentoRepository;
import com.ejemplo.empresa.repository.EmpleadoRepository;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepo;
    private final EmpleadoRepository empleadoRepo;

    public DepartamentoService(DepartamentoRepository departamentoRepo, EmpleadoRepository empleadoRepo){
        this.departamentoRepo = departamentoRepo;
        this.empleadoRepo = empleadoRepo;
    }

    //LISTAR DEPARTAMENTOS (con el total de planilla de cada uno)
    public List<Departamento> listar(){
        List<Departamento> departamentos = departamentoRepo.findAll();
        for (Departamento d : departamentos){
            d.setTotalPlanilla(empleadoRepo.sumarSalarios(d.getId()));
        }
        return departamentos;
    }

    //Buscar un solo departamento
    public Departamento buscarPorId(Long id){
        return departamentoRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No se encontro el departamento con id " + id));
    }

    //CREAR DEPARTAMENTO
    @Transactional
    public Departamento crear(Departamento departamento){
        if (departamentoRepo.existsByNombreIgnoreCase(departamento.getNombre())){
            throw new IllegalArgumentException("Ya existe un departamento con ese nombre");
        }
        return departamentoRepo.save(departamento);
    }

    //ACTUALIZAR DEPARTAMENTO
    @Transactional
    public Departamento actualizar(Long id, Departamento datos){
        Departamento existente = buscarPorId(id);

        if (departamentoRepo.existsByNombreIgnoreCaseAndIdNot(datos.getNombre(), id)){
            throw new IllegalArgumentException("Ya existe un departamento con ese nombre");
        }

        //el nuevo presupuesto debe alcanzar para pagar a los empleados actuales
        BigDecimal planilla = empleadoRepo.sumarSalarios(id);
        if (datos.getPresupuesto().compareTo(planilla) < 0){
            throw new IllegalArgumentException("El presupuesto no puede ser menor a la planilla actual");
        }

        existente.setNombre(datos.getNombre());
        existente.setUbicacion(datos.getUbicacion());
        existente.setPresupuesto(datos.getPresupuesto());

        return departamentoRepo.save(existente);
    }

    //ELIMINAR DEPARTAMENTO (solo si no tiene empleados)
    @Transactional
    public void eliminar(Long id){
        Departamento departamento = buscarPorId(id);

        if (empleadoRepo.existsByDepartamentoId(id)){
            throw new IllegalArgumentException("No se puede eliminar un departamento con empleados asignados");
        }
        departamentoRepo.delete(departamento);
    }
}