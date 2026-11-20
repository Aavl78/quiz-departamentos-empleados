package com.ejemplo.empresa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ejemplo.empresa.model.Departamento;
import com.ejemplo.empresa.service.DepartamentoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/departamentos")
public class DepartamentoController {

    private final DepartamentoService service;

    public DepartamentoController(DepartamentoService service){
        this.service = service;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model){
        model.addAttribute("departamentos", service.listar());
        return "departamentos/lista";
    }

    // CREAR
    @GetMapping("/nuevo")
    public String nuevo(Model model){
        model.addAttribute("departamento", new Departamento());
        model.addAttribute("modo", "crear");
        return "departamentos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("departamento") Departamento departamento,
                          BindingResult errores, Model model, RedirectAttributes flash){
        if (errores.hasErrors()){
            model.addAttribute("modo", "crear");
            return "departamentos/formulario";
        }
        try {
            service.crear(departamento);
            flash.addFlashAttribute("mensaje", "Departamento creado correctamente");
            return "redirect:/departamentos";
        } catch (IllegalArgumentException e){
            model.addAttribute("modo", "crear");
            model.addAttribute("error", e.getMessage());
            return "departamentos/formulario";
        }
    }

    // ACTUALIZAR
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash){
        try {
            model.addAttribute("departamento", service.buscarPorId(id));
            model.addAttribute("modo", "editar");
            return "departamentos/formulario";
        } catch (IllegalArgumentException e){
            //si el id no existe se regresa a la lista con un mensaje
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/departamentos";
        }
    }

    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("departamento") Departamento departamento,
                             BindingResult errores, Model model, RedirectAttributes flash){
        if (errores.hasErrors()){
            departamento.setId(id);
            model.addAttribute("modo", "editar");
            return "departamentos/formulario";
        }
        try {
            service.actualizar(id, departamento);
            flash.addFlashAttribute("mensaje", "Departamento actualizado correctamente");
            return "redirect:/departamentos";
        } catch (IllegalArgumentException e){
            departamento.setId(id);
            model.addAttribute("modo", "editar");
            model.addAttribute("error", e.getMessage());
            return "departamentos/formulario";
        }
    }

    // ELIMINAR
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash){
        try {
            service.eliminar(id);
            flash.addFlashAttribute("mensaje", "Departamento eliminado correctamente");
        } catch (IllegalArgumentException e){
            //el error se muestra en la lista como alerta roja
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/departamentos";
    }
}