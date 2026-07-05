package com.petsolution.veterinaria.controller;

import com.petsolution.veterinaria.dao.MascotaDAO;
import com.petsolution.veterinaria.dao.PropietarioDAO;
import com.petsolution.veterinaria.entity.Mascota;
import com.petsolution.veterinaria.entity.Propietario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/propietarios")
public class PropietarioController {

    // Inyección por constructor: mejor práctica con Spring
    private final PropietarioDAO propietarioDAO;
    private final MascotaDAO mascotaDAO;

    public PropietarioController(PropietarioDAO propietarioDAO,
                                  MascotaDAO mascotaDAO) {
        this.propietarioDAO = propietarioDAO;
        this.mascotaDAO     = mascotaDAO;
    }

    // ── LISTAR TODOS ──────────────────────────────────────────────────────
    // GET /propietarios → tabla de propietarios en asistente_view
    @GetMapping
    public String listar(Model model) {
        List<Propietario> propietarios = propietarioDAO.listarTodos();
        model.addAttribute("propietarios", propietarios);
        model.addAttribute("paginaActiva", "asistente");
        return "asistente_view";
    }

    // ── BUSCAR POR DNI (AJAX-ready o formulario) ──────────────────────────
    // GET /propietarios/buscar?dni=47123456
    @GetMapping("/buscar")
    public String buscarPorDni(@RequestParam String dni, Model model) {
        Propietario propietario = propietarioDAO.buscarPorDni(dni);

        if (propietario != null) {
            // Si encontró al propietario, carga también sus mascotas
            List<Mascota> mascotas =
                    mascotaDAO.listarPorPropietario(propietario.getIdPropietario());
            model.addAttribute("propietario", propietario);
            model.addAttribute("mascotas", mascotas);
        } else {
            // Si no existe, manda mensaje de aviso a la vista
            model.addAttribute("mensajeError",
                    "No se encontró un propietario con DNI: " + dni);
        }

        model.addAttribute("paginaActiva", "asistente");
        return "asistente_view";
    }

    // ── GUARDAR NUEVO PROPIETARIO ─────────────────────────────────────────
    // POST /propietarios/guardar (formulario de "Nueva Registro" del mockup)
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Propietario propietario,
                           RedirectAttributes redirectAttrs) {
        try {
            propietarioDAO.insertar(propietario);
            redirectAttrs.addFlashAttribute("mensajeExito",
                    "Propietario registrado correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError",
                    "Error al registrar: " + e.getMessage());
        }
        return "redirect:/asistente";
    }

    // ── VER DETALLE DE UN PROPIETARIO CON SUS MASCOTAS ───────────────────
    // GET /propietarios/1
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable int id, Model model) {
        Propietario propietario = propietarioDAO.buscarPorId(id);
        List<Mascota> mascotas  = mascotaDAO.listarPorPropietario(id);

        model.addAttribute("propietario", propietario);
        model.addAttribute("mascotas", mascotas);
        model.addAttribute("paginaActiva", "asistente");
        return "propietario_view";
    }

    // ── ACTUALIZAR ────────────────────────────────────────────────────────
    // POST /propietarios/actualizar
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Propietario propietario,
                              RedirectAttributes redirectAttrs) {
        try {
            propietarioDAO.actualizar(propietario);
            redirectAttrs.addFlashAttribute("mensajeExito",
                    "Propietario actualizado correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError",
                    "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/asistente";
    }

    // ── ELIMINAR ──────────────────────────────────────────────────────────
    // GET /propietarios/eliminar/1
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id,
                            RedirectAttributes redirectAttrs) {
        try {
            propietarioDAO.eliminar(id);
            redirectAttrs.addFlashAttribute("mensajeExito",
                    "Propietario eliminado correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError",
                    "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/asistente";
    }
}