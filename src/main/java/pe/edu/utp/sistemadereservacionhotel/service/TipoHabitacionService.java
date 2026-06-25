package pe.edu.utp.sistemadereservacionhotel.service;

import pe.edu.utp.sistemadereservacionhotel.model.TipoHabitacion;

import java.util.List;
import java.util.Optional;

public interface TipoHabitacionService {
    TipoHabitacion save(TipoHabitacion tipoHabitacion);

    TipoHabitacion update(TipoHabitacion tipoHabitacion);

    void delete(Long id);

    List<TipoHabitacion> findAll();

    Optional<TipoHabitacion> findById(Long id);

    Optional<TipoHabitacion> findByNombre(String nombre);

    List<TipoHabitacion> findByCapacidadMinima(Integer capacidad);

    List<TipoHabitacion> findByRangoPrecio(Double precioMin, Double precioMax);

    boolean existsByNombre(String nombre);

    long count();
}
