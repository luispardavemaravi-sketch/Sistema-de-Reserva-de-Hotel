package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import pe.edu.utp.sistemadereservacionhotel.model.finanzas.MetodoPago;

import java.util.List;
import java.util.Optional;

public interface MetodoPagoService {
    MetodoPago save(MetodoPago metodoPago);

    MetodoPago update(MetodoPago metodoPago);

    void delete(Long id);

    List<MetodoPago> findAll();

    Optional<MetodoPago> findById(Long id);

    Optional<MetodoPago> findByNombre(String nombre);

    List<MetodoPago> findByEsDigital(Boolean esDigital);

    boolean existsByNombre(String nombre);

    long count();
}

