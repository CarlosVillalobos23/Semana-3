package com.carlos.commons.clients;

import com.carlos.commons.dto.medicos.MedicoResponse;
import com.carlos.commons.dto.pacientes.PacienteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pacientes")
public interface PacienteClient {
    @GetMapping("/{id}")
    PacienteResponse obtenerPacienteActivoPorId(@PathVariable Long id);
    @GetMapping("/id-medico/{id}")
    PacienteResponse obtenerPacienteSinEstadoPorId(@PathVariable Long id);
}
