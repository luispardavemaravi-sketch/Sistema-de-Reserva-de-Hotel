package pe.edu.utp.sistemadereservacionhotel.service.personal;

import pe.edu.utp.sistemadereservacionhotel.dto.TurnoDTO;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * Contrato de operaciones de negocio para la gestión de la malla horaria del hotel.
 * Asegura el aislamiento de la capa de persistencia (Entidades) exponiendo únicamente DTO.
 */
public interface TurnoService {

    /**
     * Da de alta un nuevo bloque horario, validando la integridad temporal.
     */
    TurnoDTO registrarTurno(TurnoDTO dto);

    /**
     * Actualiza los parámetros de un turno existente de manera atómica.
     */
    TurnoDTO actualizarTurno(Long id, TurnoDTO dto);

    /**
     * Realiza un borrado físico del turno.
     * Nota: En fases posteriores se debe evaluar si impacta la integridad referencial con el Personal.
     */
    void eliminarTurno(Long id);

    List<TurnoDTO> listarTodos();

    TurnoDTO buscarPorId(Long id);

    /**
     * Busca todos los turnos que incluyen un día específico de la semana.
     * @param dia Día de la semana (LUNES, MARTES, etc.) tipado de manera estricta.
     */
    List<TurnoDTO> buscarPorDia(DayOfWeek dia);

    /**
     * Evalúa qué turnos están activos en una franja horaria determinada.
     */
    List<TurnoDTO> buscarTurnosActivosEnHora(LocalTime hora);
}