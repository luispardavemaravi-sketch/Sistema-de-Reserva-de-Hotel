package pe.edu.utp.SistemaDeReservacionHotel.service;

import pe.edu.utp.SistemaDeReservacionHotel.model.EstadoReserva;

import java.util.List;
import java.util.Optional;

public interface EstadoReservaService {

    EstadoReserva save(EstadoReserva estadoReserva);

    EstadoReserva update(EstadoReserva estadoReserva);

    void delete(Long id);

    List<EstadoReserva> findAll();

    Optional<EstadoReserva> findById(Long id);

    Optional<EstadoReserva> findByNombreEstado(String nombreEstado);

    List<EstadoReserva> findByEsModificable(Boolean esModificable);

    boolean existsByNombreEstado(String nombreEstado);

    long count();
}