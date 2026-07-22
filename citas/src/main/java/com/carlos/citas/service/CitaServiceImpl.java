package com.carlos.citas.service;

import com.carlos.citas.dto.CitaRequest;
import com.carlos.citas.dto.CitaResponse;
import com.carlos.citas.entity.Cita;
import com.carlos.citas.enums.EstadoCita;
import com.carlos.citas.mapper.CitaMapper;
import com.carlos.citas.repository.CitaRepository;
import com.carlos.commons.clients.MedicoClient;
import com.carlos.commons.clients.PacienteClient;
import com.carlos.commons.dto.medicos.MedicoResponse;
import com.carlos.commons.dto.pacientes.PacienteResponse;
import com.carlos.commons.enums.EstadoRegistro;
import com.carlos.commons.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CitaServiceImpl implements CitaService{

    private final CitaRepository citaRepository;

    private final CitaMapper citaMapper;

    private final MedicoClient medicoClient;
    private final PacienteClient pacienteClient;

    @Override
    public void actualizarEstadoCita(Long idCita, Long idEstadoCita) {
        Cita cita = obtenerCitaOException(idCita);

        log.info("Actualizando estado de la cita cin id: {}", idCita);

        cita.actualizarEstadoCita(EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita));

        log.info("Estado de la cita {} actualizado correctamente", idCita);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listar() {
        log.info("Listando todas las citas activas");
        return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(cita-> citaMapper.entidadAResponse(cita,
                        obtenerPacienteSinEstado(cita.getIdPaciente()), obtenerMedicoSinEstado(cita.getIdMedico()))).toList();
    }

    @Override
    public CitaResponse obtenerPorId(Long id) {
        Cita cita = obtenerCitaOException(id);
        return citaMapper.entidadAResponse(obtenerCitaOException(id),
                obtenerPacienteSinEstado(cita.getIdPaciente()),
                obtenerMedicoSinEstado(cita.getIdMedico()));
    }

    @Override
    public CitaResponse registrar(CitaRequest request) {
        log.info("Registrando nueva cita: {}",request);

        Cita cita = citaMapper.requestAEntidad(request);

        citaRepository.save(cita);

        log.info("Cita registrada exitosamente");

        return citaMapper.entidadAResponse(obtenerCitaOException(cita.getId()),
                obtenerPacienteSinEstado(cita.getIdPaciente()),
                obtenerMedicoSinEstado(cita.getIdMedico()));
    }

    @Override
    public CitaResponse actualizar(CitaRequest request, Long id) {
        Cita cita = obtenerCitaOException(id);

        log.info("Actualizando cita con id: {}", id);

        cita.actualizar(
                request.idPaciente(),
                request.idMedico(),
                request.fechaCita(),
                request.sintomas()
        );

        log.info("Cita actualizada con id: {}", id);

        return citaMapper.entidadAResponse(obtenerCitaOException(id),
                obtenerPacienteSinEstado(cita.getIdPaciente()),
                obtenerMedicoSinEstado(cita.getIdMedico()));
    }

    @Override
    public void eliminar(Long id) {

        Cita cita = obtenerCitaOException(id);

        log.info("Eliminando cita con id: {}",id);

        cita.eliminar();

        log.info("Cita con id {} ha sido marcada como ELIMINADA", id);
    }

    public MedicoResponse obtenerMedicoActivo(Long id){
        log.info("Bucando medico activo con id {} en el servicio remoto...", id);
        return medicoClient.obtenerMedicoActivoPorId(id);
    }

    public MedicoResponse obtenerMedicoSinEstado(Long id){
        log.info("Bucando medico sin estado con id {} en el servicio remoto...", id);
        return medicoClient.obtenerMedicoSinEstadoPorId(id);
    }

    public PacienteResponse obtenerPacienteActivo(Long id){
        log.info("Bucando paciente activo con id {} en el servicio remoto...", id);
        return pacienteClient.obtenerPacienteActivoPorId(id);
    }

    public PacienteResponse obtenerPacienteSinEstado(Long id){
        log.info("Bucando paciente sin estado con id {} en el servicio remoto...", id);
        return pacienteClient.obtenerPacienteSinEstadoPorId(id);
    }

    private Cita obtenerCitaOException(Long id){
        log.info("Buscando cita con id: {}",id);

        return citaRepository.findById(id).orElseThrow(
                ()->new RecursoNoEncontradoException("Cita no encontrada con id: "+id));
    }
}