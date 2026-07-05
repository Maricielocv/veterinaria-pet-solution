package com.petsolution.veterinaria.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinica {

    private int       idHistoria;
    private int       idMascota;
    private LocalDate fechaConsulta;
    private String    motivoConsulta;
    private String    diagnostico;
    private String    tratamiento;
    private String    veterinarioResponsable;
}