package pe.edu.utp.sistemadereservacionhotel.service.reserva;

import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckInRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckInResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrato de negocio para formalizar la ocupación física de la habitación.
 */
public interface CheckInService {

    /**
     * Formaliza la entrada del huésped.
     * Nota: En la implementación, este método debe cambiar el estado de la Reserva y la Habitación.
     */
    CheckInResponseDTO realizarCheckIn(CheckInRequestDTO dto);

    /**
     * Modifica datos del registro de entrada (ej. corrección de hora por error de sistema).
     */
    CheckInResponseDTO actualizarCheckIn(Long id, CheckInRequestDTO dto);

    List<CheckInResponseDTO> listarTodos();

    CheckInResponseDTO buscarPorId(Long id);

    CheckInResponseDTO buscarPorReserva(Long idReserva);

    List<CheckInResponseDTO> buscarPorRangoFecha(LocalDateTime inicio, LocalDateTime fin);
}