package com.petsolution.veterinaria.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenServicio {

    private int       idOrden;
    private int       idMascota;
    private String    tipoServicio;  // Consulta, Vacunación, Cirugía…
    private LocalDate fechaOrden;
    // Estados: PENDIENTE, EN_SALA, CONFIRMADO, COMPLETADO
    private String    estado;
    private String    descripcion;
    private double    costo;

    public String getCssEstado() {
    return switch (this.estado) {
        case "EN_SALA" -> "badge-en-sala";
        case "CONFIRMADO" -> "badge-confirmado";
        case "COMPLETADO" -> "badge-al-dia";
        default -> "badge-pendiente";
    };
}
}