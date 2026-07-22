package com.carlos.commons.clients;

import com.carlos.commons.dto.medicos.MedicoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="medicos")
public interface MedicoClient {
    @GetMapping("/{id}")
    MedicoResponse obtenerMedicoActivoPorId(@PathVariable Long id);
    @GetMapping("/id-medico/{id}")
    MedicoResponse obtenerMedicoSinEstadoPorId(@PathVariable Long id);
}
