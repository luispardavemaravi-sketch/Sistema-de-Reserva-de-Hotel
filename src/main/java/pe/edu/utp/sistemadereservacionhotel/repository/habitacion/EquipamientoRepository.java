package pe.edu.utp.sistemadereservacionhotel.repository.habitacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.habitacion.Equipamiento;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipamientoRepository extends JpaRepository<Equipamiento, Long> {

    Optional<Equipamiento> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Equipamiento> findByNombreContainingIgnoreCase(String nombre);

    List<Equipamiento> findByCostoAdicionalLessThanEqual(Double costoMaximo);
}