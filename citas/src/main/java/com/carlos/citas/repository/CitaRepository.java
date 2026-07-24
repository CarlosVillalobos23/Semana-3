package com.carlos.citas.repository;

import com.carlos.citas.entity.Cita;
import com.carlos.citas.enums.EstadoCita;
import com.carlos.commons.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository <Cita,Long>{
    List<Cita> findByEstadoRegistro(EstadoRegistro estadoRegistro);
    Optional<Cita> findByIdAndEstadoRegistro(Long id,EstadoRegistro estadoRegistro);
    boolean existsByIdPacienteAndEstadoCita(Long idPaciente,EstadoCita estadoCita);
    boolean existsByIdMedicoAndEstadoCita(Long idMedico, EstadoCita estadoCita);
}
