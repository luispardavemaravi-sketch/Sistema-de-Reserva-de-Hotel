package pe.edu.utp.sistemadereservacionhotel.service.finanzas;

import pe.edu.utp.sistemadereservacionhotel.model.finanzas.Promocion;

import java.util.List;
import java.util.Optional;

public interface PromocionService {
    Promocion save(Promocion promocion);

    Promocion update(Promocion promocion);

    void delete(Long id);

    List<Promocion> findAll();

    Optional<Promocion> findById(Long id);

    Optional<Promocion> findByCodigoCupon(String codigo);

    List<Promocion> findVigentes();

    List<Promocion> findByPorcentajeMinimo(Double porcentaje);

    boolean existsByCodigoCupon(String codigo);

    long count();
}