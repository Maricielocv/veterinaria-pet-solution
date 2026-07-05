package com.petsolution.veterinaria.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /*
     * Ruta raíz → muestra el dashboard general (index.html)
     * En el mockup es la pantalla con los 3 KPIs:
     * Total Patients / Pending Vaccines / Appointments
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("paginaActiva", "home");
        return "index"; // → templates/index.html
    }

    /*
     * Ruta directa al panel del médico veterinario
     * Mockup: "Panel Clínico Veterinario"
     */
    @GetMapping("/medico")
    public String medico(Model model) {
        model.addAttribute("paginaActiva", "medico");
        return "medico_view"; // → templates/medico_view.html
    }

    /*
     * Ruta directa al panel del propietario
     * Mockup: "Mi Panel de Mascotas"
     */
    @GetMapping("/propietario")
    public String propietario(Model model) {
        model.addAttribute("paginaActiva", "propietario");
        return "propietario_view"; // → templates/propietario_view.html
    }

    /*
     * Ruta directa al panel del asistente
     * Mockup: "Gestión Administrativa y Citas"
     */
    @GetMapping("/asistente")
    public String asistente(Model model) {
        model.addAttribute("paginaActiva", "asistente");
        return "asistente_view"; // → templates/asistente_view.html
    }
}