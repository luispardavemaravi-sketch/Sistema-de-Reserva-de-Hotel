package pe.edu.utp.sistemadereservacionhotel.repository.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.CheckIn;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio operativo para la auditoría de ingresos de huéspedes (Check-In).
 */
@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    /**
     * Recupera el evento de entrada exacto vinculado a un contrato de reserva.
     */
    Optional<CheckIn> findByReserva_IdReserva(Long idReserva);

    /**
     * Filtra los ingresos físicos ocurridos dentro de un marco temporal (reportes operativos).
     */
    List<CheckIn> findByFechaHoraRealEntradaBetween(LocalDateTime inicio, LocalDateTime fin);
}