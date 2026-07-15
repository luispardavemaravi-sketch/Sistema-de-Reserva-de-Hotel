package pe.edu.utp.sistemadereservacionhotel.dto.reserva;

public record AcompananteResponseDTO(
        Long idAcompanante,
        String nombre,
        String apellidos,
        String documentoIdentidad,
        String parentesco,
        Long idReserva
) {}