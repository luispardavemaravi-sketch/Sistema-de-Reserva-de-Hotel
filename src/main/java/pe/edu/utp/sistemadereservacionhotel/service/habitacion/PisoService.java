package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Piso;

import java.util.List;
import java.util.Optional;

public interface PisoService {

    Piso save(Piso piso);

    Piso update(Piso piso);

    void delete(Long id);

    List<Piso> findAll();

    Optional<Piso> findById(Long id);

    Optional<Piso> findByNumeroPiso(Integer numeroPiso);

    List<Piso> findBySector(String sector);

    boolean existsByNumeroPiso(Integer numeroPiso);

    long count();
}