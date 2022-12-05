package com.sena.mindsoul.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Implementa los getter
@Setter
// Implementa los setter
@Getter

// Implementa un contrcutor con argumentos y otro sin
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "records")

public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "diagnosis", length = 1000, nullable = false)
    @Size(min = 2, max = 1000)
    private String diagnosis;

    // Relación entre Historial Medico y Cita

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointments") // Nombre llave foránea
    private Appointment appointments;

}
