package com.ejemplo.empresa.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ejemplo.empresa.model.Departamento;
import com.ejemplo.empresa.model.Empleado;
import com.ejemplo.empresa.repository.EmpleadoRepository;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepo;
    private final DepartamentoService departamentoService;

    public EmpleadoService(EmpleadoRepository empleadoRepo, DepartamentoService departamentoService){
        this.empleadoRepo = empleadoRepo;
        this.departamentoService = departamentoService;
    }

    //Leer todos los empleados
    public List<Empleado> listar(){
        return empleadoRepo.findAll();
    }

    //Obtener los empleados de un departamento
    public List<Empleado> listarPorDepartamento(Long departamentoId){
        return empleadoRepo.findByDepartamentoId(departamentoId);
    }

    //Obtener un solo empleado
    public Empleado buscarPorId(Long id){
        return empleadoRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No se encontro el empleado con id " + id));
    }

    //CREAR EMPLEADO
    @Transactional
    public Empleado crear(Empleado empleado, Long departamentoId){
        Departamento departamento = obtenerDepartamento(departamentoId);

        if (empleadoRepo.existsByCedula(empleado.getCedula())){
            throw new IllegalArgumentException("La cédula ya está registrada");
        }

        //planilla actual + salario nuevo no puede pasar el presupuesto
        BigDecimal planilla = empleadoRepo.sumarSalarios(departamentoId);
        BigDecimal nuevaPlanilla = planilla.add(empleado.getSalario());
        if (nuevaPlanilla.compareTo(departamento.getPresupuesto()) > 0){
            throw new IllegalArgumentException("El salario excede el presupuesto disponible del departamento");
        }

        departamento.agregarEmpleado(empleado);
        return empleadoRepo.save(empleado);
    }

    //ACTUALIZAR EMPLEADO
    @Transactional
    public Empleado actualizar(Long id, Empleado datos, Long departamentoId){
        Empleado existente = buscarPorId(id);
        Departamento nuevoDepartamento = obtenerDepartamento(departamentoId);

        if (empleadoRepo.existsByCedulaAndIdNot(datos.getCedula(), id)){
            throw new IllegalArgumentException("La cédula ya está registrada");
        }

        //se valida contra el departamento elegido, sin contar el salario viejo del empleado
        BigDecimal planilla = empleadoRepo.sumarSalariosSinEmpleado(departamentoId, id);
        BigDecimal nuevaPlanilla = planilla.add(datos.getSalario());
        if (nuevaPlanilla.compareTo(nuevoDepartamento.getPresupuesto()) > 0){
            throw new IllegalArgumentException("El salario excede el presupuesto disponible del departamento");
        }

        existente.setNombre(datos.getNombre());
        existente.setCedula(datos.getCedula());
        existente.setEmail(datos.getEmail());
        existente.setPuesto(datos.getPuesto());
        existente.setSalario(datos.getSalario());
        existente.setFechaIngreso(datos.getFechaIngreso());

        //si cambio de departamento: sale del anterior y entra al nuevo
        if (!existente.getDepartamento().getId().equals(departamentoId)){
            existente.getDepartamento().quitarEmpleado(existente);
            nuevoDepartamento.agregarEmpleado(existente);
        }

        return empleadoRepo.save(existente);
    }

    //ELIMINAR EMPLEADO
    @Transactional
    public void eliminar(Long id){
        Empleado empleado = buscarPorId(id);
        empleado.getDepartamento().quitarEmpleado(empleado);
        empleadoRepo.delete(empleado);
    }

    //Obtener departamento, todo empleado debe tener uno
    private Departamento obtenerDepartamento(Long departamentoId){
        if (departamentoId == null){
            throw new IllegalArgumentException("Debe seleccionar un departamento");
        }
        return departamentoService.buscarPorId(departamentoId);
    }
}