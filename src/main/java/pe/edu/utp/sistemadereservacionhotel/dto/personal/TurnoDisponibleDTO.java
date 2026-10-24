package pe.edu.utp.sistemadereservacionhotel.dto.personal;

import java.time.LocalTime;

/** Datos públicos mínimos para el selector de ingreso, sin información de personal. */
public record TurnoDisponibleDTO(Long idTurno, LocalTime horaInicio, LocalTime horaFinal) {}
