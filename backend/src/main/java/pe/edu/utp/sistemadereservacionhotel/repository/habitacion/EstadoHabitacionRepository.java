package pe.edu.utp.sistemadereservacionhotel.repository.habitacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.EstadoHabitacion;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio paramétrico para gestionar la máquina de estados de las habitaciones.
 */
@Repository
public interface EstadoHabitacionRepository extends JpaRepository<EstadoHabitacion, Long> {

    /**
     * Recupera un estado operativo por su nombre exacto.
     */
    Optional<EstadoHabitacion> findByNombreEstado(String nombreEstado);

    /**
     * Valida si un estado operativo ya se encuentra registrado.
     */
    boolean existsByNombreEstado(String nombreEstado);

    /**
     * Filtra los estados según si permiten que la habitación sea reservada (ej. Disponible).
     */
    List<EstadoHabitacion> findByEsReservable(Boolean esReservable);
}