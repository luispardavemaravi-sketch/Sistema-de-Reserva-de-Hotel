package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.MetodoPagoDTO;

import java.util.List;

/**
 * Servicio paramétrico para el catálogo de medios de pago.
 * CORRECCIÓN: Entidad MetodoPago reemplazada por MetodoPagoDTO.
 */
public interface MetodoPagoService {

    MetodoPagoDTO registrarMetodoPago(MetodoPagoDTO dto);

    MetodoPagoDTO actualizarMetodoPago(Long id, MetodoPagoDTO dto);

    void eliminarMetodoPago(Long id);

    List<MetodoPagoDTO> listarTodos();

    MetodoPagoDTO buscarPorId(Long id);

    MetodoPagoDTO buscarPorNombre(String nombre);

    List<MetodoPagoDTO> buscarPorEsDigital(Boolean esDigital);
}