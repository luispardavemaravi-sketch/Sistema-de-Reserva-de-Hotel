package pe.edu.utp.sistemadereservacionhotel.dto.response;

public record HuespedResponseDTO(
        Long idHuesped,
        String nombre,
        String apellidos,
        String documentoIdentidad,
        String email,
        String telefono,
        Boolean estadoActivo
) {}