package pe.edu.utp.sistemadereservacionhotel.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record HuespedRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "Los apellidos son obligatorios")
        String apellidos,

        @NotBlank(message = "El documento de identidad es obligatorio")
        @Size(min = 8, max = 15, message = "El documento debe tener entre 8 y 15 caracteres")
        String documentoIdentidad,

        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "El formato del correo electrónico no es válido")
        String email,

        String telefono
) {}