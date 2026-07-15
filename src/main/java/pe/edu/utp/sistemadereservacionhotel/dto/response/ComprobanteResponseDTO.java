package pe.edu.utp.sistemadereservacionhotel.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ComprobanteResponseDTO(
        Long idComprobante,
        String numeroSerie,
        LocalDateTime fechaEmision,
        BigDecimal montoTotal,
        Long idReserva
) {
}