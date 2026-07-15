package pe.edu.utp.sistemadereservacionhotel.repository.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.EstadoReserva;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de catálogo que define la máquina de estados por la que transita el motor de reservas.
 */
@Repository
public interface EstadoReservaRepository extends JpaRepository<EstadoReserva, Long> {

    /**
     * Recupera un estado de reserva por su denominación oficial.
     */
    Optional<EstadoReserva> findByNombreEstado(String nombreEstado);

    /**
     * Valida la existencia de un estado en el catálogo.
     */
    boolean existsByNombreEstado(String nombreEstado);

    /**
     * Filtra los estados según si permiten realizar modificaciones lógicas sobre la reserva.
     */
    List<EstadoReserva> findByEsModificable(Boolean esModificable);
}