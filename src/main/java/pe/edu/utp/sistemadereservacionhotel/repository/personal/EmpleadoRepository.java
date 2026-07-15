package pe.edu.utp.sistemadereservacionhotel.repository.personal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.sistemadereservacionhotel.model.personal.CargoEmpleado;
import pe.edu.utp.sistemadereservacionhotel.model.personal.Empleado;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la administración del personal del hotel.
 */
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    List<Empleado> findByNombreContainingIgnoreCase(String nombre);

    List<Empleado> findByApellidoContainingIgnoreCase(String apellido);

    List<Empleado> findByCiudadContainingIgnoreCase(String ciudad);

    List<Empleado> findByDireccionContainingIgnoreCase(String direccion);

    List<Empleado> findByTelefonoContainingIgnoreCase(String telefono);

    List<Empleado> findByEmailContainingIgnoreCase(String email);

    Optional<Empleado> findByEmail(String email);

    boolean existsByEmail(String email);

    /**
     * CAMBIO CRÍTICO: Refactorizado para usar el Enum CargoEmpleado.
     * El uso de 'ContainingIgnoreCase' sobre un Enum rompe el mapeo de Spring Data JPA
     * e impide el arranque de la aplicación.
     */
    List<Empleado> findByCargo(CargoEmpleado cargo);
}