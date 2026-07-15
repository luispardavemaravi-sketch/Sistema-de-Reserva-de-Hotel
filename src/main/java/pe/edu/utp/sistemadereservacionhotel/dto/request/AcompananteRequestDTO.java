package pe.edu.utp.sistemadereservacionhotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AcompananteRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "Los apellidos son obligatorios") String apellidos,
        @NotBlank(message = "El documento es obligatorio") String documentoIdentidad,
        @NotBlank(message = "El parentesco es obligatorio") String parentesco,
        @NotNull(message = "El ID de la reserva es obligatorio") Long idReserva
) {}