package com.ejemplo.empresa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ejemplo.empresa.model.Empleado;
import com.ejemplo.empresa.model.Puesto;
import com.ejemplo.empresa.service.DepartamentoService;
import com.ejemplo.empresa.service.EmpleadoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final DepartamentoService departamentoService;

    public EmpleadoController(EmpleadoService empleadoService, DepartamentoService departamentoService){
        this.empleadoService = empleadoService;
        this.departamentoService = departamentoService;
    }

    // LISTAR (todos o filtrados por departamento)
    @GetMapping
    public String listar(@RequestParam(required = false) Long departamentoId,
                         Model model, RedirectAttributes flash){
        if (departamentoId != null){
            try {
                model.addAttribute("departamento", departamentoService.buscarPorId(departamentoId));
                model.addAttribute("empleados", empleadoService.listarPorDepartamento(departamentoId));
            } catch (IllegalArgumentException e){
                flash.addFlashAttribute("error", e.getMessage());
                return "redirect:/empleados";
            }
        } else {
            model.addAttribute("empleados", empleadoService.listar());
        }
        return "empleados/lista";
    }

    // CREAR
    @GetMapping("/nuevo")
    public String nuevo(@RequestParam(required = false) Long departamentoId, Model model){
        model.addAttribute("empleado", new Empleado());
        prepararFormulario(model, "crear", departamentoId);   // departamentoId preselecciona el departamento
        return "empleados/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("empleado") Empleado empleado,
                          BindingResult errores,
                          @RequestParam(required = false) Long departamentoId,
                          Model model, RedirectAttributes flash){
        if (errores.hasErrors()){
            prepararFormulario(model, "crear", departamentoId);
            return "empleados/formulario";
        }
        try {
            empleadoService.crear(empleado, departamentoId);
            flash.addFlashAttribute("mensaje", "Empleado registrado correctamente");
            return "redirect:/empleados?departamentoId=" + departamentoId;
        } catch (IllegalArgumentException e){
            prepararFormulario(model, "crear", departamentoId);
            model.addAttribute("error", e.getMessage());
            return "empleados/formulario";
        }
    }

    // ACTUALIZAR
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash){
        try {
            Empleado empleado = empleadoService.buscarPorId(id);
            model.addAttribute("empleado", empleado);
            prepararFormulario(model, "editar", empleado.getDepartamento().getId());
            return "empleados/formulario";
        } catch (IllegalArgumentException e){
            //si el id no existe se regresa a la lista con un mensaje
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/empleados";
        }
    }

    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("empleado") Empleado empleado,
                             BindingResult errores,
                             @RequestParam(required = false) Long departamentoId,
                             Model model, RedirectAttributes flash){
        if (errores.hasErrors()){
            empleado.setId(id);
            prepararFormulario(model, "editar", departamentoId);
            return "empleados/formulario";
        }
        try {
            empleadoService.actualizar(id, empleado, departamentoId);
            flash.addFlashAttribute("mensaje", "Empleado actualizado correctamente");
            return "redirect:/empleados?departamentoId=" + departamentoId;
        } catch (IllegalArgumentException e){
            empleado.setId(id);
            prepararFormulario(model, "editar", departamentoId);
            model.addAttribute("error", e.getMessage());
            return "empleados/formulario";
        }
    }

    // ELIMINAR
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash){
        try {
            empleadoService.eliminar(id);
            flash.addFlashAttribute("mensaje", "Empleado eliminado correctamente");
        } catch (IllegalArgumentException e){
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/empleados";
    }

    // Datos que necesita el formulario: modo, lista de departamentos, puestos y departamento elegido
    private void prepararFormulario(Model model, String modo, Long departamentoId){
        model.addAttribute("modo", modo);
        model.addAttribute("departamentos", departamentoService.listar());
        model.addAttribute("puestos", Puesto.values());
        model.addAttribute("departamentoId", departamentoId);
    }
}