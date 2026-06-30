package com.petsolution.veterinaria.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "t_mascota")
@Data
public class Mascota {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMascota;
    private String nombre;
    private String especie;
    private String estadoSalud; // Para el indicador visual de RSU
}