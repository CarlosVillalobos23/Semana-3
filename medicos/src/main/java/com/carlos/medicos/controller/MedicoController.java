package com.carlos.medicos.controller;

import com.carlos.commons.controller.CommonController;
import com.carlos.commons.dto.medicos.MedicoRequest;
import com.carlos.commons.dto.medicos.MedicoResponse;
import com.carlos.medicos.service.MedicoService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class MedicoController extends CommonController<MedicoRequest, MedicoResponse, MedicoService> {
    protected MedicoController(MedicoService service) {
        super(service);
    }

    @GetMapping("/activo/{id}")
    public ResponseEntity<MedicoResponse> obtenerMedicoActivoPorId(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/id-medico/{id}")
    public ResponseEntity<MedicoResponse> obtenerMedicoPorIdSinEstado(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id) {
        return ResponseEntity.ok(service.obtenerMedicoPorIdSinEstado(id));
    }

    @PutMapping("/{idMedico}/disponibilidad/{idDisponibilidad}")
    public ResponseEntity<Void> actualizarDisponibilidadMedico(
            @PathVariable @Positive(message = "El id debe ser positivo") Long id,
            @PathVariable @Positive(message = "El idDisponibilidad debe ser positivo") Long idDisponibilidad) {
        service.actualizarDisponibilidadMedico(id, idDisponibilidad);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medico-disponible/{id}")
    public void obtenerDisponibilidadMedicoPorId(@PathVariable Long id) {
        service.obtenerDisponibilidadMedicoPorId(id);
    }

}
