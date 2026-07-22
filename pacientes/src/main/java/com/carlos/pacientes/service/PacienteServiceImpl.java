package com.carlos.pacientes.service;

import com.carlos.commons.dto.pacientes.PacienteRequest;
import com.carlos.commons.dto.pacientes.PacienteResponse;
import com.carlos.commons.enums.EstadoRegistro;
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
public class PacienteServiceImpl implements PacienteService{

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    @Override
    public List<PacienteResponse> listar() {

        return pacienteRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(pacienteMapper::entidadAResponse).toList();
    }

    @Override
    public PacienteResponse obtenerPorId(Long id) {
       if(id==null) return null;
       return null;
       /*pacienteRepository.findByIdAndEstadoRegistro(id,EstadoRegistro.ACTIVO).stream()
               .map(pacienteMapper.entidadAResponse());
    */}

    @Override
    public PacienteResponse registrar(PacienteRequest request) {
        return null;
    }

    @Override
    public PacienteResponse actualizar(PacienteRequest request, Long id) {
        return null;
    }

    @Override
    public void eliminar(Long id) {

    }

    @Override
    public PacienteResponse obtenerPacientesPorIdSinEstado(Long id) {
        if(id==null)return null;
        return  null;
    }


    private void obtenerPacienteOExcepcion(Long id){
        if()
    }
}
