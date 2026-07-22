package com.carlos.citas.entity;

import com.carlos.citas.enums.EstadoCita;
import com.carlos.commons.enums.EstadoRegistro;
import com.carlos.commons.utils.StringCustomUtils;
import com.carlos.commons.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "CITAS")
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CITAS")
    private Long id;
    @Column(name = "ID_PACIENTE",nullable = false)
    private Long idPaciente;
    @Column(name = "ID_MEDICO",nullable = false)
    private Long idMedico;
    @Column(name = "FECHA_CITA",nullable = false)
    private LocalDateTime fechaCita;
    @Column(name = "SINTOMAS",nullable = false,length = 500)
    private String sintomas;
    @Column(name = "ESTADO_CITA",nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCita estadoCita;
    @Column(name = "ESTADO_REGISTRO",nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoRegistro estadoRegistro;

    public void actualizarEstadoCita(EstadoCita nuevoEstado){
        validarActualizacionPermitida();
        validarNoEliminado();
        if(nuevoEstado==null)throw new IllegalArgumentException("El nuevo estado de la cita es requerido");
        if(!this.estadoCita.puedeCambiarA(nuevoEstado))throw new IllegalArgumentException("La cita con estado "
                +this.estadoCita+" solo se puede cambiar a :"+estadoCita.puedeCambiar());
    }
    public void actualizar(Long idPaciente, Long idMedico, LocalDateTime fechaCita, String sintomas) {
        validarActualizacionPermitida();
        validarDatos(idPaciente,idMedico,fechaCita,sintomas);
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fechaCita = fechaCita;
        this.sintomas = sintomas.trim();
    }

    public static Cita crear(Long idPaciente,Long idMedico,LocalDateTime fechaCita,String sintomas){
        validarDatos(idPaciente,idMedico,fechaCita,sintomas);
        return Cita.builder()
                .idPaciente(idPaciente)
                .idMedico(idMedico)
                .fechaCita(fechaCita)
                .sintomas(sintomas.trim())
                .estadoCita(EstadoCita.PENDIENTE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();

    }
    public static  void validarDatos(Long idPaciente, Long idMedico, LocalDateTime fechaCita, String sintomas){
        validarId(idPaciente,"Paciente");
        validarId(idMedico,"Medico");
        validarFechaCita(fechaCita);
        StringCustomUtils.validarTamano(sintomas,20,500,"Los sintomas son requeridos y debe tener entre 20 y 500 caracteres");

    }
    private static void validarId(Long id, String campo){
        ValoresNumericosUtils.validarLongPositivo(id,"El id del "+campo+" debe ser positivo");
    }
    private static void validarFechaCita(LocalDateTime fechaCita){
        if(fechaCita==null||!fechaCita.isAfter(LocalDateTime.now()))throw new IllegalArgumentException("La fecha de la cita es requerida y debe ser futura");
    }
    public  void eliminar(){
        validadEliminacionPermitida();
        this.estadoRegistro=EstadoRegistro.ELIMINADO;
    }
    private void validarNoEliminado(){
        if(this.estadoRegistro==EstadoRegistro.ELIMINADO)throw new IllegalArgumentException("La cita ya esta eliminada");
    }
    private void validadEliminacionPermitida(){
        validarNoEliminado();
        if(!this.estadoCita.isEliminable())throw new IllegalArgumentException("La cita con estado "+this.estadoCita+" no puede eliminarse");
    }
    private void validarActualizacionPermitida(){
        validarNoEliminado();
        if(!this.estadoCita.isActualizable())throw new IllegalArgumentException("La cita con estado "+this.estadoCita+" no puede actualizarse");
    }


}
