package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import pe.edu.utp.sistemadereservacionhotel.model.finanzas.ComprobantePago;
import pe.edu.utp.sistemadereservacionhotel.model.reserva.Reserva;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ComprobantePagoService {
    ComprobantePago save(ComprobantePago comprobante);

    ComprobantePago update(ComprobantePago comprobante);

    void delete(Long id);

    List<ComprobantePago> findAll();

    Optional<ComprobantePago> findById(Long id);

    Optional<ComprobantePago> findByNumeroSerie(String serie);

    Optional<ComprobantePago> findByReserva(Long idReserva);

    List<ComprobantePago> findByRangoFecha(LocalDateTime inicio, LocalDateTime fin);

    List<ComprobantePago> findByRangoMonto(Double min, Double max);

    long count();

    ComprobantePago generarComprobante(Reserva reserva, Double monto, String tipo);
}
