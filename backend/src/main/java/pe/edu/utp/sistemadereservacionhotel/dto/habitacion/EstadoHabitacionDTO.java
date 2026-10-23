package pe.edu.utp.sistemadereservacionhotel.dto.habitacion;

import jakarta.validation.constraints.NotBlank;

public record EstadoHabitacionDTO(
        Long idEstado,
        @NotBlank(message = "El nombre del estado es obligatorio")
        String nombreEstado
) {
}