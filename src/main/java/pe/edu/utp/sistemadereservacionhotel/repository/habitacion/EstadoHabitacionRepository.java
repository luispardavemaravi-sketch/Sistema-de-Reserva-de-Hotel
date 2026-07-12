package pe.edu.utp.sistemadereservacionhotel.repository.habitacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.EstadoHabitacion;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstadoHabitacionRepository extends JpaRepository<EstadoHabitacion, Long> {
    Optional<EstadoHabitacion> findByNombreEstado(String nombreEstado);

    boolean existsByNombreEstado(String nombreEstado);

    List<EstadoHabitacion> findByEsReservable(Boolean esReservable);
}
