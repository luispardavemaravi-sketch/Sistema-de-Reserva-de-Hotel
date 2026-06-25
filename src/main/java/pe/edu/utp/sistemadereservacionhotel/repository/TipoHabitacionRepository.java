package pe.edu.utp.sistemadereservacionhotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.TipoHabitacion;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Long> {
    Optional<TipoHabitacion> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<TipoHabitacion> findByCapacidadMaximaGreaterThanEqual(Integer capacidad);

    List<TipoHabitacion> findByPrecioBaseBetween(Double precioMin, Double precioMax);


}
