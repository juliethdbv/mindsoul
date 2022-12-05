package com.sena.mindsoul.entity;

import java.util.List;

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
@Table(name = "specialities")

public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    @Size(min = 2, max = 50)
    private String name; // Tipo De Especialidad

    @Column(name = "description", length = 2000, nullable = false)
    @Size(min = 2, max = 2000)
    private String description;

    // Estado Especialidad

    @Column(name = "stateSpecialty")
    private Boolean stateSpecialty = true;

    // Relación entre Usuario y Especialidad

    @OneToMany(mappedBy = "specialities", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> user;

}
