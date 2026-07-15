package pe.edu.utp.sistemadereservacionhotel.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de salida para exponer la información de la caja de forma segura.
 */
public record CajaResponseDTO(
        Long idCaja,
        LocalDate fecha,
        BigDecimal montoApertura,
        BigDecimal montoCierre,
        Boolean estaAbierta,
        Long idEmpleado
) {}