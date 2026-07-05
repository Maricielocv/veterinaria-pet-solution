package com.petsolution.veterinaria.controller;

import com.petsolution.veterinaria.dao.HistoriaClinicaDAO;
import com.petsolution.veterinaria.dao.MascotaDAO;
import com.petsolution.veterinaria.dao.PropietarioDAO;
import com.petsolution.veterinaria.dao.RegistroVacunacionDAO;
import com.petsolution.veterinaria.entity.HistoriaClinica;
import com.petsolution.veterinaria.entity.Mascota;
import com.petsolution.veterinaria.entity.Propietario;
import com.petsolution.veterinaria.entity.RegistroVacunacion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/mascotas")
public class MascotaController {

    private final MascotaDAO            mascotaDAO;
    private final PropietarioDAO        propietarioDAO;
    private final RegistroVacunacionDAO registroDAO;
    private final HistoriaClinicaDAO    historiaDAO;

    public MascotaController(MascotaDAO mascotaDAO,
                               PropietarioDAO propietarioDAO,
                               RegistroVacunacionDAO registroDAO,
                               HistoriaClinicaDAO historiaDAO) {
        this.mascotaDAO     = mascotaDAO;
        this.propietarioDAO = propietarioDAO;
        this.registroDAO    = registroDAO;
        this.historiaDAO    = historiaDAO;
    }

    // ── LISTAR TODAS (panel médico: lista de pacientes) ───────────────────
    // GET /mascotas
    @GetMapping
    public String listarTodas(Model model) {
        List<Mascota> mascotas = mascotaDAO.listarTodas();
        model.addAttribute("mascotas", mascotas);
        model.addAttribute("paginaActiva", "medico");
        return "medico_view";
    }

    // ── VER HISTORIAL COMPLETO DE UNA MASCOTA ────────────────────────────
    // GET /mascotas/1/historial
    // Mockup: "Historial Médico: Luna" con vacunas y consultas
    @GetMapping("/{id}/historial")
    public String verHistorial(@PathVariable int id, Model model) {
        Mascota mascota = mascotaDAO.buscarPorId(id);

        if (mascota == null) {
            model.addAttribute("mensajeError", "Mascota no encontrada.");
            return "medico_view";
        }

        // Carga propietario de la mascota
        Propietario propietario =
                propietarioDAO.buscarPorId(mascota.getIdPropietario());

        // Carga historial de vacunaciones ordenado por fecha desc
        List<RegistroVacunacion> vacunaciones =
                registroDAO.listarPorMascota(id);

        // Carga historial clínico (consultas, diagnósticos)
        List<HistoriaClinica> historiaClinica =
                historiaDAO.listarPorMascota(id);

        model.addAttribute("mascota",       mascota);
        model.addAttribute("propietario",   propietario);
        model.addAttribute("vacunaciones",  vacunaciones);
        model.addAttribute("historiaClinica", historiaClinica);
        model.addAttribute("paginaActiva",  "medico");
        return "medico_view";
    }

    // ── GUARDAR NUEVA MASCOTA ─────────────────────────────────────────────
    // POST /mascotas/guardar
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Mascota mascota,
                           RedirectAttributes redirectAttrs) {
        try {
            mascotaDAO.insertar(mascota);
            redirectAttrs.addFlashAttribute("mensajeExito",
                    "Mascota registrada correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError",
                    "Error al registrar mascota: " + e.getMessage());
        }
        return "redirect:/asistente";
    }

    // ── ACTUALIZAR ────────────────────────────────────────────────────────
    // POST /mascotas/actualizar
    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Mascota mascota,
                              RedirectAttributes redirectAttrs) {
        try {
            mascotaDAO.actualizar(mascota);
            redirectAttrs.addFlashAttribute("mensajeExito",
                    "Mascota actualizada correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError",
                    "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/medico";
    }

    // ── ELIMINAR ──────────────────────────────────────────────────────────
    // GET /mascotas/eliminar/1
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id,
                            RedirectAttributes redirectAttrs) {
        try {
            mascotaDAO.eliminar(id);
            redirectAttrs.addFlashAttribute("mensajeExito",
                    "Mascota eliminada correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError",
                    "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/medico";
    }
}