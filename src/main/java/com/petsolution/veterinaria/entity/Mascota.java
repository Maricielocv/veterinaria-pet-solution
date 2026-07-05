package com.petsolution.veterinaria.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mascota {

    private int    idMascota;
    private int    idPropietario;
    private String nombre;
    private String especie;    // Canino, Felino, Aviario…
    private String raza;
    private int    edad;
    private String sexo;       // M / F
}