package pe.edu.utp.sistemadereservacionhotel.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CheckOutResponseDTO(
        Long idCheckOut,
        Long idReserva,
        LocalDateTime fechaHoraSalida,
        BigDecimal multaAplicada,
        String observaciones
) {}