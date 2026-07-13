package pe.edu.utp.sistemadereservacionhotel.service.reserva;

import pe.edu.utp.sistemadereservacionhotel.model.reserva.CheckOut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CheckOutService {

    CheckOut save(CheckOut checkOut);

    CheckOut update(CheckOut checkOut);

    void delete(Long id);

    List<CheckOut> findAll();

    Optional<CheckOut> findById(Long id);

    Optional<CheckOut> findByReserva(Long idReserva);

    List<CheckOut> findByRangoFecha(LocalDateTime inicio, LocalDateTime fin);

    List<CheckOut> findConMulta(Double montoMinimo);

    long count();
}