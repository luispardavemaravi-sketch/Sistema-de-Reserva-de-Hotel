package pe.edu.utp.SistemaDeReservacionHotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.SistemaDeReservacionHotel.model.EstadoHabitacion;
import pe.edu.utp.SistemaDeReservacionHotel.repository.EstadoHabitacionRepository;
import pe.edu.utp.SistemaDeReservacionHotel.service.EstadoHabitacionService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class EstadoHabitacionServiceImpl implements EstadoHabitacionService {

    private final EstadoHabitacionRepository repo;

    @Override
    public EstadoHabitacion save(EstadoHabitacion estadoHabitacion) {
        if (estadoHabitacion.getIdEstado() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (repo.existsByNombreEstado(estadoHabitacion.getNombreEstado())) {
            throw new IllegalArgumentException("Ya existe un estado con el nombre: " + estadoHabitacion.getNombreEstado());
        }
        return repo.save(estadoHabitacion);
    }

    @Override
    public EstadoHabitacion update(EstadoHabitacion estadoHabitacion) {
        if (estadoHabitacion.getIdEstado() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        EstadoHabitacion existente = repo.findById(estadoHabitacion.getIdEstado())
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con ID: " + estadoHabitacion.getIdEstado()));

        existente.setNombreEstado(estadoHabitacion.getNombreEstado());
        existente.setEsReservable(estadoHabitacion.getEsReservable());

        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Estado de habitación no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoHabitacion> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EstadoHabitacion> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EstadoHabitacion> findByNombreEstado(String nombreEstado) {
        if (nombreEstado == null || nombreEstado.isBlank()) {
            throw new IllegalArgumentException("El nombre del estado no puede estar vacío");
        }
        return repo.findByNombreEstado(nombreEstado.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoHabitacion> findByEsReservable(Boolean esReservable) {
        if (esReservable == null) {
            throw new IllegalArgumentException("El parámetro esReservable no puede ser nulo");
        }
        return repo.findByEsReservable(esReservable);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombreEstado(String nombreEstado) {
        return repo.existsByNombreEstado(nombreEstado.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}