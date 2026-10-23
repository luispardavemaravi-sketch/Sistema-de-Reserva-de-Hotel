package pe.edu.utp.sistemadereservacionhotel.dto.servicio;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Contrato de datos para el catálogo de servicios.
 * Se utiliza BigDecimal para precios para garantizar precisión en cálculos financieros.
 */
public record CatalogoServicioDTO(
        Long idCatalogo,

        @NotBlank(message = "La categoría es obligatoria")
        String categoria,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        BigDecimal precio
) {
}