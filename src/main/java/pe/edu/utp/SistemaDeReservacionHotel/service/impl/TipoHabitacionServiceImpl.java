package pe.edu.utp.SistemaDeReservacionHotel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.SistemaDeReservacionHotel.model.TipoHabitacion;
import pe.edu.utp.SistemaDeReservacionHotel.repository.TipoHabitacionRepository;
import pe.edu.utp.SistemaDeReservacionHotel.service.TipoHabitacionService;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class TipoHabitacionServiceImpl implements TipoHabitacionService {
    private final TipoHabitacionRepository repo;


    @Override
    public TipoHabitacion save(TipoHabitacion tipoHabitacion) {
        if (tipoHabitacion.getIdTipo() != null) {
            throw new IllegalArgumentException("Para actualizar use el método update");
        }
        if (repo.existsByNombre(tipoHabitacion.getNombre())) {
            throw new IllegalArgumentException("Ya existe un tipo con el nombre: " + tipoHabitacion.getNombre());
        }
        return repo.save(tipoHabitacion);
    }

    @Override
    public TipoHabitacion update(TipoHabitacion tipoHabitacion) {
        if (tipoHabitacion.getIdTipo() == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo para actualizar");
        }
        TipoHabitacion existente = repo.findById(tipoHabitacion.getIdTipo())
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado con ID: " + tipoHabitacion.getIdTipo()));

        existente.setNombre(tipoHabitacion.getNombre());
        existente.setCapacidadMaxima(tipoHabitacion.getCapacidadMaxima());
        existente.setPrecioBase(tipoHabitacion.getPrecioBase());

        return repo.save(existente);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Tipo de habitación no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoHabitacion> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoHabitacion> findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoHabitacion> findByNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        return repo.findByNombre(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoHabitacion> findByCapacidadMinima(Integer capacidad) {
        if (capacidad == null || capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero");
        }
        return repo.findByCapacidadMaximaGreaterThanEqual(capacidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoHabitacion> findByRangoPrecio(Double precioMin, Double precioMax) {
        if (precioMin > precioMax) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor al precio máximo");
        }
        return repo.findByPrecioBaseBetween(precioMin, precioMax);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNombre(String nombre) {
        return repo.existsByNombre(nombre.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }

}
