package com.ejemplo.empresa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Muestra la pagina de inicio de sesion
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    // La pagina principal envia a la lista de departamentos
    @GetMapping("/")
    public String inicio(){
        return "redirect:/departamentos";
    }
}