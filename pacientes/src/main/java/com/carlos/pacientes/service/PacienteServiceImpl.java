package com.carlos.pacientes.service;

import com.carlos.commons.clients.CitaClient;
import com.carlos.commons.dto.pacientes.PacienteRequest;
import com.carlos.commons.dto.pacientes.PacienteResponse;
import com.carlos.commons.enums.EstadoRegistro;
import com.carlos.commons.exceptions.RecursoNoEncontradoException;
import com.carlos.pacientes.entity.Paciente;
import com.carlos.pacientes.mappers.PacienteMapper;
import com.carlos.pacientes.repository.PacienteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;
    private final CitaClient citaClient;

    @Override
    public List<PacienteResponse> listar() {
        return pacienteRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(pacienteMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorId(Long id) {
        Paciente paciente = obtenerPacienteOExcepcion(id);
        return pacienteMapper.entidadAResponse(paciente);
    }

    @Override
    public PacienteResponse registrar(PacienteRequest request) {
        validarRestricciones(request);
        validarDatosUnicos(request.email(), request.telefono());
        Paciente paciente = pacienteMapper.requestAEntidad(request);
        paciente.asignarImc();
        paciente.asignarNumExpediente();
        Paciente guardado = pacienteRepository.save(paciente);
        return pacienteMapper.entidadAResponse(guardado);
    }

    @Override
    public PacienteResponse actualizar(PacienteRequest request, Long id) {
        Paciente paciente = obtenerPacienteOExcepcion(id);
        pacienteActualizable(id);
        validarRestricciones(request);
        validarDatosUnicos(request.email(), request.telefono());
        paciente.asignarImc();
        paciente.asignarNumExpediente();
        paciente.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.edad(),
                request.peso(),
                request.estatura(),
                request.email(),
                request.telefono(),
                request.direccion()
        );
        Paciente actualizado = pacienteRepository.save(paciente);
        return pacienteMapper.entidadAResponse(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        Paciente paciente = obtenerPacienteOExcepcion(id);
        pacienteEliminable(id);
        paciente.eliminar();
        pacienteRepository.save(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPacientesPorIdSinEstado(Long id) {
        return pacienteRepository.findById(id)
                .map(pacienteMapper::entidadAResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado con id: " + id));
    }

    private void validarDatosUnicos(String email, String telefono) {
        if (pacienteRepository.existsByEmailIgnoreCaseAndEstadoRegistro(email, EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un registro activo con este email");
        if (pacienteRepository.existsByTelefonoAndEstadoRegistro(telefono, EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un registro activo con este teléfono");
    }

    private void validarRestricciones(PacienteRequest request) {
        if (request.edad() < 1 || request.edad() > 100) throw new IllegalArgumentException("Edad fuera de rango");
        if (request.peso() < 0.1 || request.peso() > 200) throw new IllegalArgumentException("Peso fuera de rango");
        if (request.estatura() < 1.0 || request.estatura() > 2.0) throw new IllegalArgumentException("Estatura fuera de rango");
        if (!request.telefono().matches("^[0-9]{10}$")) throw new IllegalArgumentException("Teléfono inválido");
        if (!request.email().contains("@")) throw new IllegalArgumentException("Email inválido");
    }

    private Paciente obtenerPacienteOExcepcion(Long id) {
        return pacienteRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente activo no encontrado con id: " + id));
    }

    private void pacienteEliminable(Long id) {
        if (citaClient.obtenerCitaActivaConPacienteId(id))
            throw new IllegalArgumentException("No se puede eliminar el paciente porque tiene citas activas");
    }

    private void pacienteActualizable(Long id) {
        if (citaClient.obtenerCitaActivaConPacienteId(id))
            throw new IllegalArgumentException("No se puede actualizar el paciente porque tiene citas activas");
    }
}
