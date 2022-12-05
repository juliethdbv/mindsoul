package com.sena.mindsoul.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// Implementa los getter
@Getter

// Implementa los setter
@Setter

// Implementa un contructor con parametros y uno sin
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users")
public class User {
    // Esto sirve pues es el id único que identifica una clase cuando lo
    // serializamos
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    // Estado Usuario

    @Column
    private Boolean statusUser = true;

    // LAZY = fetch cuando se necesita
    // EAGER = fetch inmediatamente

    // La operación de merge copia el estado del objeto dado en el objeto
    // persistente con el mismo identificador.
    // CascadeType.MERGE propaga la operación de fusión de una entidad principal a
    // una entidad secundaria.
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "users_roles", joinColumns = {
            @JoinColumn(name = "USER_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
    private List<Role> roles = new ArrayList<>();

    // Relación Usuario con Especialidad

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialities") // Nombre llave foránea
    private Specialty specialities;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    // Contructor 

    public User(Long id, @Size(min = 2, max = 40) String name, @Size(min = 2, max = 30) String email,
            @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres") String password,
            List<Role> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

}
