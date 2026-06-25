package pe.edu.utp.sistemadereservacionhotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.sistemadereservacionhotel.model.Habitacion;
import pe.edu.utp.sistemadereservacionhotel.repository.HabitacionRepository;
import pe.edu.utp.sistemadereservacionhotel.service.HabitacionService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class HabitacionServiceImpl implements HabitacionService {

    private final HabitacionRepository repo;

    @Override
    public Habitacion save(Habitacion habitacion) {
        if (habitacion.getIdHabitacion() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (repo.existsByNumeroHabitacion(habitacion.getNumeroHabitacion())) {
            throw new IllegalArgumentException("Ya existe una habitación con el número: " + habitacion.getNumeroHabitacion());
        }
        return repo.save(habitacion);
    }

    @Override
    public Habitacion update(Habitacion habitacion) {
        if (habitacion.getIdHabitacion() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        Habitacion existente = repo.findById(habitacion.getIdHabitacion())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada con ID: " + habitacion.getIdHabitacion()));

        existente.setDescripcion(habitacion.getDescripcion());
        existente.setPrecioActual(habitacion.getPrecioActual());
        existente.setEstadoActivo(habitacion.isEstadoActivo());
        existente.setPiso(habitacion.getPiso());
        existente.setTipoHabitacion(habitacion.getTipoHabitacion());
        existente.setEstadoHabitacion(habitacion.getEstadoHabitacion());

        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Habitación no encontrada con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Habitacion> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Habitacion> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Habitacion> findByNumeroHabitacion(String numero) {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("El número de habitación no puede estar vacío");
        }
        return repo.findByNumeroHabitacion(numero.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Habitacion> findByEstadoActivo(boolean activo) {
        return repo.findByEstadoActivo(activo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Habitacion> findByPiso(Long idPiso) {
        return repo.findByPiso_IdPiso(idPiso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Habitacion> findByTipo(Long idTipo) {
        return repo.findByTipoHabitacion_IdTipo(idTipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Habitacion> findByEstadoHabitacion(Long idEstadoHabitacion) {
        return repo.findByEstadoHabitacion_IdEstadoHabitacion(idEstadoHabitacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Habitacion> findByRangoPrecio(Double precioMin, Double precioMax) {
        if (precioMin > precioMax) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor al precio máximo");
        }
        return repo.findByPrecioActualBetween(precioMin, precioMax);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNumeroHabitacion(String numero) {
        return repo.existsByNumeroHabitacion(numero);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }
}
