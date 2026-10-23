package pe.edu.utp.sistemadereservacionhotel.repository.habitacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Equipamiento;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión del inventario de comodidades y mobiliario extra.
 */
@Repository
public interface EquipamientoRepository extends JpaRepository<Equipamiento, Long> {

    /**
     * Busca un equipamiento específico por su denominación exacta.
     */
    Optional<Equipamiento> findByNombre(String nombre);

    /**
     * Verifica la existencia de un equipamiento para evitar duplicidad en el catálogo.
     */
    boolean existsByNombre(String nombre);

    /**
     * Realiza una búsqueda parcial (LIKE) por nombre, ignorando mayúsculas y minúsculas.
     */
    List<Equipamiento> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Filtra equipamientos cuyo costo de penalidad o alquiler sea menor o igual al límite.
     * CAMBIO CRÍTICO: Refactorizado a BigDecimal para coincidir con la entidad.
     */
    List<Equipamiento> findByCostoAdicionalLessThanEqual(BigDecimal costoMaximo);
}