package com.petsolution.veterinaria.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.petsolution.veterinaria.entity.Mascota;

public interface MascotaRepository extends JpaRepository<Mascota, Integer> {}