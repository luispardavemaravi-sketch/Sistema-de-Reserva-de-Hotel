package pe.edu.utp.sistemadereservacionhotel.dto.request;

import jakarta.validation.constraints.NotNull;

public record CheckInRequestDTO(
        @NotNull(message = "El ID de la reserva es obligatorio") Long idReserva,
        String observaciones
) {}