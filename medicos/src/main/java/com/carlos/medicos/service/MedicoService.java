package com.carlos.medicos.service;

import com.carlos.commons.dto.medicos.MedicoRequest;
import com.carlos.commons.dto.medicos.MedicoResponse;
import com.carlos.commons.service.CrudService;

public interface MedicoService  extends CrudService<MedicoRequest, MedicoResponse> {
    MedicoResponse obtenerMedicoPorIdSinEstado(Long id);
    void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad);

    void obtenerDisponibilidadMedicoPorId(Long id);

}
