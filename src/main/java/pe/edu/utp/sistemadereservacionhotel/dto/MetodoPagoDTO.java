package pe.edu.utp.sistemadereservacionhotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MetodoPagoDTO(
        Long idMetodo,
        @NotBlank(message = "El nombre es obligatorio") String nombreMetodo,
        @NotNull(message = "Indique si es digital") Boolean esDigital
) {
}