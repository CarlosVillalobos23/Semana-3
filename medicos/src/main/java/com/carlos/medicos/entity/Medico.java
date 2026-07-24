package com.carlos.medicos.entity;

import com.carlos.commons.enums.DisponibilidadMedico;
import com.carlos.commons.enums.EspecialidadMedico;
import com.carlos.commons.enums.EstadoRegistro;
import com.carlos.commons.utils.StringCustomUtils;
import com.carlos.commons.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "MEDICOS")
public class Medico {
    @Id
    @Column(name = "ID_MEDICO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NOMBRE",length = 50,nullable = false)
    private String nombre;
    @Column(name = "APELLIDO_PATERNO",length = 50,nullable = false)
    private String apellidoPaterno;
    @Column(name = "APELLIDO_MATERNO",length = 50,nullable = false)
    private String apellidoMaterno;
    @Column(name = "EDAD",nullable = false)
    private Short edad;
    @Column(name = "EMAIL",length = 100,nullable = false)
    private String email;
    @Column(name = "TELEFONO",length = 10,nullable = false)
    private String telefono;
    @Column(name = "CEDULA_PROFESIONAL",length = 12,nullable = false)
    private String cedulaProfesional;
    @Column(name = "ESPECIALIDAD",nullable = false,length = 30)
    @Enumerated(EnumType.STRING)
    private EspecialidadMedico idEspecialidad;
    @Column(name = "DISPONIBILIDAD",length = 30,nullable = false)
    @Enumerated(EnumType.STRING)
    private DisponibilidadMedico disponibilidad;
    @Column(name = "ESTADO_REGISTRO",length = 30,nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoRegistro estadoRegistro;

    public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno, Short edad, String email, String telefono, String cedula, EspecialidadMedico especialidad) {
        validarNoEliminado();
        validarDatos(nombre,apellidoPaterno, apellidoMaterno,edad,email,telefono,cedula,especialidad);
        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.edad = edad;
        this.email = email.trim().toLowerCase();
        this.telefono = telefono.trim();
        this.cedulaProfesional = cedula.trim();

    }

    private void validarDatos(String nombre, String apellidoPaterno, String apellidoMaterno, Short edad, String email, String telefono, String cedula,EspecialidadMedico especialidad){
        StringCustomUtils.validarTamano(nombre,2,50,"El nombre es requerido y debe tener entre 2 y 50 caracteres");
        StringCustomUtils.validarTamano(apellidoPaterno,2,50,"El apellido paterno es requerido y debe tener entre 2 y 50 caracteres");
        StringCustomUtils.validarTamano(apellidoMaterno,2,50,"El apellido Materno es requerido y debe tener entre 2 y 50 caracteres");
        StringCustomUtils.validarTamano(email,10,100,"El email es requerido y debe tener entre 10 y 100 caracteres");
        StringCustomUtils.validarTamano(telefono,10,10,"El telefono es requerido y debe tener 10 caracteres");
        StringCustomUtils.validarTamano(cedula.trim(),12,12,"La cedula es requerida y debe tener 12 caracteres");
        ValoresNumericosUtils.validarRangoShort(edad,(short)18,(short)100,"La edad es requerida y debe tener entre 18 y 100 años");
        if(especialidad==null)throw  new IllegalArgumentException("La especialidad es requerida");
    }
    private void validarNoEliminado(){
        if(this.estadoRegistro==EstadoRegistro.ELIMINADO)throw new IllegalArgumentException("El medico ya esta eliminado");

    }
    public void actualizarEspecialidad(EspecialidadMedico nuevaEspecialidad){
        validarNoEliminado();
        if(nuevaEspecialidad==null)throw new IllegalArgumentException("La nueva especialidad es requerida");
        this.idEspecialidad=nuevaEspecialidad;
    }
    public void eliminar(){
        validarNoEliminado();
        this.estadoRegistro=EstadoRegistro.ELIMINADO;
    }
    public void actualizarDisponibilidad(DisponibilidadMedico nuevaDisponibilidad){
        validarNoEliminado();
        if(nuevaDisponibilidad==null)throw new IllegalArgumentException("La nueva disponibilidad es requerida");
        this.disponibilidad=nuevaDisponibilidad;
    }
}
