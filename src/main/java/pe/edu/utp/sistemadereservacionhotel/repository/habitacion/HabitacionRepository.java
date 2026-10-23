package pe.edu.utp.sistemadereservacionhotel.repository.habitacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Habitacion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio transaccional central para la consulta del inventario físico del hotel.
 */
@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select h from Habitacion h where h.idHabitacion = :id")
    Optional<Habitacion> lockById(@Param("id") Long id);

    /**
     * Localiza una habitación por su identificador alfanumérico visible.
     */
    Optional<Habitacion> findByNumeroHabitacion(String numeroHabitacion);

    /**
     * Válida la existencia física de una habitación mediante su número.
     */
    boolean existsByNumeroHabitacion(String numeroHabitacion);

    /**
     * Recupera el inventario de habitaciones descartando aquellas con borrado lógico.
     */
    List<Habitacion> findByEstadoActivo(boolean estadoActivo);

    /**
     * Filtra las habitaciones que pertenecen a un nivel arquitectónico (Piso) específico.
     */
    List<Habitacion> findByPiso_IdPiso(Long idPiso);

    /**
     * Filtra el inventario basado en su categoría comercial (ej. Suite, Doble).
     */
    List<Habitacion> findByTipoHabitacion_IdTipo(Long idTipo);

    /**
     * Recupera habitaciones que se encuentran en un estado operativo particular.
     */
    List<Habitacion> findByEstadoHabitacionIdEstado(Long idEstado);

    /**
     * Busca habitaciones cuya tarifa base actual se encuentre dentro de un presupuesto.
     * CAMBIO CRÍTICO: Refactorings de Double a BigDecimal para evitar el colapso del mapeo JPA.
     */
    List<Habitacion> findByPrecioActualBetween(BigDecimal precioMin, BigDecimal precioMax);
}
