package com.qk.mscliente.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ClienteDto(
        Long id,
        @NotNull(message = "El nombre no puede ser nulo")
        @NotEmpty(message = "El nombre no puede estar vacio")
        String nombre,
        @NotNull(message = "El genero no puede ser nulo")
        @Pattern(regexp = "[MFO]", message = "El genero debe ser 'Masculino', 'Femenino' o 'Otro'")
        String genero,
        @NotNull(message = "La edad no puede ser nula")
        @Min(value = 0, message = "La edad debe ser un valor positivo")
        Integer edad,
        @NotNull(message = "La identificacion no puede ser nula")
        String identificacion,
        String direccion,
        String telefono,
        @NotNull(message = "La contrasena no puede ser nula")
        String contrasena,
        Boolean estado) {
}
