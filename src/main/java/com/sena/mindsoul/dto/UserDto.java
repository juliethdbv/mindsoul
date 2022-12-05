package com.sena.mindsoul.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// implementa los getters
@Getter
// Implementa los setters
@Setter
// Implementa un contruc
@NoArgsConstructor
@AllArgsConstructor

// Esta clase es para manipulación de datos no será mappeada a la base de datos
public class UserDto
{
    private Long id;

    @NotEmpty
    @Column(nullable=false)
    private String firstName;

    @NotEmpty
    @Column(nullable=false)
    private String lastName;

    @NotEmpty(message = "El correo no debe estar vacío")
    @Column(name = "email", length = 30, nullable=false, unique=true)
    @Size(min = 5, max = 30)
    @Email
    private String email;

    @NotEmpty(message = "La contraseña no bebe estar vacía")
    @Column(name = "password", length = 25, nullable=false)
    @Size(min = 8, max = 30)
    private String password;
}
