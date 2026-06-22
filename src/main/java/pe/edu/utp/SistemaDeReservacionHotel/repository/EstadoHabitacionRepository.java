package pe.edu.utp.SistemaDeReservacionHotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.SistemaDeReservacionHotel.model.EstadoHabitacion;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadoHabitacionRepository extends JpaRepository<EstadoHabitacion, Long> {
    Optional<EstadoHabitacion> findByNombreEstado(String nombreEstado);

    boolean existsByNombreEstado(String nombreEstado);

    List<EstadoHabitacion> findByEsReservable(Boolean esReservable);
}
