package pe.edu.utp.sistemadereservacionhotel.repository.habitacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Piso;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio arquitectónico para gestionar la distribución espacial del hotel.
 */
@Repository
public interface PisoRepository extends JpaRepository<Piso, Long> {

    /**
     * Busca un piso exacto por su nivel numérico.
     */
    Optional<Piso> findByNumeroPiso(Integer numeroPiso);

    /**
     * Valida la existencia de un nivel para evitar colisiones arquitectónicas.
     */
    boolean existsByNumeroPiso(Integer numeroPiso);

    /**
     * Busca pisos que pertenezcan a un sector o ala específica (ej. "Norte") usando texto parcial.
     */
    List<Piso> findBySectorContainingIgnoreCase(String sector);
}