package pe.edu.utp.sistemadereservacionhotel.dto.reserva;

import jakarta.validation.constraints.NotBlank;

public record AcompananteRequestDTO(
        Long idAcompanante,
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,
        @NotBlank(message = "Los apellidos son obligatorios") String apellidos,
        @NotBlank(message = "El documento es obligatorio") String documentoIdentidad,
        @NotBlank(message = "El parentesco es obligatorio") String parentesco,
        Long idReserva
) {
}