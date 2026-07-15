package pe.edu.utp.sistemadereservacionhotel.dto.response;

import java.time.LocalDateTime;

public record CheckInResponseDTO(
        Long idCheckIn,
        Long idReserva,
        LocalDateTime fechaHoraEntrada,
        String observaciones
) {}