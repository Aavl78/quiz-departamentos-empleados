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

import com.ejemplo.empresa.model.Usuario;
import com.ejemplo.empresa.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service){
        this.service = service;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model){
        model.addAttribute("usuarios", service.listar());
        return "usuarios/lista";
    }

    // CREAR
    @GetMapping("/nuevo")
    public String nuevo(Model model){
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("modo", "crear");
        return "usuarios/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("usuario") Usuario usuario,
                          BindingResult errores, Model model, RedirectAttributes flash){
        if (errores.hasErrors()){
            model.addAttribute("modo", "crear");
            return "usuarios/formulario";
        }
        try {
            service.crear(usuario);
            flash.addFlashAttribute("mensaje", "Usuario creado correctamente");
            return "redirect:/usuarios";
        } catch (IllegalArgumentException e){
            model.addAttribute("modo", "crear");
            model.addAttribute("error", e.getMessage());
            return "usuarios/formulario";
        }
    }

    // ACTUALIZAR
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash){
        try {
            Usuario u = service.buscarPorId(id);
            u.setPassword("");   //la contrasena no se muestra en el formulario
            model.addAttribute("usuario", u);
            model.addAttribute("modo", "editar");
            return "usuarios/formulario";
        } catch (IllegalArgumentException e){
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios";
        }
    }

    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("usuario") Usuario usuario,
                             BindingResult errores, Model model, RedirectAttributes flash){
        if (errores.hasErrors()){
            usuario.setId(id);
            model.addAttribute("modo", "editar");
            return "usuarios/formulario";
        }
        try {
            service.actualizar(id, usuario);
            flash.addFlashAttribute("mensaje", "Usuario actualizado correctamente");
            return "redirect:/usuarios";
        } catch (IllegalArgumentException e){
            usuario.setId(id);
            model.addAttribute("modo", "editar");
            model.addAttribute("error", e.getMessage());
            return "usuarios/formulario";
        }
    }

    // ELIMINAR
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash){
        try {
            service.eliminar(id);
            flash.addFlashAttribute("mensaje", "Usuario eliminado correctamente");
        } catch (IllegalArgumentException e){
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios";
    }
}