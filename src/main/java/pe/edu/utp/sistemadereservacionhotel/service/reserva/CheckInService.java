package pe.edu.utp.sistemadereservacionhotel.service.reserva;

import pe.edu.utp.sistemadereservacionhotel.model.reserva.CheckIn;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CheckInService {

    CheckIn save(CheckIn checkIn);

    CheckIn update(CheckIn checkIn);

    void delete(Long id);

    List<CheckIn> findAll();

    Optional<CheckIn> findById(Long id);

    Optional<CheckIn> findByReserva(Long idReserva);

    List<CheckIn> findByRangoFecha(LocalDateTime inicio, LocalDateTime fin);

    long count();
}
