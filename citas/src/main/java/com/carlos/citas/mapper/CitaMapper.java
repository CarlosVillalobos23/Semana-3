package com.carlos.citas.mapper;

import com.carlos.citas.dto.CitaRequest;
import com.carlos.citas.dto.CitaResponse;
import com.carlos.citas.entity.Cita;
import com.carlos.commons.dto.medicos.DatosMedico;
import com.carlos.commons.dto.medicos.MedicoResponse;
import com.carlos.commons.dto.pacientes.DatosPaciente;
import com.carlos.commons.dto.pacientes.PacienteResponse;
import com.carlos.commons.mapper.CommonMapper;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper implements CommonMapper<CitaRequest, CitaResponse, Cita> {

    @Override
    public Cita requestAEntidad(CitaRequest request) {
        if(request==null)return null;
        return  Cita.crear(
                request.idPaciente(),
                request.idMedico(),
                request.fechaCita(),
                request.sintomas()
        );
    }

    @Override
    public CitaResponse entidadAResponse(Cita entidad) {
        if(entidad==null)return null;
        return new CitaResponse(
                entidad.getId(),
                null,
                null,
                entidad.getFechaCita(),
                entidad.getSintomas(),
                entidad.getEstadoCita().getDescripcion()
        );
    }

    public CitaResponse entidadAResponse(Cita entidad, PacienteResponse paciente, MedicoResponse medico) {
        if(entidad==null)return null;
        return new CitaResponse(
                entidad.getId(),
                pacienteResponseADatosPaciente(paciente),
                medicoResponseADatosMedico(medico),
                entidad.getFechaCita(),
                entidad.getSintomas(),
                entidad.getEstadoCita().getDescripcion()
        );
    }
    private DatosMedico medicoResponseADatosMedico(MedicoResponse medico){
        if(medico==null)return null;
        return new DatosMedico(
                medico.nombre(),
                medico.cedula(),
                medico.especialidad()
        );
    }
    private DatosPaciente pacienteResponseADatosPaciente(PacienteResponse paciente){
        if(paciente==null)return null;
        return new DatosPaciente(
                paciente.nombre(),
                paciente.numExpediente(),
                paciente.edad()+" años",
                paciente.peso()+" Kg",
                paciente.estatura()+"m.",
                String.join("",
                        Math.round(paciente.imc()*100.0)/100.0+"",clasificacionIMC(paciente.imc())),
                paciente.telefono()
        );
    }
    private String clasificacionIMC(Double imc){
        if(imc<18.5)return "Bajo peso";
        if(imc<25)return "peso normal peso";
        if(imc<30)return "sobrepeso";
        if(imc<35)return "obesidad grado I";
        if(imc<40)return "obesidad grado II";
        return "obesia grado III";
    }
}
