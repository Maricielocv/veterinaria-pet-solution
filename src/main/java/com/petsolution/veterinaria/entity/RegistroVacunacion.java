package com.petsolution.veterinaria.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroVacunacion {

    private int       idRegistro;
    private int       idMascota;
    private String    tipoVacuna;
    private String    numeroDeLote;
    private LocalDate fechaAplicacion;
    private LocalDate proximaDosis;
    // Estados del diagrama de tiempo: PENDIENTE → APLICADA → PROGRAMADA
    private String    estado;
    private String    observaciones;
}