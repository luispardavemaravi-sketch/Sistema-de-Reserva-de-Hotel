package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import pe.edu.utp.sistemadereservacionhotel.dto.request.DetalleComprobanteRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.response.DetalleComprobanteResponseDTO;

import java.util.List;

/**
 * Servicio para la gestión de las líneas de detalle de facturación.
 * CORRECCIÓN: Refactorizado para usar DTOs y aislar la capa de persistencia.
 */
public interface DetalleComprobanteService {

    DetalleComprobanteResponseDTO agregarDetalle(DetalleComprobanteRequestDTO request);

    DetalleComprobanteResponseDTO actualizarDetalle(Long id, DetalleComprobanteRequestDTO request);

    void eliminarDetalle(Long id);

    List<DetalleComprobanteResponseDTO> listarTodos();

    DetalleComprobanteResponseDTO buscarPorId(Long id);

    List<DetalleComprobanteResponseDTO> buscarPorComprobante(Long idComprobante);

    List<DetalleComprobanteResponseDTO> buscarPorDescripcion(String descripcion);
}