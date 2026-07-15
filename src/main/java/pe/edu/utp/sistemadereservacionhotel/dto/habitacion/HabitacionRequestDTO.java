package pe.edu.utp.sistemadereservacionhotel.dto.habitacion;

import java.math.BigDecimal;

public record HabitacionRequestDTO(String numeroHabitacion, Long idPiso, Long idTipoHabitacion, BigDecimal precioActual) {}