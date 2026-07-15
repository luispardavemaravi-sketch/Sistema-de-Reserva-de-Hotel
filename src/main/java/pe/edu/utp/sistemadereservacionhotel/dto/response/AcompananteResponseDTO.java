package pe.edu.utp.sistemadereservacionhotel.dto.response;

public record AcompananteResponseDTO(
        Long idAcompanante,
        String nombre,
        String apellidos,
        String documentoIdentidad,
        String parentesco,
        Long idReserva
) {}