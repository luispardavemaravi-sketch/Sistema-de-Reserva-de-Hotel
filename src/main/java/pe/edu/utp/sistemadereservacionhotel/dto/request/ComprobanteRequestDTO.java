package pe.edu.utp.sistemadereservacionhotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComprobanteRequestDTO(
        @NotNull(message = "El ID de la reserva es obligatorio")
        Long idReserva,

        @NotBlank(message = "El tipo de comprobante es obligatorio (ej. FACTURA, BOLETA)")
        String tipoComprobante
) {
}