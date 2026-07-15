package pe.edu.utp.sistemadereservacionhotel.repository.finanzas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Impuesto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de catálogo para la gestión de tasas impositivas del sistema.
 */
@Repository
public interface ImpuestoRepository extends JpaRepository<Impuesto, Long> {

    Optional<Impuesto> findByNombreImpuesto(String nombreImpuesto);

    boolean existsByNombreImpuesto(String nombreImpuesto);

    List<Impuesto> findByPorcentajeLessThanEqual(BigDecimal porcentaje);
}