package com.petsolution.veterinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    // NUEVO MÉTODO: Mapea la raíz principal para evitar el error 404
    @GetMapping("/")
    public String inicio() {
        return "index"; // Buscará index.html dentro de templates
    }

    @GetMapping("/medico")
    public String medico(Model model) {
        model.addAttribute("rol", "Médico Veterinario");
        return "medico_view"; // Buscará medico_view.html
    }

    @GetMapping("/asistente")
    public String asistente(Model model) {
        model.addAttribute("rol", "Asistente Veterinario");
        return "asistente_view"; // Buscará asistente_view.html
    }

    @GetMapping("/propietario")
    public String propietario(Model model) {
        model.addAttribute("rol", "Propietario de Mascota");
        return "propietario_view"; // Buscará propietario_view.html o propietario.html
    }
}