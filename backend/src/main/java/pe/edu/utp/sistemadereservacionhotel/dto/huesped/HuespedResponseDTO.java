package pe.edu.utp.sistemadereservacionhotel.dto.huesped;

public record HuespedResponseDTO(
        Long idHuesped,
        String nombre,
        String apellidos,
        String documentoIdentidad,
        String email,
        String telefono,
        Boolean estadoActivo
) {}