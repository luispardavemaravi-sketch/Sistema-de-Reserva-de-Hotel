package pe.edu.utp.sistemadereservacionhotel.service.habitacion;

import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Equipamiento;

import java.util.List;
import java.util.Optional;

public interface EquipamientoService {

    Equipamiento save(Equipamiento equipamiento);

    Equipamiento update(Equipamiento equipamiento);

    void delete(Long id);

    List<Equipamiento> findAll();

    Optional<Equipamiento> findById(Long id);

    Optional<Equipamiento> findByNombre(String nombre);

    List<Equipamiento> findByNombreContaining(String nombre);

    List<Equipamiento> findByCostoMaximo(Double costoMaximo);

    boolean existsByNombre(String nombre);

    long count();
}