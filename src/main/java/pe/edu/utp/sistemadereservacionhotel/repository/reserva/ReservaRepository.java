package pe.edu.utp.sistemadereservacionhotel.repository.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio transaccional núcleo. Gestiona el ciclo de vida de los contratos de alojamiento.
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Búsqueda exacta del contrato mediante su localizador (Business Key).
     */
    Optional<Reserva> findByCodigoReserva(String codigoReserva);

    /**
     * Verifica la existencia de un localizador para evitar duplicidades generadas concurrentemente.
     */
    boolean existsByCodigoReserva(String codigoReserva);

    /**
     * Recupera el historial de reservas de un cliente titular.
     */
    List<Reserva> findByHuesped_IdHuesped(Long idHuesped);

    /**
     * Búsqueda paramétrica para consultar reservas en un estado particular (ej. Confirmadas).
     */
    List<Reserva> findByEstadoReserva_IdEstado(Long idEstado);

    /**
     * Recupera las reservas programadas para iniciar en una fecha concreta.
     */
    List<Reserva> findByFechaEntradaPlanificada(LocalDate fecha);

    /**
     * Recupera el volumen de reservas cuya fecha de inicio caiga dentro de un rango determinado.
     */
    List<Reserva> findByFechaEntradaPlanificadaBetween(LocalDate inicio, LocalDate fin);

    /**
     * CAMBIO CRÍTICO: Recupera todas las reservas vinculadas a un recurso físico.
     * Indispensable para validar disponibilidad e impedir overbooking de la misma habitación.
     */
    List<Reserva> findByHabitacion_IdHabitacion(Long idHabitacion);

    /**
     * CAMBIO CRÍTICO: Consulta el cruce temporal de ocupación para un recurso físico específico.
     * Requerido por la lógica de negocio en la capa de servicio para evaluar disponibilidad.
     */
    List<Reserva> findByHabitacion_IdHabitacionAndFechaEntradaPlanificadaBetween(Long idHabitacion, LocalDate inicio, LocalDate fin);
}L