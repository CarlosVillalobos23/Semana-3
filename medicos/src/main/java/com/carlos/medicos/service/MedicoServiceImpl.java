package com.carlos.medicos.service;

import com.carlos.commons.dto.medicos.MedicoRequest;
import com.carlos.commons.dto.medicos.MedicoResponse;
import com.carlos.commons.enums.EstadoRegistro;
import com.carlos.commons.exceptions.RecursoNoEncontradoException;
import com.carlos.medicos.entity.Medico;
import com.carlos.medicos.mapper.MedicoMapper;
import com.carlos.medicos.repository.MedicoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class MedicoServiceImpl implements MedicoService {
    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MedicoResponse> listar() {
        log.info("Listando todos los médicos");
        return medicoRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(medicoMapper::entidadAResponse)
                .toList();
    }

    @Override
    public MedicoResponse obtenerPorId(Long id) {
        return medicoMapper.entidadAResponse(obtenerMedicoOException(id));
    }

    @Override
    public MedicoResponse registrar(MedicoRequest request) {
        validarDatosUnicos(request);
        validarDatosMedico(request);
        Medico medico = medicoMapper.requestAEntidad(request);
        medicoRepository.save(medico);
        log.info("Nuevo médico {} registrado", medico.getNombre());
        return medicoMapper.entidadAResponse(medico);
    }

    @Override
    public MedicoResponse actualizar(MedicoRequest request, Long id) {
        Medico medico = obtenerMedicoOException(id);
        validarCambiosUnicos(request, id);
        validarDatosMedico(request);

        medico.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.edad().shortValue(),
                request.email(),
                request.telefono(),
                request.cedula(),
                medicoMapper.convertirEspecialidad(request.especialidad())
        );

        log.info("Médico con id {} actualizado", id);
        return medicoMapper.entidadAResponse(medico);
    }

    @Override
    public void eliminar(Long id) {
        Medico medico = obtenerMedicoOException(id);
        medico.eliminar();
        medicoRepository.save(medico);
        log.info("Médico con id {} eliminado", id);
    }

    private Medico obtenerMedicoOException(Long id) {
        log.info("Buscando médico con id: {}", id);
        return medicoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Médico no encontrado con id: " + id)
        );
    }

    private void validarDatosUnicos(MedicoRequest request) {
        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistro(request.email(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe un médico con el email: " + request.email());
        }
        if (medicoRepository.existsByTelefonoAndEstadoRegistro(request.telefono(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe un médico con el teléfono: " + request.telefono());
        }
        if (medicoRepository.existsByCedulaIgnoreCaseAndEstadoRegistro(request.cedula(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe un médico con la cédula: " + request.cedula());
        }
    }

    private void validarCambiosUnicos(MedicoRequest request, Long id) {
        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(request.email(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("Ya existe un médico con el email: " + request.email());
        }
        if (medicoRepository.existsByTelefonoAndEstadoRegistroAndIdNot(request.telefono(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("Ya existe un médico con el teléfono: " + request.telefono());
        }
        if (medicoRepository.existsByCedulaIgnoreCaseAndEstadoRegistroAndIdNot(request.cedula(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("Ya existe un médico con la cédula: " + request.cedula());
        }
    }

    private void validarDatosMedico(MedicoRequest request) {
        if (request.edad() < 18) {
            throw new IllegalArgumentException("La edad mínima es 18 años");
        }
        if (request.cedula() == null || request.cedula().length() != 12) {
            throw new IllegalArgumentException("La cédula profesional debe tener exactamente 12 caracteres");
        }
        if (request.especialidad() == null) {
            throw new IllegalArgumentException("La especialidad es requerida");
        }
    }

    @Override
    public MedicoResponse obtenerMedicoPorIdSinEstado(Long id) {
        Medico medico=obtenerMedicoOException(id);
        return medicoMapper.entidadAResponse(medico);
    }

    @Override
    public void actualizarDisponibilidadMdico(Long idMedico, Long idDisponibilidad) {

    }
}
