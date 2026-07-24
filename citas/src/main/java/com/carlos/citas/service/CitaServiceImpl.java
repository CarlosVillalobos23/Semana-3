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
import com.carlos.commons.enums.DisponibilidadMedico;
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
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;
    private final MedicoClient medicoClient;
    private final PacienteClient pacienteClient;

    @Override
    public void actualizarEstadoCita(Long idCita, Long idEstadoCita) {
        Cita cita = obtenerCitaOException(idCita);
        EstadoCita nuevoEstado = EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita);
        validarTransicionEstado(cita, nuevoEstado);
        cita.actualizarEstadoCita(nuevoEstado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listar() {
        return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(cita -> citaMapper.entidadAResponse(cita,
                        obtenerPacienteSinEstado(cita.getIdPaciente()),
                        obtenerMedicoSinEstado(cita.getIdMedico())))
                .toList();
    }

    @Override
    public CitaResponse obtenerPorId(Long id) {
        Cita cita = obtenerCitaOException(id);
        return citaMapper.entidadAResponse(cita,
                obtenerPacienteSinEstado(cita.getIdPaciente()),
                obtenerMedicoSinEstado(cita.getIdMedico()));
    }

    @Override
    public CitaResponse registrar(CitaRequest request) {
        validarDatos(request.idPaciente(), request.idMedico());
        validarCitasSimultaneas(request.idPaciente());

        Cita cita = citaMapper.requestAEntidad(request);
        citaRepository.save(cita);

        // Cambiar disponibilidad del médico a NO_DISPONIBLE
        medicoClient.actualizarDisponibilidadMedico(
                request.idMedico(),
                DisponibilidadMedico.NO_DISPONIBLE.getCodigo()
        );

        return citaMapper.entidadAResponse(obtenerCitaOException(cita.getId()),
                obtenerPacienteSinEstado(cita.getIdPaciente()),
                obtenerMedicoSinEstado(cita.getIdMedico()));
    }

    @Override
    public CitaResponse actualizar(CitaRequest request, Long id) {
        Cita cita = obtenerCitaOException(id);
        validarEstadoParaActualizar(cita);

        Long idMedicoAnterior = cita.getIdMedico();
        Long idMedicoNuevo = request.idMedico();

        validarDatos(request.idPaciente(), idMedicoNuevo);

        cita.actualizar(request.idPaciente(), idMedicoNuevo, request.fechaCita(), request.sintomas());

        // Liberar médico anterior si ya no tiene citas
        if (!obtenerCitaActivaConMedicoId(idMedicoAnterior)) {
            medicoClient.actualizarDisponibilidadMedico(
                    idMedicoAnterior,
                    DisponibilidadMedico.DISPONIBLE.getCodigo()
            );
        }

        // Marcar nuevo médico como NO_DISPONIBLE
        medicoClient.actualizarDisponibilidadMedico(
                idMedicoNuevo,
                DisponibilidadMedico.NO_DISPONIBLE.getCodigo()
        );

        return citaMapper.entidadAResponse(obtenerCitaOException(id),
                obtenerPacienteSinEstado(cita.getIdPaciente()),
                obtenerMedicoSinEstado(cita.getIdMedico()));
    }

    @Override
    public void eliminar(Long id) {
        Cita cita = obtenerCitaOException(id);
        validarEstadoParaEliminar(cita);
        cita.eliminar();

        // Si el médico ya no tiene citas activas, volver a DISPONIBLE
        if (!obtenerCitaActivaConMedicoId(cita.getIdMedico())) {
            medicoClient.actualizarDisponibilidadMedico(
                    cita.getIdMedico(),
                    DisponibilidadMedico.DISPONIBLE.getCodigo()
            );
        }
    }

    private void validarTransicionEstado(Cita cita, EstadoCita nuevoEstado) {
        if (!cita.getEstadoCita().puedeCambiarA(nuevoEstado)) {
            throw new IllegalArgumentException("Transición no permitida de " + cita.getEstadoCita() + " a " + nuevoEstado);
        }
    }

    private void validarEstadoParaActualizar(Cita cita) {
        if (!(cita.getEstadoCita() == EstadoCita.PENDIENTE || cita.getEstadoCita() == EstadoCita.CONFIRMADA)) {
            throw new IllegalArgumentException("Solo se pueden actualizar citas en estado PENDIENTE o CONFIRMADA");
        }
    }

    private void validarEstadoParaEliminar(Cita cita) {
        if (!cita.getEstadoCita().isEliminable()) {
            throw new IllegalArgumentException("No se puede eliminar una cita en estado " + cita.getEstadoCita());
        }
    }

    private void validarCitasSimultaneas(Long idPaciente) {
        boolean tieneCitaPendiente = citaRepository.existsByIdPacienteAndEstadoCita(idPaciente, EstadoCita.PENDIENTE);
        boolean tieneCitaEnCurso = citaRepository.existsByIdPacienteAndEstadoCita(idPaciente, EstadoCita.EN_CURSO);
        boolean tieneCitaConfirmada = citaRepository.existsByIdPacienteAndEstadoCita(idPaciente, EstadoCita.CONFIRMADA);
        if (tieneCitaPendiente || tieneCitaEnCurso || tieneCitaConfirmada) {
            throw new IllegalArgumentException("El paciente tiene citas simultáneas");
        }
    }

    private void validarDatos(Long idPaciente, Long idMedico) {
        MedicoResponse medico = obtenerMedicoActivo(idMedico);
        if (medico == null) {
            throw new IllegalArgumentException("No existen médicos activos con ese id");
        }

        // Validar que el médico esté disponible
        medicoClient.obtenerDisponibilidadMedicoPorId(idMedico);

        PacienteResponse paciente = obtenerPacienteActivo(idPaciente);
        if (paciente == null) {
            throw new IllegalArgumentException("No existe un paciente activo con esa id");
        }
    }

    public MedicoResponse obtenerMedicoActivo(Long id) {
        return medicoClient.obtenerMedicoActivoPorId(id);
    }

    public MedicoResponse obtenerMedicoSinEstado(Long id) {
        return medicoClient.obtenerMedicoSinEstadoPorId(id);
    }

    public PacienteResponse obtenerPacienteActivo(Long id) {
        return pacienteClient.obtenerPacienteActivoPorId(id);
    }

    public PacienteResponse obtenerPacienteSinEstado(Long id) {
        return pacienteClient.obtenerPacienteSinEstadoPorId(id);
    }

    private Cita obtenerCitaOException(Long id) {
        return citaRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Cita no encontrada con id: " + id));
    }

    public boolean obtenerCitaActivaConMedicoId(Long id) {
        return citaRepository.existsByIdMedicoAndEstadoCita(id, EstadoCita.PENDIENTE)
                || citaRepository.existsByIdMedicoAndEstadoCita(id, EstadoCita.CONFIRMADA);
    }

    public boolean obtenerCitaActivaConPacienteId(Long id) {
        return citaRepository.existsByIdPacienteAndEstadoCita(id, EstadoCita.PENDIENTE)
                || citaRepository.existsByIdPacienteAndEstadoCita(id, EstadoCita.CONFIRMADA)
                || citaRepository.existsByIdPacienteAndEstadoCita(id, EstadoCita.EN_CURSO);
    }
}
