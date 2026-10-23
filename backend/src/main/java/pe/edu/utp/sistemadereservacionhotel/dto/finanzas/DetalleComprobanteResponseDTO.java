package pe.edu.utp.sistemadereservacionhotel.dto.finanzas;

import java.math.BigDecimal;

public record DetalleComprobanteResponseDTO(
        Long idDetalle,
        Integer cantidad,
        String descripcion,
        BigDecimal precioUnitario,
        BigDecimal subtotalLinea,
        Long idComprobante
) {
}