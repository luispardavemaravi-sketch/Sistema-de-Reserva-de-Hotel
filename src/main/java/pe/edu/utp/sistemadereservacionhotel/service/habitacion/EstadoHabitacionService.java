package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import pe.edu.utp.sistemadereservacionhotel.model.habitacion.EstadoHabitacion;

import java.util.List;
import java.util.Optional;

public interface EstadoHabitacionService {

    EstadoHabitacion save(EstadoHabitacion estadoHabitacion);

    EstadoHabitacion update(EstadoHabitacion estadoHabitacion);

    void delete(Long id);

    List<EstadoHabitacion> findAll();

    Optional<EstadoHabitacion> findById(Long id);

    Optional<EstadoHabitacion> findByNombreEstado(String nombreEstado);

    List<EstadoHabitacion> findByEsReservable(Boolean esReservable);

    boolean existsByNombreEstado(String nombreEstado);

    long count();
}