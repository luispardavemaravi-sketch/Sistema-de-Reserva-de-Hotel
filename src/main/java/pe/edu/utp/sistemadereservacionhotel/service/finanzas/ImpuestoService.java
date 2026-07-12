package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Impuesto;

import java.util.List;
import java.util.Optional;

public interface ImpuestoService {
    Impuesto save(Impuesto impuesto);

    Impuesto update(Impuesto impuesto);

    void delete(Long id);

    List<Impuesto> findAll();

    Optional<Impuesto> findById(Long id);

    Optional<Impuesto> findByNombre(String nombre);

    List<Impuesto> findByPorcentajeMaximo(Double porcentaje);

    boolean existsByNombre(String nombre);

    long count();
}
