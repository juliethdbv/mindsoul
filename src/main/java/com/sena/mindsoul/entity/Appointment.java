package com.sena.mindsoul.entity;

import java.util.List;

import jakarta.persistence.*;

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
@Table(name = "appointment")

public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "hour", nullable = false)
    private String hour;

    @Column(name = "time", nullable = false)
    private String time;

    @Column(name = "link", nullable = false)
    private String link;

    @Column(name = "doctor", nullable = false)
    private String doctor;
    // Estado de Cita

    @Column(name = "stateAppointment" )
    private Boolean stateAppointment = true; 

    @Column(name = "stateCancel")
    private Boolean stateCancel = false;

    @Column(name = "motive", nullable = false)
    private String motive = "";

    // Relación Cita y Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users") // Nombre llave foránea
    private User users; // Trae Nombre del Medico

    // Relacion Cita e Historial
    @OneToMany(mappedBy = "appointments", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Record> records;

}
