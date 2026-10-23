package pe.edu.utp.sistemadereservacionhotel.repository.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.CheckOut;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio operativo para la auditoría de salidas de huéspedes (Check-Out) y penalizaciones.
 */
@Repository
public interface CheckOutRepository extends JpaRepository<CheckOut, Long> {

    /**
     * Recupera el evento de salida exacto vinculado a un contrato de reserva.
     */
    Optional<CheckOut> findByReserva_IdReserva(Long idReserva);

    /**
     * Filtra las salidas físicas ocurridas dentro de un marco temporal.
     */
    List<CheckOut> findByFechaHoraRealSalidaBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Identifica los eventos de salida que superen un umbral específico de penalización económica.
     * CAMBIO CRÍTICO: Refactorizado de Double a BigDecimal.
     */
    List<CheckOut> findByMultaPorRetrasoGreaterThan(BigDecimal monto);
}