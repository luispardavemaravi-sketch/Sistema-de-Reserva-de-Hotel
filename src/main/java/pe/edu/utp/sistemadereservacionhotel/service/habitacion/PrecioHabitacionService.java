package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import pe.edu.utp.sistemadereservacionhotel.model.habitacion.PrecioHabitacion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PrecioHabitacionService {

    PrecioHabitacion save(PrecioHabitacion precioHabitacion);

    PrecioHabitacion update(PrecioHabitacion precioHabitacion);

    void delete(Long id);

    List<PrecioHabitacion> findAll();

    Optional<PrecioHabitacion> findById(Long id);

    List<PrecioHabitacion> findByHabitacion(Long idHabitacion);

    List<PrecioHabitacion> findByRangoFecha(LocalDate inicio, LocalDate fin);

    List<PrecioHabitacion> findByRangoMonto(Double montoMin, Double montoMax);

    long count();
}