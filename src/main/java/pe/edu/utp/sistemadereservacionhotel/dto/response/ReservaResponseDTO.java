package pe.edu.utp.sistemadereservacionhotel.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservaResponseDTO(
        Long idReserva,
        String codigoReserva,
        LocalDateTime fechaReserva,
        LocalDate fechaEntradaPlanificada,
        LocalDate fechaSalidaPlanificada,
        BigDecimal montoTotalEstimado,
        Long idHuesped,
        Long idHabitacion,
        String estadoReserva
) {}