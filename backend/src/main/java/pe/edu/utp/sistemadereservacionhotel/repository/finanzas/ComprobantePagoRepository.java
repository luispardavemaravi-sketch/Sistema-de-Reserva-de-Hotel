package pe.edu.utp.sistemadereservacionhotel.repository.finanzas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de persistencia de los comprobantes de facturación.
 */
@Repository
public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {

    /**
     * Busca un comprobante exacto mediante su número de serie legal.
     */
    Optional<ComprobantePago> findByNumeroSerie(String numeroSerie);

    /**
     * Verifica la existencia de un número de serie para validaciones previas a la inserción.
     */
    boolean existsByNumeroSerie(String numeroSerie);

    /**
     * Recupera el comprobante de pago asociado a una reserva específica.
     */
    Optional<ComprobantePago> findByReserva_IdReserva(Long idReserva);

    /**
     * Recupera los comprobantes emitidos en un periodo determinado (reportes fiscales).
     */
    List<ComprobantePago> findByFechaEmisionBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Recupera comprobantes cuyo importe total se encuentre dentro de un rango.
     * Nota técnica: Parámetros corregidos de Double a BigDecimal para mantener
     * coherencia con la entidad y evitar colapsos en la validación del proxy JPA.
     */
    List<ComprobantePago> findByMontoTotalBetween(BigDecimal min, BigDecimal max);
}