package pe.edu.utp.SistemaDeReservacionHotel.service;

import pe.edu.utp.SistemaDeReservacionHotel.model.Reserva;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservaServicio {
    Reserva save(Reserva reserva);

    Reserva update(Reserva reserva);

    void delete(Long id);

    List<Reserva> findAll();

    Optional<Reserva> findById(Long id);

    Optional<Reserva> findByCodigoReserva(String codigo);

    List<Reserva> findByHuesped(Long idHuesped);

    List<Reserva> findByEstado(Long idEstado);

    List<Reserva> findByFechaEntrada(LocalDate fecha);

    List<Reserva> findByRangoFechas(LocalDate inicio, LocalDate fin);

    long count();
}
