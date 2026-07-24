package com.carlos.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "citas")
public interface CitaClient {

    @GetMapping("/medico/{idMedico}/activa")
    boolean obtenerCitaActivaConMedicoId(@PathVariable("idMedico") Long idMedico);
    @GetMapping("/paciente/{idPaciente}/activa")
    boolean obtenerCitaActivaConPacienteId(@PathVariable("idPaciente") Long idPaciente);
}
