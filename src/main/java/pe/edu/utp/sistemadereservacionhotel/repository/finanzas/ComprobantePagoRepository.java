package pe.edu.utp.sistemadereservacionhotel.repository.finanzas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComprobantePagoRepository extends JpaRepository<ComprobantePago, Long> {
    Optional<ComprobantePago> findByNumeroSerie(String numeroSerie);

    boolean existsByNumeroSerie(String numeroSerie);

    Optional<ComprobantePago> findByReserva_IdReserva(Long idReserva);

    List<ComprobantePago> findByFechaEmisionBetween(LocalDateTime inicio, LocalDateTime fin);

    List<ComprobantePago> findByMontoTotalBetween(Double min, Double max);
}