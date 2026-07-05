package com.petsolution.veterinaria.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data               // genera getters, setters, toString, equals, hashCode
@NoArgsConstructor  // constructor vacío — requerido por JdbcTemplate RowMapper
@AllArgsConstructor // constructor con todos los campos
public class Propietario {

    private int    idPropietario;
    private String dni;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String direccion;
}