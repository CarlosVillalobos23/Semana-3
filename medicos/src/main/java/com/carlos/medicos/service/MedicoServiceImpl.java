package com.carlos.medicos.service;

import com.carlos.commons.clients.CitaClient;
import com.carlos.commons.dto.medicos.MedicoRequest;
import com.carlos.commons.dto.medicos.MedicoResponse;
import com.carlos.commons.enums.DisponibilidadMedico;
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
    private final CitaClient citaClient;

    @Override
    @Transactional(readOnly = true)
    public List<MedicoResponse> listar() {
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
        return medicoMapper.entidadAResponse(medico);
    }

    @Override
    public MedicoResponse actualizar(MedicoRequest request, Long id) {
        Medico medico = obtenerMedicoOException(id);
        validarMedicoActualizable(id);
        validarCambiosUnicos(request, id);
        validarDatosMedico(request);
        medico.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.edad().shortValue(),
                request.email(),
                request.telefono(),
                request.cedulaProfesional(),
                medicoMapper.convertirEspecialidad(request.idEspecialidad())
        );
        return medicoMapper.entidadAResponse(medico);
    }

    @Override
    public void eliminar(Long id) {
        Medico medico = obtenerMedicoOException(id);
        validarMedicoEliminable(id);
        medico.eliminar();
        medicoRepository.save(medico);
    }

    @Override
    public MedicoResponse obtenerMedicoPorIdSinEstado(Long id) {
        Medico medico = obtenerMedicoOException(id);
        return medicoMapper.entidadAResponse(medico);
    }


    @Override
    public void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad) {
        Medico medico = medicoRepository.findByIdAndEstadoRegistro(idMedico, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Médico activo no encontrado con id: " + idMedico));
        DisponibilidadMedico nuevaDisponibilidad = DisponibilidadMedico.values()[idDisponibilidad.intValue()];
        if (nuevaDisponibilidad == DisponibilidadMedico.DISPONIBLE && citaClient.obtenerCitaActivaConMedicoId(idMedico)) {
            throw new IllegalArgumentException("No se puede poner DISPONIBLE al médico porque tiene citas activas");
        }
        medico.actualizarDisponibilidad(nuevaDisponibilidad);
        medicoRepository.save(medico);
    }

    public void obtenerDisponibilidadMedicoPorId(Long id) {
        if (!medicoRepository.existsByIdAndDisponibilidad(id, DisponibilidadMedico.DISPONIBLE)) {
            throw new IllegalArgumentException("El médico no está disponible");
        }
    }

    private Medico obtenerMedicoOException(Long id) {
        return medicoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Médico no encontrado con id: " + id)
        );
    }

    private void validarDatosUnicos(MedicoRequest request) {
        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistro(request.email(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un médico con el email: " + request.email());
        if (medicoRepository.existsByTelefonoAndEstadoRegistro(request.telefono(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un médico con el teléfono: " + request.telefono());
        if (medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistro(request.cedulaProfesional(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un médico con la cédula: " + request.cedulaProfesional());
    }

    private void validarCambiosUnicos(MedicoRequest request, Long id) {
        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(request.email(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe un médico con el email: " + request.email());
        if (medicoRepository.existsByTelefonoAndEstadoRegistroAndIdNot(request.telefono(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe un médico con el teléfono: " + request.telefono());
        if (medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistroAndIdNot(request.cedulaProfesional(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("Ya existe un médico con la cédula: " + request.cedulaProfesional());
    }

    private void validarDatosMedico(MedicoRequest request) {
        if (request.edad() < 18)
            throw new IllegalArgumentException("La edad mínima es 18 años");
        if (request.cedulaProfesional() == null || request.cedulaProfesional().length() != 12)
            throw new IllegalArgumentException("La cédula profesional debe tener exactamente 12 caracteres");
        if (request.idEspecialidad() == null)
            throw new IllegalArgumentException("La especialidad es requerida");
    }

    private void validarMedicoEliminable(Long id) {
        if (citaClient.obtenerCitaActivaConMedicoId(id))
            throw new IllegalArgumentException("No se puede eliminar el médico porque tiene citas activas");
    }

    private void validarMedicoActualizable(Long id) {
        if (citaClient.obtenerCitaActivaConMedicoId(id))
            throw new IllegalArgumentException("No se puede actualizar el médico porque tiene citas activas");
    }
}
