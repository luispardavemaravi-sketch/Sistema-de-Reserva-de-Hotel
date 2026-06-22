package pe.edu.utp.SistemaDeReservacionHotel.service;

import pe.edu.utp.SistemaDeReservacionHotel.model.EstadoHabitacion;

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