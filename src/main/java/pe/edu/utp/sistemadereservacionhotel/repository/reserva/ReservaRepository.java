package pe.edu.utp.sistemadereservacionhotel.repository.reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {


    Optional<Reserva> findByCodigoReserva(String codigoReserva);

    boolean existsByCodigoReserva(String codigoReserva);

    List<Reserva> findByHuesped_IdHuesped(Long idHuesped);

    List<Reserva> findByEstadoReserva_IdEstado(Long idEstado);

    List<Reserva> findByFechaEntradaPlanificada(LocalDate fecha);

    List<Reserva> findByFechaEntradaPlanificadaBetween(LocalDate inicio, LocalDate fin);

}
