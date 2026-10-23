package pe.edu.utp.sistemadereservacionhotel.service.reserva;

import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckOutRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.CheckOutResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrato de negocio para la liberación de la habitación y auditoría de salida.
 */
public interface CheckOutService {

    /**
     * Formaliza la salida del huésped.
     * Debe calcular automáticamente las penalidades si la salida es posterior a la hora límite (Late Check-out).
     */
    CheckOutResponseDTO realizarCheckOut(CheckOutRequestDTO dto);

    CheckOutResponseDTO actualizarCheckOut(Long id, CheckOutRequestDTO dto);

    List<CheckOutResponseDTO> listarTodos();

    CheckOutResponseDTO buscarPorId(Long id);

    CheckOutResponseDTO buscarPorReserva(Long idReserva);

    List<CheckOutResponseDTO> buscarPorRangoFecha(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Búsqueda financiera de salidas penalizadas utilizando alta precisión contable.
     */
    List<CheckOutResponseDTO> buscarConMulta(BigDecimal montoMinimo);
}