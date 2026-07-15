package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import pe.edu.utp.sistemadereservacionhotel.dto.request.ComprobanteRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.response.ComprobanteResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio transaccional para la orquestación, emisión y anulación de facturación.
 * CORRECCIÓN: Erradicadas las entidades ComprobantePago y Reserva. Uso estricto de DTOs y BigDecimal.
 */
public interface ComprobantePagoService {

    /**
     * Orquesta la generación de un nuevo comprobante, validando la reserva y calculando impuestos.
     */
    ComprobanteResponseDTO emitirComprobante(ComprobanteRequestDTO request);

    /**
     * Invalida un comprobante legalmente emitido. No realiza borrado físico.
     */
    ComprobanteResponseDTO anularComprobante(Long idComprobante);

    List<ComprobanteResponseDTO> listarTodos();

    ComprobanteResponseDTO buscarPorId(Long id);

    ComprobanteResponseDTO buscarPorNumeroSerie(String serie);

    ComprobanteResponseDTO buscarPorReserva(Long idReserva);

    List<ComprobanteResponseDTO> buscarPorRangoFecha(LocalDateTime inicio, LocalDateTime fin);

    List<ComprobanteResponseDTO> buscarPorRangoMonto(BigDecimal min, BigDecimal max);
}