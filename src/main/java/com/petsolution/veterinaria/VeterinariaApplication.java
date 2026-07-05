package com.petsolution.veterinaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VeterinariaApplication {

	public static void main(String[] args) {
        SpringApplication.run(VeterinariaApplication.class, args);
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║   Pet Solutions - Sistema de Vacunación      ║");
        System.out.println("║   http://localhost:8080                       ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }
}
