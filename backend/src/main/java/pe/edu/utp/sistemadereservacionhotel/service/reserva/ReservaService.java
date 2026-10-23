package pe.edu.utp.sistemadereservacionhotel.service.reserva;

import pe.edu.utp.sistemadereservacionhotel.dto.reserva.ReservaRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.ReservaResponseDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio central del motor de reservas del hotel.
 * Orquesta la disponibilidad, la asignación de tarifas y el estado del ciclo de vida del alojamiento.
 */
public interface ReservaService {

    /**
     * Orquesta la creación de una nueva reserva.
     * En su implementación debe utilizar el ReservaBuilder y las Factories pertinentes.
     */
    ReservaResponseDTO crearReserva(ReservaRequestDTO dto);

    ReservaResponseDTO actualizarReserva(Long id, ReservaRequestDTO dto);

    /**
     * Aplica la cancelación de una reserva.
     * Implica un cambio de estado y la liberación inmediata del inventario (Habitación).
     */
    void cancelarReserva(Long id);

    List<ReservaResponseDTO> listarTodas();

    ReservaResponseDTO buscarPorId(Long id);

    ReservaResponseDTO buscarPorCodigo(String codigoReserva);

    List<ReservaResponseDTO> buscarPorHuesped(Long idHuesped);

    List<ReservaResponseDTO> buscarPorEstado(Long idEstado);

    List<ReservaResponseDTO> buscarPorFechaEntrada(LocalDate fecha);

    List<ReservaResponseDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);
}