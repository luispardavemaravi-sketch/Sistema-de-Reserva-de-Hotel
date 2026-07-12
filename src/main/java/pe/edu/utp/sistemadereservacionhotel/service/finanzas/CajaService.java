package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Caja;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CajaService {
    Caja save(Caja caja);

    Caja update(Caja caja);

    void delete(Long id);

    List<Caja> findAll();

    Optional<Caja> findById(Long id);

    List<Caja> findByEmpleado(Long idEmpleado);

    List<Caja> findByRangoFecha(LocalDate inicio, LocalDate fin);

    Optional<Caja> findCajaAbierta();

    List<Caja> findByEstaAbierta(Boolean estaAbierta);

    long count();
}
