package pe.edu.utp.sistemadereservacionhotel.dto.habitacion;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PrecioHabitacionDTO(Long id, Long idHabitacion, BigDecimal monto, LocalDate fechaInicio, LocalDate fechaFin) {}