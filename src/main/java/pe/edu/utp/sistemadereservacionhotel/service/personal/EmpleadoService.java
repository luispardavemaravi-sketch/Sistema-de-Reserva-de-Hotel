package pe.edu.utp.sistemadereservacionhotel.service.personal;

import pe.edu.utp.sistemadereservacionhotel.model.personal.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {
    Empleado save(Empleado empleado);

    Empleado update(Empleado empleado);

    void delete(Long id);

    List<Empleado> findAll();

    Optional<Empleado> findById(Long id);

    Optional<Empleado> findByEmail(String email);

    List<Empleado> findByNombre(String nombre);

    List<Empleado> findByApellido(String apellido);

    List<Empleado> findByCargo(String cargo);

    List<Empleado> findByCiudad(String ciudad);

    boolean existsByEmail(String email);

    long count();


}
