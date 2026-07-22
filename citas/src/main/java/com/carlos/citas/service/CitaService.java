package com.carlos.citas.service;

import com.carlos.citas.dto.CitaRequest;
import com.carlos.citas.dto.CitaResponse;
import com.carlos.citas.entity.Cita;
import com.carlos.commons.service.CrudService;

public interface CitaService extends CrudService<CitaRequest, CitaResponse> {
    void actualizarEstadoCita(Long idCita,Long idEstadoCita);
}
