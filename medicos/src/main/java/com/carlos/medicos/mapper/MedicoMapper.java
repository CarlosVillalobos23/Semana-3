package com.carlos.medicos.mapper;

import com.carlos.commons.dto.medicos.MedicoRequest;
import com.carlos.commons.dto.medicos.MedicoResponse;
import com.carlos.commons.enums.DisponibilidadMedico;
import com.carlos.commons.enums.EspecialidadMedico;
import com.carlos.commons.enums.EstadoRegistro;
import com.carlos.commons.mapper.CommonMapper;
import com.carlos.medicos.entity.Medico;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapper implements CommonMapper<MedicoRequest, MedicoResponse, Medico> {

    @Override
    public Medico requestAEntidad(MedicoRequest request) {
        if (request == null) return null;

        return Medico.builder()
                .nombre(request.nombre().trim())
                .apellidoPaterno(request.apellidoPaterno().trim())
                .apellidoMaterno(request.apellidoMaterno().trim())
                .edad(request.edad().shortValue())
                .email(request.email().trim().toLowerCase())
                .telefono(request.telefono().trim())
                .cedula(request.cedula() != null ? request.cedula().trim() : null)
                .especialidad(convertirEspecialidad(request.especialidad()))
                .disponibilidad(DisponibilidadMedico.DISPONIBLE) // estado inicial
                .estadoRegistro(EstadoRegistro.ACTIVO)           // estado inicial
                .build();
    }

    @Override
    public MedicoResponse entidadAResponse(Medico entidad) {
        if (entidad == null) return null;

        return new MedicoResponse(
                entidad.getId(),
                entidad.getNombre(),
                entidad.getApellidoPaterno(),
                entidad.getApellidoMaterno(),
                entidad.getEdad(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getCedula(),
                entidad.getEspecialidad() != null ? entidad.getEspecialidad().getDescripcion() : null,
                entidad.getDisponibilidad() != null ? entidad.getDisponibilidad().getDescripcion() : null,
                entidad.getEstadoRegistro() != null ? entidad.getEstadoRegistro().name() : null
        );
    }

    public EspecialidadMedico convertirEspecialidad(Long especialidadId) {
        if (especialidadId == null) return null;
        return EspecialidadMedico.values()[especialidadId.intValue() % EspecialidadMedico.values().length];
    }
}
