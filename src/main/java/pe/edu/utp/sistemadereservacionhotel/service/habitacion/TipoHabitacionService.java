package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import pe.edu.utp.sistemadereservacionhotel.dto.TipoHabitacionDTO;

import java.math.BigDecimal;
import java.util.List;

public interface TipoHabitacionService {
    // ELIMINADO save() y update() genéricos.
    // Usamos métodos de intención de negocio.
    TipoHabitacionDTO registrarTipo(TipoHabitacionDTO dto);

    TipoHabitacionDTO actualizarTipo(Long id, TipoHabitacionDTO dto);

    List<TipoHabitacionDTO> listarTodos();

    TipoHabitacionDTO buscarPorId(Long id);

    // Corregido: BigDecimal en lugar de Double
    List<TipoHabitacionDTO> buscarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax);
}