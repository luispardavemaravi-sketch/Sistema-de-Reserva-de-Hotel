package pe.edu.utp.sistemadereservacionhotel.service.servicio;

import pe.edu.utp.sistemadereservacionhotel.model.servicio.ServicioAdicional;

import java.util.List;
import java.util.Optional;

public interface ServicioAdicionalService {
    ServicioAdicional save(ServicioAdicional servicio);

    ServicioAdicional update(ServicioAdicional servicio);

    void delete(Long id);

    List<ServicioAdicional> findAll();

    Optional<ServicioAdicional> findById(Long id);

    Optional<ServicioAdicional> findByNombre(String nombre);

    List<ServicioAdicional> findByNombreContaining(String nombre);

    List<ServicioAdicional> findByEstaDisponible(Boolean disponible);

    List<ServicioAdicional> findByPrecioMaximo(Double precio);

    boolean existsByNombre(String nombre);

    long count();
}