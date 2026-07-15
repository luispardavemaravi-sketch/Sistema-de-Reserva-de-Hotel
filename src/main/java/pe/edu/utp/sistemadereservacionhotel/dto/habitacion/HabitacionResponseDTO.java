    package pe.edu.utp.sistemadereservacionhotel.dto.habitacion;

    import java.math.BigDecimal;

    public record HabitacionResponseDTO(Long id, String numeroHabitacion, String tipoHabitacion, BigDecimal precioActual, boolean activo) {}