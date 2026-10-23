package pe.edu.utp.sistemadereservacionhotel.dto.servicio;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Contrato de datos para registrar consumos durante la estadía.
 */
public record ConsumoServicioDTO(
        Long idConsumo,
        @NotNull(message = "La reserva es obligatoria") Long idReserva,
        @NotNull(message = "El servicio es obligatorio") Long idServicio,
        @NotNull @Positive(message = "La cantidad debe ser mayor a 0") Integer cantidad,
        BigDecimal subTotal,
        LocalDateTime fechaHora
) {}