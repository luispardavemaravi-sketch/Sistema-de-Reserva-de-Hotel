package pe.edu.utp.SistemaDeReservacionHotel.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

        if (empleado == null) {
            throw new IllegalArgumentException(
                    "El empleado no puede ser nulo"
            );
        }

        if (repo.existsByEmail(empleado.getEmail())) {
            throw new IllegalArgumentException(
                    "El empleado ya existe"
            );
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
        Empleado empleadoExistente = repo.findById(empleado.getIdEmpleado()).orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + empleado.getIdEmpleado()));

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
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("El empleado no existe");
        }
        repo.deleteById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Empleado> findAll() {
        return repo.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Empleado> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id debe ser positivo");
        }
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empleado> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacio");
        }
        return repo.findByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> findByNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");

        }
        return repo.findByNombreContainingIgnoreCase(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> findByApellido(String apellido) {
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacio");

        }
        return repo.findByApellidoContainingIgnoreCase(apellido.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> findByCargo(String cargo) {
        if (cargo == null || cargo.isBlank()) {
            throw new IllegalArgumentException("El cargo no puede estar vacio");
        }
        return repo.findByCargoContainingIgnoreCase(cargo.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> findByCiudad(String ciudad) {
        if (ciudad == null || ciudad.isBlank()) {
            throw new IllegalArgumentException("La ciudad no puede estar vacia");
        }
        return repo.findByCiudadContainingIgnoreCase(ciudad.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacio");
        }

        return repo.existsByEmail(email.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}
