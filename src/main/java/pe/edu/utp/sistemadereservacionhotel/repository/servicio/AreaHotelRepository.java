package pe.edu.utp.sistemadereservacionhotel.repository.servicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.AreaHotel;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de catálogo para administrar las zonas y departamentos físicos del hotel.
 */
@Repository
public interface AreaHotelRepository extends JpaRepository<AreaHotel, Long> {

    /**
     * Búsqueda de un área específica por su denominación exacta.
     */
    Optional<AreaHotel> findByNombreArea(String nombreArea);

    /**
     * Validación de existencia para evitar duplicidad de áreas operativas.
     */
    boolean existsByNombreArea(String nombreArea);

    /**
     * Búsqueda parcial de áreas por nombre.
     */
    List<AreaHotel> findByNombreAreaContainingIgnoreCase(String nombre);

    /**
     * Búsqueda parcial de áreas filtradas por su ubicación física o bloque.
     */
    List<AreaHotel> findByUbicacionContainingIgnoreCase(String ubicacion);
}