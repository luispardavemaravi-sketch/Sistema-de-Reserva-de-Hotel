package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.HabitacionRequestDTO;
import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.HabitacionResponseDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio transaccional para la gestión del inventario físico.
 * Aísla la persistencia mediante DTOs y gestiona cambios de estado operativo.
 */
public interface HabitacionService {

    HabitacionResponseDTO registrarHabitacion(HabitacionRequestDTO dto);

    HabitacionResponseDTO actualizarHabitacion(Long id, HabitacionRequestDTO dto);

    void darDeBajaHabitacion(Long id); // Borrado lógico

    List<HabitacionResponseDTO> listarTodas();

    HabitacionResponseDTO buscarPorId(Long id);

    HabitacionResponseDTO buscarPorNumero(String numero);

    List<HabitacionResponseDTO> buscarPorRangoPrecio(BigDecimal min, BigDecimal max);

    HabitacionResponseDTO actualizarTarifa(Long id, BigDecimal nuevoPrecio);
}