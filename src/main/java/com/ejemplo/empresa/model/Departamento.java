package com.ejemplo.empresa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departamentos")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del departamento es obligatorio")
    @Size(max = 60, message = "Maximo 60 caracteres")
    @Column(nullable = false, length = 60)
    private String nombre;

    @NotBlank(message = "La ubicacion es obligatoria")
    @Size(max = 100, message = "Maximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String ubicacion;

    @NotNull(message = "El presupuesto es obligatorio")
    @PositiveOrZero(message = "El presupuesto no puede ser negativo")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal presupuesto;

    // Lado "uno" de la relacion, sin borrado en cascada
    @OneToMany(mappedBy = "departamento")
    private List<Empleado> empleados = new ArrayList<>();

    // Suma de salarios, no se guarda en la base de datos
    @Transient
    private BigDecimal totalPlanilla = BigDecimal.ZERO;

    public Departamento(){}

    public Departamento(String nombre, String ubicacion, BigDecimal presupuesto){
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.presupuesto = presupuesto;
    }

    // Agrega un empleado y le asigna este departamento
    public void agregarEmpleado(Empleado empleado){
        empleados.add(empleado);
        empleado.setDepartamento(this);
    }

    // Quita un empleado de la lista y lo desvincula
    public void quitarEmpleado(Empleado empleado){
        empleados.remove(empleado);
        empleado.setDepartamento(null);
    }

    // Presupuesto que queda libre despues de pagar la planilla
    public BigDecimal getDisponible(){
        if (presupuesto == null){
            return BigDecimal.ZERO;
        }
        return presupuesto.subtract(totalPlanilla);
    }

    //Getters y Setters
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getUbicacion(){
        return ubicacion;
    }

    public void setUbicacion(String ubicacion){
        this.ubicacion = ubicacion;
    }

    public BigDecimal getPresupuesto(){
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto){
        this.presupuesto = presupuesto;
    }

    public List<Empleado> getEmpleados(){
        return empleados;
    }

    public BigDecimal getTotalPlanilla(){
        return totalPlanilla;
    }

    public void setTotalPlanilla(BigDecimal totalPlanilla){
        this.totalPlanilla = totalPlanilla;
    }
}