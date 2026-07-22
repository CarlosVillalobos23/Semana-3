package com.carlos.pacientes.controller;

import com.carlos.commons.controller.CommonController;
import com.carlos.commons.dto.pacientes.PacienteRequest;
import com.carlos.commons.dto.pacientes.PacienteResponse;
import com.carlos.pacientes.service.PacienteService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class PacientrController extends CommonController<PacienteRequest, PacienteResponse, PacienteService> {

    protected PacientrController(PacienteService service) {
        super(service);
    }
    @GetMapping("/id-paciente/{id}")
    public ResponseEntity<PacienteResponse> obtenerPacientePorIdSinEstado(
            @PathVariable @Positive(message = "El id debe ser positivo")Long id){
        return  ResponseEntity.ok(service.obtenerPacientesPorIdSinEstado(id));
    }

}
