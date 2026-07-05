package com.petsolution.veterinaria.controller;

import com.petsolution.veterinaria.dao.MascotaDAO;
import com.petsolution.veterinaria.dao.OrdenServicioDAO;
import com.petsolution.veterinaria.dao.PropietarioDAO;
import com.petsolution.veterinaria.dao.RegistroVacunacionDAO;
import com.petsolution.veterinaria.entity.Mascota;
import com.petsolution.veterinaria.entity.OrdenServicio;
import com.petsolution.veterinaria.entity.Propietario;
import com.petsolution.veterinaria.entity.RegistroVacunacion;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/vacunacion")
public class VacunacionController {

    private final RegistroVacunacionDAO registroDAO;
    private final MascotaDAO            mascotaDAO;
    private final PropietarioDAO        propietarioDAO;
    private final OrdenServicioDAO      ordenDAO;

    public VacunacionController(RegistroVacunacionDAO registroDAO,
                                 MascotaDAO mascotaDAO,
                                 PropietarioDAO propietarioDAO,
                                 OrdenServicioDAO ordenDAO) {
        this.registroDAO    = registroDAO;
        this.mascotaDAO     = mascotaDAO;
        this.propietarioDAO = propietarioDAO;
        this.ordenDAO       = ordenDAO;
    }

    // ── PANEL MÉDICO COMPLETO ─────────────────────────────────────────────
    // GET /vacunacion/panel-medico
    // Carga todo lo que necesita medico_view.html:
    //   - Lista de mascotas (pacientes en espera)
    //   - Próximas dosis con recordatorio
    //   - Citas del día
    @GetMapping("/panel-medico")
    public String panelMedico(Model model) {

        // Pacientes en espera (todas las mascotas activas)
        List<Mascota> pacientes = mascotaDAO.listarTodas();

        // Vacunas próximas a vencer (próximos 7 días)
        List<RegistroVacunacion> proximasDosis =
                registroDAO.listarProximasDosis();

        // Citas del día para la agenda
        List<OrdenServicio> citasHoy = ordenDAO.listarCitasHoy();

        model.addAttribute("pacientes",     pacientes);
        model.addAttribute("proximasDosis", proximasDosis);
        model.addAttribute("citasHoy",      citasHoy);
        model.addAttribute("paginaActiva",  "medico");

        // Objeto vacío para el formulario "Nueva Vacunación" del mockup
        model.addAttribute("nuevaVacuna", new RegistroVacunacion());

        return "medico_view";
    }

    // ── PANEL PROPIETARIO COMPLETO ────────────────────────────────────────
    // GET /vacunacion/panel-propietario?idPropietario=1
    // Carga todo lo que necesita propietario_view.html:
    //   - Sus mascotas con estado AL DÍA / PENDIENTE
    //   - Próximos refuerzos
    @GetMapping("/panel-propietario")
    public String panelPropietario(
            @RequestParam(defaultValue = "1") int idPropietario,
            Model model) {

        Propietario propietario =
                propietarioDAO.buscarPorId(idPropietario);

        List<Mascota> mismascotas =
                mascotaDAO.listarPorPropietario(idPropietario);

        // Para cada mascota obtiene sus vacunaciones (historial + próximas)
        model.addAttribute("propietario",  propietario);
        model.addAttribute("misMascotas",  mismascotas);
        model.addAttribute("paginaActiva", "propietario");

        return "propietario_view";
    }

    // ── PANEL ASISTENTE COMPLETO ──────────────────────────────────────────
    // GET /vacunacion/panel-asistente
    // Carga todo lo que necesita asistente_view.html:
    //   - Agenda de citas del día
    //   - Recordatorios de vacunación vencida/próxima
    @GetMapping("/panel-asistente")
    public String panelAsistente(Model model) {

        List<OrdenServicio>      citasHoy      = ordenDAO.listarCitasHoy();
        List<RegistroVacunacion> proximasDosis = registroDAO.listarProximasDosis();
        List<Propietario>        propietarios  = propietarioDAO.listarTodos();

        model.addAttribute("citasHoy",      citasHoy);
        model.addAttribute("proximasDosis", proximasDosis);
        model.addAttribute("propietarios",  propietarios);
        model.addAttribute("paginaActiva",  "asistente");

        // Objeto vacío para el formulario "Nueva Registro" del mockup
        model.addAttribute("nuevoPropietario", new Propietario());
        model.addAttribute("nuevaMascota",     new Mascota());

        return "asistente_view";
    }

    // ── REGISTRAR NUEVA VACUNACIÓN ────────────────────────────────────────
    // POST /vacunacion/registrar
    // Es el botón "Registrar Aplicación" del mockup del médico
    @PostMapping("/registrar")
    public String registrarVacunacion(
            // @RequestParam captura cada campo del formulario HTML
            @RequestParam int idMascota,
            @RequestParam String tipoVacuna,
            @RequestParam String numeroDeLote,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate fechaAplicacion,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate proximaDosis,
            @RequestParam(defaultValue = "") String observaciones,
            RedirectAttributes redirectAttrs) {

        try {
            // Verifica que la mascota exista antes de insertar
            // (rol "verifica" del diagrama de estructura compuesta)
            Mascota mascota = mascotaDAO.buscarPorId(idMascota);
            if (mascota == null) {
                redirectAttrs.addFlashAttribute("mensajeError",
                        "La mascota indicada no existe en el sistema.");
                return "redirect:/vacunacion/panel-medico";
            }

            // Construye el objeto con estado APLICADA
            // (transición t1 del diagrama de tiempo)
            RegistroVacunacion registro = new RegistroVacunacion();
            registro.setIdMascota(idMascota);
            registro.setTipoVacuna(tipoVacuna);
            registro.setNumeroDeLote(numeroDeLote);
            registro.setFechaAplicacion(fechaAplicacion);
            registro.setProximaDosis(proximaDosis);
            registro.setObservaciones(observaciones);

            // Si hay próxima dosis asignada → estado PROGRAMADA
            // Si no → estado APLICADA (se calcula después, restricción ≤24h)
            registro.setEstado(proximaDosis != null ? "PROGRAMADA" : "APLICADA");

            // Persiste el registro (rol "persiste" de estructura compuesta)
            registroDAO.insertar(registro);

            redirectAttrs.addFlashAttribute("mensajeExito",
                    "Vacunación registrada correctamente para " +
                    mascota.getNombre() + ".");

        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError",
                    "Error al registrar vacunación: " + e.getMessage());
        }

        return "redirect:/vacunacion/panel-medico";
    }

    // ── VER HISTORIAL DE VACUNACIÓN DE UNA MASCOTA ───────────────────────
    // GET /vacunacion/historial/1
    @GetMapping("/historial/{idMascota}")
    public String verHistorial(@PathVariable int idMascota, Model model) {

        Mascota mascota = mascotaDAO.buscarPorId(idMascota);
        List<RegistroVacunacion> historial =
                registroDAO.listarPorMascota(idMascota);

        model.addAttribute("mascota",      mascota);
        model.addAttribute("historial",    historial);
        model.addAttribute("paginaActiva", "medico");

        return "medico_view";
    }

    // ── AGENDAR CITA (botón "Registrar y Agendar" del asistente) ─────────
    // POST /vacunacion/agendar
    @PostMapping("/agendar")
    public String agendarCita(
            @RequestParam int    idMascota,
            @RequestParam String tipoServicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate fechaOrden,
            @RequestParam(defaultValue = "PENDIENTE") String estado,
            @RequestParam(defaultValue = "") String descripcion,
            RedirectAttributes redirectAttrs) {

        try {
            OrdenServicio orden = new OrdenServicio();
            orden.setIdMascota(idMascota);
            orden.setTipoServicio(tipoServicio);
            orden.setFechaOrden(fechaOrden);
            orden.setEstado(estado);
            orden.setDescripcion(descripcion);
            orden.setCosto(0.0); // El costo se asigna al completar el servicio

            ordenDAO.insertar(orden);

            redirectAttrs.addFlashAttribute("mensajeExito",
                    "Cita agendada correctamente.");

        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError",
                    "Error al agendar cita: " + e.getMessage());
        }

        return "redirect:/vacunacion/panel-asistente";
    }

    // ── CAMBIAR ESTADO DE CITA (botón "Gestionar" del asistente) ─────────
    // POST /vacunacion/cita/estado
    @PostMapping("/cita/estado")
    public String cambiarEstadoCita(
            @RequestParam int    idOrden,
            @RequestParam String nuevoEstado,
            RedirectAttributes redirectAttrs) {

        try {
            ordenDAO.cambiarEstado(idOrden, nuevoEstado);
            redirectAttrs.addFlashAttribute("mensajeExito",
                    "Estado actualizado a: " + nuevoEstado);
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("mensajeError",
                    "Error al cambiar estado: " + e.getMessage());
        }

        return "redirect:/vacunacion/panel-asistente";
    }
}