package pe.edu.utp.sistemadereservacionhotel.repository.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.CheckOut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckOutRepository extends JpaRepository<CheckOut, Long> {

    Optional<CheckOut> findByReserva_IdReserva(Long idReserva);

    List<CheckOut> findByFechaHoraRealSalidaBetween(LocalDateTime inicio, LocalDateTime fin);

    List<CheckOut> findByMultaPorRetrasoGreaterThan(Double monto);
}