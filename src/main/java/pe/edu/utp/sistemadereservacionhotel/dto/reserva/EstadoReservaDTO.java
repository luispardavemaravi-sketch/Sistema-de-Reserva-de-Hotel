package pe.edu.utp.sistemadereservacionhotel.dto.reserva;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EstadoReservaDTO(
        Long idEstado,
        @NotBlank(message = "El nombre del estado es obligatorio") String nombreEstado,
        @NotNull(message = "El flag de modificación es obligatorio") Boolean esModificable
) {}