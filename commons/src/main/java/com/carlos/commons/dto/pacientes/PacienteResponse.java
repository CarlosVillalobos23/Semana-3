package com.carlos.commons.dto.pacientes;

public record PacienteResponse(
        Long id,
        String nombre,
        Short edad,
        Double peso,
        Double estatura,
        Double imc,
        String email,
        String telefono,
        String direccopm,
        String numExpediente
) {
}
