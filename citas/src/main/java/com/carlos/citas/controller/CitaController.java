package com.carlos.citas.controller;

import com.carlos.citas.dto.CitaRequest;
import com.carlos.citas.dto.CitaResponse;
import com.carlos.citas.service.CitaService;
import com.carlos.commons.controller.CommonController;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class CitaController extends CommonController<CitaRequest, CitaResponse, CitaService> {
    protected CitaController(CitaService service) {
        super(service);
    }
    @PatchMapping("/{idCita}/estado/{idEstado}")
    public ResponseEntity<Void>actualizarEstadoCita(
            @PathVariable@Positive(message = "El id cita debe ser positivo")Long idCita,
            @PathVariable@Positive(message = "El idEstado debe ser positivo")Long idEstado
    ){
        service.actualizarEstadoCita(idCita,idEstado);
        return ResponseEntity.noContent().build();
    }
    private CitaService citaService;

    @GetMapping("/medico/{idMedico}/activa")
    public boolean obtenerCitaActivaConMedicoId(@PathVariable Long idMedico) {
        return citaService.obtenerCitaActivaConMedicoId(idMedico);
    }

    @GetMapping("/paciente/{idPaciente}/activa")
    public boolean obtenerCitaActivaConPacienteId(@PathVariable Long idPaciente) {
        return citaService.obtenerCitaActivaConPacienteId(idPaciente);
    }
}
