package pe.edu.utp.sistemadereservacionhotel.model.personal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

/**
 * Entidad que define la estructura horaria y la programación semanal de los empleados.
 * Establece los bloques de tiempo operativos que se asignarán posteriormente al personal.
 */
@Entity
@Table(name = "turno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Turno implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único del turno a nivel de base de datos.
     * Clave primaria gestionada automáticamente por la estrategia de identidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTurno;

    /**
     * Marca de tiempo exacta que define el inicio de la jornada laboral.
     * Restricción de nulidad obligatoria a nivel de aplicación y esquema.
     */
    @NotNull(message = "La hora de inicio es obligatoria")
    @Column(nullable = false)
    private LocalTime horaInicio;

    /**
     * Marca de tiempo exacta que define la finalización de la jornada laboral.
     * Restricción de nulidad obligatoria.
     * Nota técnica: Carece de validación a nivel de entidad para evaluar la coherencia
     * temporal con horaInicio, especialmente en turnos que atraviesan la medianoche.
     */
    @NotNull(message = "La hora final es obligatoria")
    @Column(nullable = false)
    private LocalTime horaFinal;

    /**
     * Registro de los días de la semana en los que este turno tiene vigencia.
     * Limitado a 50 caracteres a nivel de esquema DDL.
     * Nota técnica crítica: El uso de String (texto libre o separado por comas) para
     * representar días de la semana vulnera la primera forma normal (1FN). Impide la
     * indexación eficiente, dificulta las consultas agregadas y no garantiza integridad
     * de dominio. Debe ser refactorizado a una colección de Enums (@ElementCollection)
     * o una tabla intermedia relacional.
     */
    @NotEmpty(message = "Debe asignar al menos un día de la semana")
    @ElementCollection(targetClass = DayOfWeek.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "turno_dias", joinColumns = @JoinColumn(name = "id_turno"))
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private Set<DayOfWeek> diasSemana;
}