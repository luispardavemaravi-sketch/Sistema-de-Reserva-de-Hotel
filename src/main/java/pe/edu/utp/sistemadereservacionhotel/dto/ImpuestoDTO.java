package pe.edu.utp.sistemadereservacionhotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * Objeto de Transferencia de Datos inmutable para la gestión de impuestos.
 * Actúa como barrera de seguridad entre la capa de presentación y el dominio.
 */
public record ImpuestoDTO(
        Long idImpuesto,

        @NotBlank(message = "El nombre del impuesto es obligatorio")
        String nombreImpuesto,

        @NotNull(message = "El porcentaje es obligatorio")
        @PositiveOrZero(message = "El porcentaje no puede ser negativo")
        BigDecimal porcentaje
) {}