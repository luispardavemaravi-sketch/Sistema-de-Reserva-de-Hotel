package pe.edu.utp.SistemaDeReservacionHotel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.utp.SistemaDeReservacionHotel.Model.Empleado;

import java.util.List;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);

    List<Empleado> findByApellidoContainingIgnoreCase(String apellido);

    List<Empleado> findByCiudadContainingIgnoreCase(String ciudad);

    List<Empleado> findByDireccionContainingIgnoreCase(String direccion);

    List<Empleado> findByTelefonoContainingIgnoreCase(String telefono);

    List<Empleado> findByEmailContainingIgnoreCase(String email);

    List<Empleado> findByCargoContainingIgnoreCase(String cargo);
}
