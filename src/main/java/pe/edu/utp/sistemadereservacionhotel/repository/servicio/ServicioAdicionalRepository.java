package pe.edu.utp.sistemadereservacionhotel.repository.servicio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.servicio.ServicioAdicional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicioAdicionalRepository extends JpaRepository<ServicioAdicional, Long> {
    Optional<ServicioAdicional> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<ServicioAdicional> findByEstaDisponible(Boolean estaDisponible);

    List<ServicioAdicional> findByNombreContainingIgnoreCase(String nombre);

    List<ServicioAdicional> findByPrecioUnitarioLessThanEqual(Double precio);
}