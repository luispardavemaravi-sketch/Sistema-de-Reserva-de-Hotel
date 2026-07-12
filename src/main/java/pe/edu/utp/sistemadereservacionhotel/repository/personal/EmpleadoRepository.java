package pe.edu.utp.sistemadereservacionhotel.repository.personal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Empleado;

import java.util.List;
import java.util.Optional;


@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);

    List<Empleado> findByApellidoContainingIgnoreCase(String apellido);

    List<Empleado> findByCiudadContainingIgnoreCase(String ciudad);

    List<Empleado> findByDireccionContainingIgnoreCase(String direccion);

    List<Empleado> findByTelefonoContainingIgnoreCase(String telefono);

    List<Empleado> findByEmailContainingIgnoreCase(String email);

    List<Empleado> findByCargoContainingIgnoreCase(String cargo);

    Optional<Empleado> findByEmail(String email);

    boolean existsByEmail(String email);
}