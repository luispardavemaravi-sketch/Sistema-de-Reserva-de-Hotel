package pe.edu.utp.sistemadereservacionhotel.repository.finanzas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Promocion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {
    Optional<Promocion> findByCodigoCupon(String codigoCupon);

    boolean existsByCodigoCupon(String codigoCupon);

    List<Promocion> findByFechaCaducidadAfter(LocalDate fecha);

    List<Promocion> findByPorcentajeDescuentoGreaterThanEqual(Double porcentaje);
}