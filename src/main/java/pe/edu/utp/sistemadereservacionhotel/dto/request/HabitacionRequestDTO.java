package pe.edu.utp.sistemadereservacionhotel.dto.request;

import java.math.BigDecimal;

public record HabitacionRequestDTO(String numeroHabitacion, Long idPiso, Long idTipoHabitacion, BigDecimal precioActual) {}