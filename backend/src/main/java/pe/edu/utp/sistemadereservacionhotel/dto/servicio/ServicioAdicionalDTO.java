package pe.edu.utp.sistemadereservacionhotel.dto.servicio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Contrato de datos para la gestión del catálogo de servicios adicionales.
 * Este DTO abstrae los detalles operativos de los servicios, excluyendo
 * la lógica financiera que reside en CatalogoServicio.
 */
public record ServicioAdicionalDTO(
        Long idServicio,

        @NotBlank(message = "El nombre del servicio es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String nombre,

        @Size(max = 255, message = "La descripción no debe exceder los 255 caracteres")
        String descripcion,

        @NotNull(message = "El estado de disponibilidad es obligatorio")
        Boolean estaDisponible
) {}