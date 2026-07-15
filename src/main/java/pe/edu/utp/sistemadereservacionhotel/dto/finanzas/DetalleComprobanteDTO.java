package pe.edu.utp.sistemadereservacionhotel.dto.finanzas;

import java.math.BigDecimal;

/**
 * Representa el detalle desglosado de un comprobante de pago.
 */
public record DetalleComprobanteDTO(
        Long idDetalle,
        Long idComprobante,
        String descripcionItem,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subTotal
) {}