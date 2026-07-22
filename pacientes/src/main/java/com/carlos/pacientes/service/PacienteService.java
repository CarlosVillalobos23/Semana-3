package com.carlos.pacientes.service;

import com.carlos.commons.dto.pacientes.PacienteRequest;
import com.carlos.commons.dto.pacientes.PacienteResponse;
import com.carlos.commons.service.CrudService;

public interface PacienteService extends CrudService<PacienteRequest, PacienteResponse> {
    PacienteResponse obtenerPacientesPorIdSinEstado(Long id);

}
