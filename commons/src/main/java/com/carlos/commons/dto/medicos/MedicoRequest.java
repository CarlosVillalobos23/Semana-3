package com.carlos.commons.dto.medicos;

import jakarta.validation.constraints.*;

public record MedicoRequest(
        @NotBlank(message = "El nombre es requerido")
        @Size(min=1,max = 50,message = "El nombre debe tener entr 1 y 50 caracteres")
        String nombre,
        @NotBlank(message = "El apellido es requerido")
        @Size(min=1,max = 50,message = "El apellido debe tener entr 1 y 50 caracteres")
        String apellidoPaterno,

        @NotBlank(message = "El apellido es requerido")
        @Size(min=1,max = 50,message = "El apellido debe tener entr 1 y 50 caracteres")
        String apellidoMaterno,

        @NotBlank(message = "La edad es requerida")
        @Min(value=18,message = "La edad minima es de 18 años")
        @Max(value=100,message = "La edad maxima es de 100 años")
        Short edad,
        @NotBlank(message = "El email es requerido")
        @Size(min=1,max = 100,message = "El email debe tener entr 1 y 100 caracteres")
        @Email(message = "El email debe tener el formato correcto(Correo@Dominio)")
        String email,

        @NotBlank(message = "El telefono es requerido")
        @Pattern(regexp = "^[0-9]{10}$",message = "El telefono debe contener solo 10 digitos numericos")
        String telefono,
        @NotBlank(message = "La cedula es requerida")
        @Size(min=12,max = 12,message = "El email debe tener 12 caracteres")
        String cedulaProfesional,
        @NotNull(message = "La id de especialidda es requerida")
        Long idEspecialidad
) {
}
