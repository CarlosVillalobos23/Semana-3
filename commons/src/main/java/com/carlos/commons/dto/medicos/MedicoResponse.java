package com.carlos.commons.dto.medicos;

public record MedicoResponse(
        Long id,
        String nombre,
        String apellidoPaterno, String apellidoMaterno, Short edad,
        String email,
        String telefono,
        String cedula,
        String especialidad,
        String disponibilidad,
        String estado
){
}
