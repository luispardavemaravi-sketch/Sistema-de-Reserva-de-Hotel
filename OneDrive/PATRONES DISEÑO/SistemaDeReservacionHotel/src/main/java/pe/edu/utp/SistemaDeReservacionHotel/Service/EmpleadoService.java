package pe.edu.utp.SistemaDeReservacionHotel.Service;

import pe.edu.utp.SistemaDeReservacionHotel.Model.Empleado;

import java.util.List;

public interface EmpleadoService {

    void save(Empleado empleado);

    List<Empleado> findAll();

    void delete(Empleado empleado);

    List<Empleado> findByCargo(String cargo);

    List<Empleado> findByEmail(String email);


}
