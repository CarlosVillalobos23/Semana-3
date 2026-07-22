package com.carlos.commons.dto.pacientes;

import jakarta.validation.constraints.*;

public record PacienteRequest(
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
        Integer edad,
        @NotBlank(message = " El peso es requerido")
        @DecimalMin(value="1.0",message = "El peso minimo es de 1 kg")
        @DecimalMax(value="20.0",message = "El peso maximo es de 200 kg")
        Double peso,
        @NotBlank(message = " El peso es requerido")
        @DecimalMin(value="1.0",message = "La estatura minima es 1 m")
        @DecimalMax(value="20.0",message = "La estatura maxima permitida es de 2 Metros")
        Double estatura,
        @NotBlank(message = "El email es requerido")
        @Size(min=1,max = 100,message = "El email debe tener entr 1 y 100 caracteres")
        @Email(message = "El email debe tener el formato correcto(Correo@Dominio)")
        String email,
        @NotBlank(message = "El telefono es requerido")
        @Pattern(regexp = "^[0-9]{10}$",message = "El telefono debe contener solo 10 digitos numericos")
        String telefono,
        @NotBlank(message = "La direccion es requerid")
        @Size(min=1,max = 150,message = "La direccionl debe tener entr 1 y 150 caracteres")
        String direccion
) {
}
