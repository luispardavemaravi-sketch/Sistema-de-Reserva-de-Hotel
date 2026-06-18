package pe.edu.utp.SistemaDeReservacionHotel.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.SistemaDeReservacionHotel.model.Empleado;
import pe.edu.utp.SistemaDeReservacionHotel.repository.EmpleadoRepository;
import pe.edu.utp.SistemaDeReservacionHotel.service.EmpleadoService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository repo;

    //Guardar empleados
    @Override
    public Empleado save(Empleado empleado) {
        if (repo.existsByEmail(empleado.getEmail())) {
            throw new IllegalArgumentException("El empleado ya existe");
        }
        return repo.save(empleado);
    }

    //Actualizar empleados
    @Override
    public Empleado update(Empleado empleado) {
        if (empleado.getIdEmpleado() == null) {
            throw new IllegalArgumentException("El id no puede ser nulo para actualizar");
        }
        // 1. Buscar el empleado existente en DB
        Empleado empleadoExistente = repo.findById(empleado.getIdEmpleado())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + empleado.getIdEmpleado()));

        // 2. Actualizar solo los campos que llegaron
        empleadoExistente.setNombre(empleado.getNombre());
        empleadoExistente.setApellido(empleado.getApellido());
        empleadoExistente.setTelefono(empleado.getTelefono());
        empleadoExistente.setCargo(empleado.getCargo());
        empleadoExistente.setEspecialidad(empleado.getEspecialidad());
        empleadoExistente.setDireccion(empleado.getDireccion());
        empleadoExistente.setCiudad(empleado.getCiudad());

        // 3. Guardar y retornar
        return repo.save(empleadoExistente);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Empleado> findAll() {
        return List.of();
    }

    @Override
    public Optional<Empleado> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Empleado> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<Empleado> findByNombre(String nombre) {
        return List.of();
    }

    @Override
    public List<Empleado> findByApellido(String apellido) {
        return List.of();
    }

    @Override
    public List<Empleado> findByCargo(String cargo) {
        return List.of();
    }

    @Override
    public List<Empleado> findByCiudad(String ciudad) {
        return List.of();
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }
}
