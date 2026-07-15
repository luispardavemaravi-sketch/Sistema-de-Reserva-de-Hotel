package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.EstadoHabitacionDTO;

import java.util.List;

public interface EstadoHabitacionService {

    EstadoHabitacionDTO registrar(EstadoHabitacionDTO dto);

    EstadoHabitacionDTO actualizar(Long id, EstadoHabitacionDTO dto);

    void eliminar(Long id);

    List<EstadoHabitacionDTO> listarTodos();

    EstadoHabitacionDTO buscarPorId(Long id);
}