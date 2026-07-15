package pe.edu.utp.sistemadereservacionhotel.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pe.edu.utp.sistemadereservacionhotel.model.personal.CargoEmpleado;
import pe.edu.utp.sistemadereservacionhotel.model.personal.EspecialidadEmpleado;

public record EmpleadoRequestDTO(
        @NotBlank String nombre,
        @NotBlank String apellido,
        @NotBlank @Email String email,
        @NotBlank String telefono,
        @NotNull CargoEmpleado cargo,
        @NotNull EspecialidadEmpleado especialidad,
        @NotBlank String direccion,
        @NotBlank String ciudad,
        @NotNull Long idTurno
) {}