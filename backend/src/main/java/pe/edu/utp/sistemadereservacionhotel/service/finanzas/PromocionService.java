package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import pe.edu.utp.sistemadereservacionhotel.dto.finanzas.PromocionDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio de validación y control para los cupones y campañas promocionales.
 * CORRECCIÓN: Refactorizado a DTOs y reemplazo crítico de Double por BigDecimal.
 */
public interface PromocionService {

    PromocionDTO registrarPromocion(PromocionDTO dto);

    PromocionDTO actualizarPromocion(Long id, PromocionDTO dto);

    void eliminarPromocion(Long id);

    List<PromocionDTO> listarTodos();

    PromocionDTO buscarPorId(Long id);

    PromocionDTO buscarPorCodigoCupon(String codigo);

    List<PromocionDTO> buscarVigentes();

    List<PromocionDTO> buscarPorPorcentajeMinimo(BigDecimal porcentaje);
}