package pe.edu.utp.sistemadereservacionhotel.dto.finanzas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PromocionDTO(
        Long idPromocion,
        @NotBlank(message = "El código es obligatorio") String codigoCupon,
        @NotNull(message = "El porcentaje es obligatorio")
        @PositiveOrZero(message = "No puede ser negativo") BigDecimal porcentajeDescuento,
        @NotNull(message = "La fecha de caducidad es obligatoria") LocalDate fechaCaducidad
) {}