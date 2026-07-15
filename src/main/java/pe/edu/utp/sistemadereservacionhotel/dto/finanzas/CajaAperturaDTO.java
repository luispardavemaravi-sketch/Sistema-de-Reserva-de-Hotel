package pe.edu.utp.sistemadereservacionhotel.dto.finanzas;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * DTO para la petición de apertura de una nueva sesión de caja.
 */
public record CajaAperturaDTO(
        @NotNull(message = "El ID del empleado es obligatorio")
        Long idEmpleado,

        @NotNull(message = "El monto de apertura es obligatorio")
        @PositiveOrZero(message = "El monto no puede ser negativo")
        BigDecimal montoApertura
) {
}