package pe.edu.utp.sistemadereservacionhotel.dto.reserva;


import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CheckOutRequestDTO(
        @NotNull(message = "El ID de la reserva es obligatorio")
        Long idReserva,
        @NotNull BigDecimal multa,
        String observaciones
) {
}