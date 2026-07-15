package pe.edu.utp.sistemadereservacionhotel.dto.response;

import java.math.BigDecimal;

public record HabitacionResponseDTO(Long id, String numeroHabitacion, String tipoHabitacion, BigDecimal precioActual, boolean activo) {}