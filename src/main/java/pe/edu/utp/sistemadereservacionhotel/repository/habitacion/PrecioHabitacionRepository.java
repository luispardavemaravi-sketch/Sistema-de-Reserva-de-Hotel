package pe.edu.utp.sistemadereservacionhotel.repository.habitacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.PrecioHabitacion;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrecioHabitacionRepository extends JpaRepository<PrecioHabitacion, Long> {

    List<PrecioHabitacion> findByHabitacion_IdHabitacion(Long idHabitacion);

    List<PrecioHabitacion> findByFechaInicioBetween(LocalDate inicio, LocalDate fin);

    List<PrecioHabitacion> findByMontoBetween(Double montoMin, Double montoMax);
}