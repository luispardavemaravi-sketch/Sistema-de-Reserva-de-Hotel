package pe.edu.utp.sistemadereservacionhotel.service.reserva;

import pe.edu.utp.sistemadereservacionhotel.dto.reserva.AcompananteRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.reserva.AcompananteResponseDTO;

import java.util.List;

/**
 * Servicio para la gestión de los co-ocupantes de una habitación.
 * Vincula la identidad de los acompañantes al identificador maestro de la Reserva.
 */
public interface AcompananteService {

    AcompananteResponseDTO registrarAcompanante(AcompananteRequestDTO dto);

    AcompananteResponseDTO actualizarAcompanante(Long id, AcompananteRequestDTO dto);

    void eliminarAcompanante(Long id);

    List<AcompananteResponseDTO> listarTodos();

    AcompananteResponseDTO buscarPorId(Long id);

    List<AcompananteResponseDTO> buscarPorReserva(Long idReserva);

    List<AcompananteResponseDTO> buscarPorDocumentoIdentidad(String documento);
}