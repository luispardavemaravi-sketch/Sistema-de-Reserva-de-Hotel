package pe.edu.utp.sistemadereservacionhotel.repository.habitacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.TipoHabitacion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de catálogo que administra las categorías comerciales del hotel.
 */
@Repository
public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Long> {

    /**
     * Busca una categoría comercial por su nombre exacto.
     */
    Optional<TipoHabitacion> findByNombre(String nombre);

    /**
     * Verifica la existencia de una categoría para evitar duplicidades en el catálogo.
     */
    boolean existsByNombre(String nombre);

    /**
     * Filtra categorías que soporten un aforo mayor o igual al requerido por el cliente.
     */
    List<TipoHabitacion> findByCapacidadMaximaGreaterThanEqual(Integer capacidad);

    /**
     * Recupera tipos de habitación cuyo costo referencial se ajuste a un rango.
     *
     */
    List<TipoHabitacion> findByPrecioBaseBetween(BigDecimal precioMin, BigDecimal precioMax);
}