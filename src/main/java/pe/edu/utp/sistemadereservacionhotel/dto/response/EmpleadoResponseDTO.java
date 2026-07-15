package pe.edu.utp.sistemadereservacionhotel.dto.response;

import pe.edu.utp.sistemadereservacionhotel.model.personal.CargoEmpleado;
import pe.edu.utp.sistemadereservacionhotel.model.personal.EspecialidadEmpleado;

public record EmpleadoResponseDTO(
        Long idEmpleado,
        String nombre,
        String apellido,
        String email,
        String telefono,
        CargoEmpleado cargo,
        EspecialidadEmpleado especialidad,
        String direccion,
        String ciudad,
        Long idTurno,
        Boolean estadoActivo
) {}