package pe.edu.utp.sistemadereservacionhotel.service.reserva;

import pe.edu.utp.sistemadereservacionhotel.dto.EstadoReservaDTO;

import java.util.List;

/**
 * Servicio paramétrico para gestionar el ciclo de vida de una reserva (Ej. Pendiente, Confirmada, Cancelada).
 */
public interface EstadoReservaService {

    EstadoReservaDTO registrarEstado(EstadoReservaDTO dto);

    EstadoReservaDTO actualizarEstado(Long id, EstadoReservaDTO dto);

    void eliminarEstado(Long id);

    List<EstadoReservaDTO> listarTodos();

    EstadoReservaDTO buscarPorId(Long id);

    EstadoReservaDTO buscarPorNombre(String nombreEstado);

    List<EstadoReservaDTO> buscarPorEsModificable(Boolean esModificable);
}