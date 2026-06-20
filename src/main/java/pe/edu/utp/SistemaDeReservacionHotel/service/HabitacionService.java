package pe.edu.utp.SistemaDeReservacionHotel.service;

import pe.edu.utp.SistemaDeReservacionHotel.model.Habitacion;

import java.util.List;
import java.util.Optional;

public interface HabitacionService {

    Habitacion save(Habitacion habitacion);

    Habitacion update(Habitacion habitacion);

    void delete(Long id);

    List<Habitacion> findAll();

    Optional<Habitacion> findById(Long id);

    Optional<Habitacion> findByNumeroHabitacion(String numero);

    List<Habitacion> findByEstadoActivo(boolean activo);

    List<Habitacion> findByPiso(Long idPiso);

    List<Habitacion> findByTipo(Long idTipo);

    List<Habitacion> findByEstadoHabitacion(Long idEstadoHabitacion);

    List<Habitacion> findByRangoPrecio(Double precioMin, Double precioMax);

    boolean existsByNumeroHabitacion(String numero);

    long count();
}