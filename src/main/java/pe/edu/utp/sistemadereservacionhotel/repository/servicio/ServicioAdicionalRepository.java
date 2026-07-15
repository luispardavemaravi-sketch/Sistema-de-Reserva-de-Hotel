package pe.edu.utp.sistemadereservacionhotel.repository.servicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.ServicioAdicional;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de inventario base para los servicios extra del hotel.
 */
@Repository
public interface ServicioAdicionalRepository extends JpaRepository<ServicioAdicional, Long> {

    /**
     * Busca un servicio específico por su nombre exacto.
     */
    Optional<ServicioAdicional> findByNombre(String nombre);

    /**
     * Verifica la existencia de un servicio para evitar colisiones en la creación.
     */
    boolean existsByNombre(String nombre);

    /**
     * Filtra los servicios habilitados o deshabilitados para ser ofrecidos por recepción.
     */
    List<ServicioAdicional> findByEstaDisponible(Boolean estaDisponible);

    /**
     * Búsqueda por texto parcial del nombre del servicio.
     */
    List<ServicioAdicional> findByNombreContainingIgnoreCase(String nombre);

    // ELIMINADO CRÍTICO: El método findByPrecioUnitarioLessThanEqual fue purgado porque
    // la entidad ServicioAdicional ya no posee el atributo precioUnitario.
}