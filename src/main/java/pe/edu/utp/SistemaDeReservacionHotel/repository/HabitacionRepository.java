package pe.edu.utp.SistemaDeReservacionHotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.SistemaDeReservacionHotel.model.Habitacion;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    Optional<Habitacion> findByNumeroHabitacion(String numeroHabitacion);

    boolean existsByNumeroHabitacion(String numeroHabitacion);

    List<Habitacion> findByEstadoActivo(boolean estadoActivo);

    List<Habitacion> findByPiso_IdPiso(Long idPiso);

    List<Habitacion> findByTipoHabitacion_IdTipo(Long idTipo);

    List<Habitacion> findByEstadoHabitacion_IdEstadoHabitacion(Long idEstadoHabitacion);

    List<Habitacion> findByPrecioActualBetween(Double precioMin, Double precioMax);



}
