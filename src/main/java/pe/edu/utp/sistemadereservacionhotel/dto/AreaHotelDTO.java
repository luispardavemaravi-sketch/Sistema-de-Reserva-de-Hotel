package pe.edu.utp.sistemadereservacionhotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Contrato de transferencia de datos para la entidad AreaHotel.
 * Utiliza validaciones de Bean Validation para garantizar la integridad del input.
 */
public record AreaHotelDTO(
        Long idAreaHotel,

        @NotBlank(message = "El nombre del área es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String nombreArea,

        @NotBlank(message = "La ubicación es obligatoria")
        @Size(max = 200, message = "La ubicación no puede exceder los 200 caracteres")
        String ubicacion
) {}