package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import pe.edu.utp.sistemadereservacionhotel.dto.habitacion.PrecioHabitacionDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la administración de tarifas dinámicas.
 */
public interface PrecioHabitacionService {

    /**
     * Establece una nueva tarifa, validando que no existan traslapes temporales
     * con tarifas ya registradas para la misma habitación.
     */
    PrecioHabitacionDTO establecerTarifa(PrecioHabitacionDTO dto);

    List<PrecioHabitacionDTO> listarHistorialPorHabitacion(Long idHabitacion);

    List<PrecioHabitacionDTO> buscarPorRangoFecha(LocalDate inicio, LocalDate fin);

    List<PrecioHabitacionDTO> buscarPorRangoMonto(BigDecimal min, BigDecimal max);
}