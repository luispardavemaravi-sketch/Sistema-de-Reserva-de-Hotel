package pe.edu.utp.sistemadereservacionhotel.dto.reserva;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservaRequestDTO(
        @NotNull(message = "El huésped es obligatorio") Long idHuesped,
        @NotNull(message = "La habitación es obligatoria") Long idHabitacion,
        @NotNull(message = "La fecha de entrada es obligatoria") LocalDate fechaEntradaPlanificada,
        @NotNull(message = "La fecha de salida es obligatoria") LocalDate fechaSalidaPlanificada
) {}