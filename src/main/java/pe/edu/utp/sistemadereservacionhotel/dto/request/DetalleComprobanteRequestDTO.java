package pe.edu.utp.sistemadereservacionhotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record DetalleComprobanteRequestDTO(
        @NotNull(message = "La cantidad es obligatoria") @Positive Integer cantidad,
        @NotBlank(message = "La descripción es obligatoria") String descripcion,
        @NotNull(message = "El precio unitario es obligatorio") @Positive BigDecimal precioUnitario,
        @NotNull(message = "El ID del comprobante es obligatorio") Long idComprobante
) {}