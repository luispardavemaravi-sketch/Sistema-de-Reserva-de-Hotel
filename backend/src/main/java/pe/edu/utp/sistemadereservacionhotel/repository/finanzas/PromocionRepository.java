package pe.edu.utp.sistemadereservacionhotel.repository.finanzas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Promocion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para el control y validación de cupones de descuento.
 */
@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {

    /**
     * Busca una promoción específica mediante su código alfanumérico.
     */
    Optional<Promocion> findByCodigoCupon(String codigoCupon);

    /**
     * Verifica la existencia de un código promocional en la base de datos.
     */
    boolean existsByCodigoCupon(String codigoCupon);

    /**
     * Recupera únicamente las promociones que aún no han superado su fecha de caducidad.
     */
    List<Promocion> findByFechaCaducidadAfter(LocalDate fecha);

    /**
     * Filtra promociones que ofrezcan un descuento mayor o igual al parámetro indicado.
     * Nota técnica: Parámetro refactorizado a BigDecimal por coherencia estructural.
     */
    List<Promocion> findByPorcentajeDescuentoGreaterThanEqual(BigDecimal porcentaje);
}