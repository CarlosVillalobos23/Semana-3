package com.carlos.commons.clients;

import com.carlos.commons.dto.medicos.MedicoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "medicos")
public interface MedicoClient {

    @GetMapping("/activo/{id}")
    MedicoResponse obtenerMedicoActivoPorId(@PathVariable("id") Long id);

    @GetMapping("/id-medico/{id}")
    MedicoResponse obtenerMedicoSinEstadoPorId(@PathVariable("id") Long id);

    @GetMapping("/medico-disponible/{id}")
    void obtenerDisponibilidadMedicoPorId(@PathVariable("id") Long id);

    @PutMapping("/medicos/{idMedico}/disponibilidad/{idDisponibilidad}")
    void actualizarDisponibilidadMedico(@PathVariable Long idMedico,
                                        @PathVariable Long idDisponibilidad);
}
