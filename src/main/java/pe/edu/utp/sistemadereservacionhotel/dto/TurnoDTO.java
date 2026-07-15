package pe.edu.utp.sistemadereservacionhotel.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

/**
 * Objeto de Transferencia de Datos inmutable para la gestión de horarios.
 * Aísla la entidad Turno de la capa de presentación y validación web.
 */
public record TurnoDTO(
        Long idTurno,

        @NotNull(message = "La hora de inicio es obligatoria")
        LocalTime horaInicio,

        @NotNull(message = "La hora final es obligatoria")
        LocalTime horaFinal,

        @NotEmpty(message = "Debe asignar al menos un día de la semana")
        Set<DayOfWeek> diasSemana
) {
}