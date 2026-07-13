package pe.edu.utp.sistemadereservacionhotel.service.servicio;

import pe.edu.utp.sistemadereservacionhotel.model.servicio.ConsumoServicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsumoServicioService {
    ConsumoServicio save(ConsumoServicio consumo);

    ConsumoServicio update(ConsumoServicio consumo);

    void delete(Long id);

    List<ConsumoServicio> findAll();

    Optional<ConsumoServicio> findById(Long id);

    List<ConsumoServicio> findByReserva(Long idReserva);

    List<ConsumoServicio> findByServicio(Long idServicio);

    List<ConsumoServicio> findByRangoFecha(LocalDateTime inicio, LocalDateTime fin);

    long count();
}