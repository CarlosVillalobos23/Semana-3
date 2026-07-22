package com.carlos.citas.dto;

import com.carlos.commons.dto.pacientes.DatosPaciente;
import com.carlos.commons.dto.medicos.DatosMedico;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CitaResponse(
        Long id,
        DatosPaciente paciente,
        DatosMedico medico,
        @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd/MM,yyyy HH:mm")
        LocalDateTime fechaCita,
        String sintomas,
        String estadoCita
) {
}
